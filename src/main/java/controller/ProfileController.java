package controller;

import dao.ProductDAO;
import dao.UserDAO;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;
@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("auth") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/view/user/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        // Kiểm tra session phòng trường hợp hết hạn đột ngột
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        UserDAO dao = new UserDAO();

        // Lấy dữ liệu từ form dựa trên action
        if ("updateProfile".equals(action)) {
            user.setUsername(request.getParameter("username"));
            user.setFullName(request.getParameter("fullName"));
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            user.setAddress(request.getParameter("address"));

            if (dao.updateProfile(user)) {
                session.setAttribute("auth", user);
                request.setAttribute("message", "Cập nhật thông tin thành công!");
            } else {
                request.setAttribute("error", "Cập nhật thất bại!");
            }
        }
        else if ("changePassword".equals(action)) {
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
                    user.setPassword(newPass); // Cập nhật mật khẩu mới vào session
                    session.setAttribute("auth", user);
                    request.setAttribute("message", "Đổi mật khẩu thành công!");
                } else {
                    request.setAttribute("error", "Lỗi khi cập nhật mật khẩu!");
                }
            }
            // Gửi tín hiệu để JSP biết cần active tab mật khẩu
            request.setAttribute("activeTab", "password");
        }

        request.getRequestDispatcher("/view/user/profile.jsp").forward(request, response);
    }
}
