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

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        UserDAO dao = new UserDAO();

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        validator.ProfileValidator validator = new validator.ProfileValidator();
        java.util.Map<String, String> errors = validator.validatePasswordChange(oldPass, user.getPassword(), newPass, confirmPass);

        if (!errors.isEmpty()) {
            session.setAttribute("errors", errors);
            session.setAttribute("activeTab", "password");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (dao.updatePassword(user.getEmail(), newPass)) {
            user.setPassword(newPass);
            session.setAttribute("auth", user);
            session.setAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            session.setAttribute("error", "Lỗi khi cập nhật mật khẩu!");
        }
        session.removeAttribute("errors");
        session.setAttribute("activeTab", "password");
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
