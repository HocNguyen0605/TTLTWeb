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

        if (!user.getPassword().equals(oldPass)) {
            request.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
        }
        else if (!newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
        }
        else {
            if (dao.updatePassword(user.getEmail(), newPass)) {
                user.setPassword(newPass);
                session.setAttribute("auth", user);
                request.setAttribute("message", "Đổi mật khẩu thành công!");
            } else {
                request.setAttribute("error", "Lỗi khi cập nhật mật khẩu!");
            }
        }
        request.setAttribute("activeTab", "password");
        
        request.getRequestDispatcher("/view/user/profile/profile.jsp").forward(request, response);
    }
}
