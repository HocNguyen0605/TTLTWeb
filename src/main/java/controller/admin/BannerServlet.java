package controller.admin;

import dao.BannerDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Banner;
import model.Product;
import service.CloudinaryService;

import java.io.IOException;
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

        if ("add".equals(action)) {
            try {
                Part filePart = request.getPart("bannerImage"); // Trùng với name="bannerImage" trong HTML
                if (filePart != null && filePart.getSize() > 0) {
                    // Đọc file thành mảng byte
                    byte[] fileBytes = filePart.getInputStream().readAllBytes();
                    String fileName = filePart.getSubmittedFileName();

                    CloudinaryService cloudinaryService = new CloudinaryService();
                    String secureUrl = cloudinaryService.uploadImage(fileBytes, fileName, "banners");

                    Banner banner = new Banner();
                    banner.setTitle(title);
                    banner.setImageUrl(secureUrl);
                    banner.setLinkUrl(linkUrl);
                    banner.setPriority(priority);
                    banner.setIsActive(true);

                    BannerDAO dao = new BannerDAO();
                    boolean isSuccess = dao.insertBanner(banner);

                    if (isSuccess) {
                        response.sendRedirect(request.getContextPath() + "/admin/banner");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/banner");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/banner");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BannerDAO bannerDAO = new BannerDAO();
        List<Banner> banners = bannerDAO.getAllBanners();

        request.setAttribute("banners", banners);
        request.getRequestDispatcher("/view/admin/admin-banner.jsp").forward(request, response);

    }
}