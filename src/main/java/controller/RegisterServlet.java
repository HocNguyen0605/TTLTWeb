package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import util.GoogleUtils;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login");
        String googleLoginUrl = GoogleUtils.buildGoogleLoginUrl(request.getSession());
        request.setAttribute("googleLoginUrl", googleLoginUrl);
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

        boolean isApi = request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");

        // trả về nếu có lỗi trong fields
        if (!errors.isEmpty()) {
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("status", "error");
                result.put("errors", errors);
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                session.setAttribute("errors", errors);
                session.setAttribute("oldUsername", username);
                session.setAttribute("oldFullName", fullName);
                session.setAttribute("oldEmail", email);
                session.setAttribute("activeTab", "register");
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        // bắt đầu điền vào db
        try {
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setRole(0);

            userDAO.register(newUser);

            session.removeAttribute("otpCode");
            session.removeAttribute("otpTime");

            session.setAttribute("auth", newUser);
            session.setAttribute("role", "user");

            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Map<String, String> result = new java.util.HashMap<>();
                result.put("status", "success");
                result.put("redirect", request.getContextPath() + "/products");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("status", "error");
                Map<String, String> errMap = new java.util.HashMap<>();
                errMap.put("system", "Lỗi hệ thống: " + e.getMessage());
                result.put("errors", errMap);
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                session.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
                session.setAttribute("oldUsername", username);
                session.setAttribute("oldFullName", fullName);
                session.setAttribute("oldEmail", email);
                session.setAttribute("activeTab", "register");
                response.sendRedirect(request.getContextPath() + "/login");
            }
        }
    }
}