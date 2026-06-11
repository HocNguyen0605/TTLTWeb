package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import util.MailUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> result = new HashMap<>();

        if (dao.isUserEmailExists(email)) {
            // Tạo mật khẩu mới ngẫu nhiên
            String newPassword = UUID.randomUUID().toString().substring(0, 8);
            String hashedNewPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());

            // Cập nhật vào Database
            boolean isUpdated = dao.updatePassword(email, hashedNewPassword);

            if (isUpdated) {
                String subject = "Khôi phục mật khẩu tài khoản JUICY";
                String htmlContent = "<h2>Yêu cầu cấp lại mật khẩu</h2>"
                        + "<p>Chào bạn,</p>"
                        + "<p>Mật khẩu mới của bạn là: <b style='color: #28a745; font-size: 1.2em;'>" + newPassword + "</b></p>"
                        + "<p>Vui lòng đăng nhập và đổi lại mật khẩu ngay.</p>";

                boolean mailSent = MailUtil.sendMail(email, subject, htmlContent);

                if (mailSent) {
                    result.put("status", "success");
                    result.put("message", "Mật khẩu mới đã được gửi vào Email của bạn!");
                } else {
                    result.put("status", "error");
                    result.put("message", "Không thể gửi email, vui lòng thử lại sau.");
                }
            } else {
                result.put("status", "error");
                result.put("message", "Lỗi cập nhật CSDL.");
            }
        } else {
            result.put("status", "error");
            result.put("message", "Email này không tồn tại trong hệ thống!");
        }
        response.getWriter().write(new com.google.gson.Gson().toJson(result));
    }
}