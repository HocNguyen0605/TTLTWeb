package controller.profile;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import java.io.IOException;

@WebServlet("/updateEmail")
public class UpdateEmailController extends HttpServlet {

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

        String email = request.getParameter("email");
        String userOtp = request.getParameter("otp");

        String serverOtp = (String) session.getAttribute("otpCode");
        Long otpTime = (Long) session.getAttribute("otpTime");

        UserDAO dao = new UserDAO();

        // Validate
        validator.ProfileValidator validator = new validator.ProfileValidator();
        java.util.Map<String, String> errors = validator.validateEmailUpdate(email, userOtp, serverOtp, otpTime, dao, user.getEmail());

        if (!errors.isEmpty()) {
            session.setAttribute("errors", errors);
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        user.setEmail(email);

        session.removeAttribute("errors");
        session.removeAttribute("otpCode");
        session.removeAttribute("otpTime");

        if (dao.updateProfile(user)) {
            session.setAttribute("auth", user);
            session.setAttribute("message", "Cập nhật Email thành công!");
        } else {
            session.setAttribute("error", "Cập nhật Email thất bại!");
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
