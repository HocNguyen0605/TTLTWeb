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

            String code = request.getParameter("codeVoucher");
            HttpSession session = request.getSession(false);

            VoucherDAO vDAO = new VoucherDAO(conn);
            Voucher v = vDAO.getVoucherWithDiscount(code);
            String requestedWith = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(requestedWith)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                if (code == null || code.trim().isEmpty()) {
                    session.removeAttribute("voucher");
                    response.getWriter().write("{\"success\":false,\"error\":\"Vui lòng nhập mã giảm giá\"}");
                } else if (v != null) {
                    session.setAttribute("voucher", v);
                    session.removeAttribute("voucherError");
                    response.getWriter().write("{\"success\":true}");
                } else {
                    session.removeAttribute("voucher");
                    response.getWriter().write("{\"success\":false,\"error\": \"Mã giảm giá không hợp lệ hoăc đã hết hạn\"}");
                }
                return;
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        }catch(Exception e){
                throw new RuntimeException(e);
            }
        }

}