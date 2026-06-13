package controller.admin;

import dao.BannerDAO;
import dao.BannerProductDAO;
import dao.PromotionComboItemDAO;
import dao.PromotionDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Banner;
import model.Product;
import model.Promotion;
import model.PromotionComboItem;
import service.CloudinaryService;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
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
        int promotionId = Integer.parseInt(request.getParameter("promotionId"));


        CloudinaryService cloudinaryService = new CloudinaryService();
            try(Connection conn = DBContext.getConnection()) {
                conn.setAutoCommit(false);
                //Xử lý thêm và update banner
                BannerDAO bannerDAO = new BannerDAO(conn);
                PromotionComboItemDAO pciDAO = new PromotionComboItemDAO(conn);
                BannerProductDAO bpDAO = new BannerProductDAO(conn);
                if ("add".equals(action)) {
                    Part filePart = request.getPart("bannerImage");
                    if (filePart != null && filePart.getSize() > 0) {
                        byte[] fileBytes = filePart.getInputStream().readAllBytes();
                        String secureUrl = cloudinaryService.uploadImage(fileBytes, filePart.getSubmittedFileName(), "banners");
                        //Thêm thông tin banner vào DB
                        Banner banner = new Banner();
                        banner.setTitle(title);
                        banner.setImageUrl(secureUrl);
                        banner.setLinkUrl(linkUrl);
                        banner.setPriority(priority);
                        banner.setIsActive(true);

                        int bannerId=bannerDAO.insertBanner(banner);

                        //Thêm thông tin các id sản phẩm khuyến mãi vào DB
                        List<PromotionComboItem> listItem = pciDAO.getItemsByComboId(promotionId);
                        System.out.println(listItem.size());
                        if(listItem.size()>0){
                            for (PromotionComboItem item : listItem) {
                                bpDAO.insertBannerProduct(bannerId,item.getProductId(),promotionId);
                            }
                        }
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
                        Banner oldBanner = bannerDAO.getBannerById(id);
                        finalImageUrl = (oldBanner != null) ? oldBanner.getImageUrl() : "";
                    }

                    Banner updatedBanner = new Banner(id, title, finalImageUrl, linkUrl, priority, isActive, null);
                    bannerDAO.updateBanner(updatedBanner);
                    bpDAO.deleteBannerProducts(id);

                    if (promotionId > 0) {
                        List<PromotionComboItem> listItem = pciDAO.getItemsByComboId(promotionId);
                        if (listItem != null && listItem.size() > 0) {
                            // Nếu chương trình khuyến mãi có danh sách sản phẩm đi kèm
                            for (PromotionComboItem item : listItem) {
                                bpDAO.insertBannerProduct(id, item.getProductId(), promotionId);
                            }
                        } else {
                            bpDAO.insertBannerProduct(id, 0, promotionId);
                        }
                    }
                }
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/admin/banner");
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Banner> banners = new ArrayList<>();
        List<Promotion>  listComboPromotion = new ArrayList<>();
        String search = request.getParameter("search");

        try(Connection conn =DBContext.getConnection()){
            BannerDAO bannerDAO = new BannerDAO(conn);
            if (search != null && !search.trim().isEmpty()) {
                banners = bannerDAO.getBannerByTitle(search.trim());
            }  else {
                banners = bannerDAO.getAllBanners();
            }

            PromotionDAO promotionDAO = new PromotionDAO(conn);
            listComboPromotion =promotionDAO.getActiveComboPromotions();
            request.setAttribute("listComboPromotion", listComboPromotion);
        }catch(Exception e){
            e.printStackTrace();
        }

        request.setAttribute("banners", banners);
        request.setAttribute("currentSearch", search);
        request.getRequestDispatcher("/view/admin/admin-banner.jsp").forward(request, response);

    }
}