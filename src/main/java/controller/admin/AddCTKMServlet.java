package controller.admin;

import dao.ProductDAO;
import dao.PromotionComboItemDAO;
import dao.PromotionDAO;
import model.Product;
import model.Promotion;
import model.PromotionComboItem;
import util.DBContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "AddCTKMServlet", value = "/admin/addCTKM")
public class AddCTKMServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int p1 = Integer.parseInt(request.getParameter("product_id_1"));
        int q1 = Integer.parseInt(request.getParameter("quantity_1"));
        String p2Str = request.getParameter("product_id_2");
        int p2 = (p2Str != null) ? Integer.parseInt(p2Str) : 0;
        int q2 = Integer.parseInt(request.getParameter("quantity_2"));
        String discountType = request.getParameter("discount_type");
        int discountValue = Integer.parseInt(request.getParameter("discount_value"));
        String type = request.getParameter("type");
        String status = request.getParameter("status");
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.parse(request.getParameter("start_date")));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.parse(request.getParameter("end_date")));
        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PromotionDAO promotionDAO = new PromotionDAO(conn);
                PromotionComboItemDAO promotionComboItemDAO = new PromotionComboItemDAO(conn);
                ProductDAO productDAO = new ProductDAO(conn);
                Promotion promotion = new Promotion();
                promotion.setName(name);
                promotion.setType(type);
                promotion.setDiscount_type(discountType);
                promotion.setDiscount_value(discountValue);
                promotion.setStart_date(startDate);
                promotion.setEnd_date(endDate);
                promotion.setStatus(status);

                promotionDAO.insertPromotion(promotion);

                //  Lưu danh sách sản phẩm áp dụng vào bảng chi tiết
                if (p1 > 0) {
                    PromotionComboItem comboItem1 = new PromotionComboItem();
                    comboItem1.setComboId(promotion.getId());
                    comboItem1.setProductId(p1);
                    comboItem1.setQuantity(q1);
                    promotionComboItemDAO.insert(comboItem1);

                    productDAO.updatePromotionById(p1, promotion.getId());
                }

                // Lưu sản phẩm 2
                if (p2 > 0) {
                    PromotionComboItem comboItem2 = new PromotionComboItem();
                    comboItem2.setComboId(promotion.getId());
                    comboItem2.setProductId(p2);
                    comboItem2.setQuantity(q2);

                    promotionComboItemDAO.insert(comboItem2);
                    productDAO.updatePromotionById(p2, promotion.getId());

                }

                conn.commit();
                response.sendRedirect("CTKM?msg=success");
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                response.getWriter().println("Lỗi xử lý: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}