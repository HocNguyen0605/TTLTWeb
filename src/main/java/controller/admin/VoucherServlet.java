package controller.admin;

import dao.ProductDAO;
import dao.PromotionDAO;
import dao.VoucherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.Promotion;
import model.Voucher;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "VoucherServlet", value = "/admin/voucher")
public class VoucherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String discount_type = request.getParameter("discount_type");
        int discount_value = Integer.parseInt(request.getParameter("discount_value")) ;
        Timestamp start_date = Timestamp.valueOf(request.getParameter("start_date"));
        Timestamp end_date = Timestamp.valueOf(request.getParameter("end_date"));
        String status = request.getParameter("status");
        String code = request.getParameter("code");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if("add".equals(action)) {
            try(Connection conn = DBContext.getConnection();) {
                conn.setAutoCommit(false);
                PromotionDAO  promotionDAO = new PromotionDAO(conn);
                VoucherDAO voucherDAO = new VoucherDAO(conn);
                //Lưu vào promotion
                Promotion promotion = new Promotion();
                promotion.setName(name);
                promotion.setType(type);
                promotion.setDiscount_type(discount_type);
                promotion.setDiscount_value(discount_value);
                promotion.setStart_date(start_date);
                promotion.setEnd_date(end_date);
                promotion.setStatus(status);

                promotionDAO.insertPromotion(promotion);

                // Lưu voucher mới
                Voucher voucher = new Voucher();
                voucher.setCode(code);
                voucher.setPromotionId(promotion.getId());
                voucher.setStartDate(start_date);
                voucher.setEndDate(end_date);
                voucher.setStatus(status);
                voucher.setQuanity(quantity);

                voucherDAO.insertVoucher(voucher);
                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("Lỗi: " + e.getMessage());
            }
        }


        request.getRequestDispatcher("/view/admin/admin-CTKM.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}