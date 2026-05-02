package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullname");
        String userOtp = request.getParameter("otp");

        HttpSession session = request.getSession();

        // check otp
        String serverOtp = (String) session.getAttribute("otpCode");
        Long otpTime = (Long) session.getAttribute("otpTime");

        validator.AuthValidator validator = new validator.AuthValidator(userDAO);
        Map<String, String> errors = validator.validateRegister(username, fullName, email, password, confirmPassword, userOtp, serverOtp, otpTime);

        // trả về nếu có lỗi trong fields
        if (!errors.isEmpty()) {
            session.setAttribute("errors", errors);
            session.setAttribute("oldUsername", username);
            session.setAttribute("oldFullName", fullName);
            session.setAttribute("oldEmail", email);
            session.setAttribute("activeTab", "register");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // bắt đầu điền vào db
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setRole(0);

            userDAO.register(newUser);

            session.removeAttribute("otpCode");
            session.removeAttribute("otpTime");

            session.setAttribute("auth", newUser);
            session.setAttribute("role", "user");

            response.sendRedirect(request.getContextPath() + "/product");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            session.setAttribute("oldUsername", username);
            session.setAttribute("oldFullName", fullName);
            session.setAttribute("oldEmail", email);
            session.setAttribute("activeTab", "register");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}