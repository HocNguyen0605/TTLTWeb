package controller.profile;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import java.io.IOException;

@WebServlet("/updateProfile")
public class UpdateProfileController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        User currentUser = (User) request.getSession().getAttribute("auth");
        String newName = request.getParameter("fullName");
        String newEmail = request.getParameter("email");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Validate
        validator.ProfileValidator validator = new validator.ProfileValidator();
        java.util.Map<String, String> errors = validator.validate(fullName, email, phone);

        if (!errors.isEmpty()) {
            session.setAttribute("errors", errors);
            // PRG - Redirect back to profile page
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        UserDAO dao = new UserDAO();

        user.setUsername(request.getParameter("username"));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone != null && phone.trim().isEmpty() ? null : phone.trim());

        user.setAddress(address != null && address.trim().isEmpty() ? null : address.trim());

        session.removeAttribute("errors");

        if (dao.updateProfile(user)) {
            session.setAttribute("auth", user);
            session.setAttribute("message", "Cập nhật thông tin thành công!");
        } else {
            session.setAttribute("error", "Cập nhật thất bại!");
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
