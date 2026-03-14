package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login"); // Redirect if access via GET
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        UserDAO dao = new UserDAO();
        response.setContentType("text/plain");

        if (dao.isUserEmailExists(email)) {
            // 1. Tạo mật khẩu mới ngẫu nhiên (8 ký tự)
            String newPassword = java.util.UUID.randomUUID().toString().substring(0, 8);

            // 2. Cập nhật vào Database (Hàm updatePassword bạn đã có trong UserDAO)
            boolean isUpdated = dao.updatePassword(email, newPassword);

            if (isUpdated) {
                // 3. Gửi email thực tế dùng MailUtil đã hoàn thiện ở bước trước
                String subject = "Mật khẩu mới cho tài khoản Juicy";
                String content = "Chào bạn, mật khẩu mới của bạn là: <b>" + newPassword + "</b><br>Vui lòng đăng nhập và đổi lại mật khẩu ngay.";

                // Dùng hàm gửi mail bạn đã có (nên dùng bản sendEmail trả về boolean)
                boolean mailSent = util.MailUtil.sendForgotPasswordMail(email, newPassword); // Hoặc MailUtil.sendEmail(...)

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