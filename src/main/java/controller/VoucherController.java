package controller;

import dao.VoucherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Voucher;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/apply-voucher")
public class VoucherController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection conn = DBContext.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String code = request.getParameter("codeVoucher");
        HttpSession session = request.getSession(false);

        VoucherDAO vDAO = new VoucherDAO();
        Voucher v = vDAO.getVoucherWithDiscount(code);
        if(code==null || code.trim().isEmpty()){
            session.removeAttribute("voucher");
            session.setAttribute("voucherError", "Vui lòng nhập mã giảm giá!");
        }
        else if (v != null) {
            session.setAttribute("voucher", v);
            session.removeAttribute("voucherError");
        } else {
            session.removeAttribute("voucher");
            session.setAttribute("voucherError", "*Mã giảm giá không hợp lệ hoặc đã hết hạn!");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}