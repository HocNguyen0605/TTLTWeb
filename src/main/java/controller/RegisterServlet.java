package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String userOtp = request.getParameter("otp");

        Map<String, String> errors = new HashMap<>();
        HttpSession session = request.getSession();

        // --- BƯỚC 1: KIỂM TRA LỖI NHẬP LIỆU CƠ BẢN ---
        if (userDAO.isUserEmailExists(email)) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (userDAO.isUserNameExists(username)) {
            errors.put("username", "Tên tài khoản đã tồn tại!");
        }
        if (password == null || password.isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống");
        } else if (!isStrongPassword(password)) {
            errors.put("password", "Mật khẩu ≥ 8 ký tự, có chữ hoa và ký tự đặc biệt");
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Nhập lại mật khẩu chưa chính xác");
        }

        // --- BƯỚC 2: KIỂM TRA OTP (QUAN TRỌNG) ---
        String serverOtp = (String) session.getAttribute("otpCode");
        Long otpTime = (Long) session.getAttribute("otpTime");

        if (serverOtp == null || userOtp == null || !serverOtp.equals(userOtp)) {
            errors.put("otp", "Mã OTP không chính xác!");
        } else if (otpTime == null || (System.currentTimeMillis() - otpTime > 60000)) {
            errors.put("otp", "Mã OTP đã hết hạn (quá 60 giây)!");
        }

        // --- BƯỚC 3: TRẢ VỀ NẾU CÓ BẤT KỲ LỖI NÀO ---
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("oldUsername", username);
            request.setAttribute("oldFullName", fullName);
            request.setAttribute("oldEmail", email);
            request.setAttribute("activeTab", "register");
            request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
            return;
        }

        // --- BƯỚC 4: TIẾN HÀNH ĐĂNG KÝ (KHI MỌI THỨ ĐÃ ĐÚNG) ---
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setRole(0);

            userDAO.register(newUser);

            // Xóa OTP khỏi session sau khi dùng xong để bảo mật
            session.removeAttribute("otpCode");
            session.removeAttribute("otpTime");

            // 2. TỰ ĐỘNG ĐĂNG NHẬP: Lưu user vừa tạo vào Session
            session.setAttribute("auth", newUser);
            // Nếu filter của bạn kiểm tra role, hãy đảm bảo set đúng
            session.setAttribute("role", "user");

            // 3. CHUYỂN HƯỚNG: Đưa thẳng tới trang sản phẩm
            // Thay "/products" bằng URL chuẩn của trang sản phẩm bên bạn (ví dụ: /product hoặc /shop)
            response.sendRedirect(request.getContextPath() + "/product");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
        }
    }

    // Hàm kiểm tra mật khẩu mạnh
    private boolean isStrongPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        return hasUpper && hasSpecial;
    }
}
