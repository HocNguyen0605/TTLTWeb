package controller.profile;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet("/changePassword")
public class ChangePasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        java.util.Map<String, Object> result = new java.util.HashMap<>();

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("status", "error");
            result.put("message", "Vui lòng đăng nhập lại.");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
            return;
        }

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        validator.ProfileValidator validator = new validator.ProfileValidator();
        java.util.Map<String, String> errors = validator.validatePasswordChange(oldPass, user.getPassword(), newPass, confirmPass);

        if (!errors.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Mã 400 cho lỗi validation
            result.put("status", "error");
            result.put("errors", errors);
        } else {
            UserDAO dao = new UserDAO();
            if (dao.updatePassword(user.getEmail(), newPass)) {
                user.setPassword(newPass);
                session.setAttribute("auth", user);
                result.put("status", "success");
                result.put("message", "Đổi mật khẩu thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                result.put("status", "error");
                result.put("message", "Lỗi khi cập nhật mật khẩu!");
            }
        }
        response.getWriter().write(new com.google.gson.Gson().toJson(result));
    }
}