package controller.admin;

import dao.BannerDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Banner;
import model.Product;
import service.CloudinaryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
@WebServlet(name = "BannerServlet", value = "/admin/banner")
public class BannerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String title = request.getParameter("title");
        String linkUrl = request.getParameter("link_url");
        int priority = Integer.parseInt(request.getParameter("priority"));

        BannerDAO dao = new BannerDAO();
        CloudinaryService cloudinaryService = new CloudinaryService();
            try {
                if ("add".equals(action)) {
                    Part filePart = request.getPart("bannerImage");
                    if (filePart != null && filePart.getSize() > 0) {
                        byte[] fileBytes = filePart.getInputStream().readAllBytes();
                        String secureUrl = cloudinaryService.uploadImage(fileBytes, filePart.getSubmittedFileName(), "banners");

                        Banner banner = new Banner();
                        banner.setTitle(title);
                        banner.setImageUrl(secureUrl);
                        banner.setLinkUrl(linkUrl);
                        banner.setPriority(priority);
                        banner.setIsActive(true);

                        dao.insertBanner(banner);
                    }
                }
                else if ("update".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean isActive = Boolean.parseBoolean(request.getParameter("is_active"));

                    Part filePart = request.getPart("bannerImage");
                    String finalImageUrl;

                    if (filePart != null && filePart.getSize() > 0) {
                        // Có ảnh mới thì upload
                        byte[] fileBytes = filePart.getInputStream().readAllBytes();
                        finalImageUrl = cloudinaryService.uploadImage(fileBytes, filePart.getSubmittedFileName(), "banners");
                    } else {
                        // Không chọn ảnh mới thì lấy lại link ảnh cũ từ DB
                        Banner oldBanner = dao.getBannerById(id);
                        finalImageUrl = (oldBanner != null) ? oldBanner.getImageUrl() : "";
                    }

                    Banner updatedBanner = new Banner(id, title, finalImageUrl, linkUrl, priority, isActive, null);
                    dao.updateBanner(updatedBanner);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/admin/banner");
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BannerDAO bannerDAO = new BannerDAO();
        List<Banner> banners = new ArrayList<>();
        String search = request.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            banners = bannerDAO.getBannerByTitle(search.trim());
        }  else {
            banners = bannerDAO.getAllBanners();
        }

        request.setAttribute("banners", banners);
        request.setAttribute("currentSearch", search);
        request.getRequestDispatcher("/view/admin/admin-banner.jsp").forward(request, response);

    }
}