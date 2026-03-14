package controller;

import dao.VoucherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Voucher;

import java.io.IOException;
@WebServlet("/apply-voucher")
public class VoucherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("codeVoucher");
        HttpSession session = request.getSession(false);

        VoucherDAO vDAO = new VoucherDAO();
        Voucher v = vDAO.getVoucherWithDiscount(code);

        if (v != null) {
            session.setAttribute("voucher", v);
        } else {
            session.removeAttribute("voucher");
            session.setAttribute("voucherError", "Mã giảm giá không hợp lệ hoặc đã hết hạn!");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}