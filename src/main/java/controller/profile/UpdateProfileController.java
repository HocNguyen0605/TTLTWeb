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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        UserDAO dao = new UserDAO();

        user.setUsername(request.getParameter("username"));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        String phone = request.getParameter("phone");
        user.setPhone(phone != null && phone.trim().isEmpty() ? null : phone.trim());

        String address = request.getParameter("address");
        user.setAddress(address != null && address.trim().isEmpty() ? null : address.trim());

        if (dao.updateProfile(user)) {
            session.setAttribute("auth", user);
            request.setAttribute("message", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("error", "Cập nhật thất bại!");
        }
        
        request.getRequestDispatcher("/view/user/profile/profile.jsp").forward(request, response);
    }
}
