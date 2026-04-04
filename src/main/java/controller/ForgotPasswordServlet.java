package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import util.MailUtil;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        UserDAO dao = new UserDAO();
        response.setContentType("text/plain");

        if (dao.isUserEmailExists(email)) {
            // Tạo mật khẩu mới ngẫu nhiên (8 ký tự)
            String newPassword = java.util.UUID.randomUUID().toString().substring(0, 8);

            // Cập nhật vào Database
            boolean isUpdated = dao.updatePassword(email, newPassword);

            if (isUpdated) {
                // Gửi email thực tế dùng MailUtil đã hoàn thiện ở bước trước
                // Thay vì MailUtil.sendForgotPasswordMail(email, newPassword);
                String subject = "Khôi phục mật khẩu tài khoản JUICY";
                String htmlContent = "<h2>Yêu cầu cấp lại mật khẩu</h2>"
                        + "<p>Chào bạn,</p>"
                        + "<p>Mật khẩu mới của bạn là: <b style='color: #28a745; font-size: 1.2em;'>" + newPassword + "</b></p>"
                        + "<p>Vui lòng đăng nhập và đổi lại mật khẩu ngay.</p>";

                boolean mailSent = MailUtil.sendMail(email, subject, htmlContent);

                if (mailSent) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("error_mail");
                }
            } else {
                response.getWriter().write("error_db");
            }
        } else {
            response.getWriter().write("not_found");
        }
    }
}