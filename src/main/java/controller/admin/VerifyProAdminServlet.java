package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.MailUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet("/admin/verify-pro")
public class VerifyProAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User authUser = (session != null) ? (User) session.getAttribute("auth") : null;

        if (authUser == null || authUser.getRole() != 2) {
            resp.sendRedirect(req.getContextPath() + "/view/user/403.jsp");
            return;
        }

        // Nếu đã xác minh rồi thì chuyển thẳng vô quản lý account
        if (Boolean.TRUE.equals(session.getAttribute("pro_verified"))) {
            resp.sendRedirect(req.getContextPath() + "/admin/accounts");
            return;
        }

        req.getRequestDispatcher("/view/admin/verify-pro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        HttpSession session = req.getSession(false);
        User authUser = (session != null) ? (User) session.getAttribute("auth") : null;

        if (authUser == null || authUser.getRole() != 2) {
            result.put("status", "error");
            result.put("message", "Không có quyền.");
            resp.getWriter().write(gson.toJson(result));
            return;
        }

        String action = req.getParameter("action");

        if ("sendOTP".equals(action)) {
            // Cập nhật lại thông tin User từ Database để đảm bảo lấy được Email mới nhất
            dao.UserDAO userDAO = new dao.UserDAO();
            User latestUser = userDAO.login(authUser.getUsername(), authUser.getPassword());
            if (latestUser != null) {
                session.setAttribute("auth", latestUser);
                authUser = latestUser;
            }

            // Generate 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            session.setAttribute("pro_otp", otp);
            session.setAttribute("pro_otp_time", System.currentTimeMillis());

            // Send Email
            String email = authUser.getEmail();
            if (email == null || email.isEmpty()) {
                result.put("status", "error");
                result.put("message", "Tài khoản của bạn chưa cập nhật Email. Vui lòng cập nhật thông tin cá nhân trước.");
            } else {
                String subject = "Mã xác minh bảo mật - JUICY Pro-Admin";
                String content = "<h2>Xác nhận truy cập Quản lý Tài khoản</h2>"
                        + "<p>Mã OTP của bạn là: <strong style='font-size:24px; color:green;'>" + otp + "</strong></p>"
                        + "<p>Mã này sẽ hết hạn trong 5 phút. Tuyệt đối không chia sẻ mã này cho ai.</p>";
                boolean success = MailUtil.sendMail(email, subject, content);

                if (success) {
                    result.put("status", "success");
                    result.put("message", "Đã gửi mã OTP đến email của bạn (" + maskEmail(email) + ").");
                } else {
                    result.put("status", "error");
                    result.put("message", "Lỗi gửi email. Vui lòng kiểm tra lại cấu hình SMTP.");
                }
            }
        } else if ("verify".equals(action)) {
            String inputOtp = req.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("pro_otp");
            Long otpTime = (Long) session.getAttribute("pro_otp_time");

            if (sessionOtp == null || otpTime == null) {
                result.put("status", "error");
                result.put("message", "Vui lòng yêu cầu mã OTP trước.");
            } else if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) { // 5 mins
                session.removeAttribute("pro_otp");
                session.removeAttribute("pro_otp_time");
                result.put("status", "error");
                result.put("message", "Mã OTP đã hết hạn. Vui lòng gửi lại.");
            } else if (!sessionOtp.equals(inputOtp)) {
                result.put("status", "error");
                result.put("message", "Mã OTP không chính xác.");
            } else {
                // Success
                session.setAttribute("pro_verified", true);
                session.removeAttribute("pro_otp");
                session.removeAttribute("pro_otp_time");

                result.put("status", "success");
                result.put("redirect", req.getContextPath() + "/admin/accounts");
            }
        } else {
            result.put("status", "error");
            result.put("message", "Action không hợp lệ.");
        }

        resp.getWriter().write(gson.toJson(result));
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 3) {
            return name + "@" + parts[1];
        }
        return name.substring(0, 3) + "***@" + parts[1];
    }
}
