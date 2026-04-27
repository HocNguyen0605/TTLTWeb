package controller;

import dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.MailUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/send-otp")
public class SendOTPServlet extends HttpServlet {
    private dao.UserDAO userDAO = new dao.UserDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String email = request.getParameter("email");
        Map<String, String> result = new HashMap<>();

        if (!util.ValidationUtils.isValidEmail(email)) {
            result.put("status", "error");
            result.put("message", "Email không đúng định dạng.");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
            return;
        }

        if (userDAO.isUserEmailExists(email)) {
            result.put("status", "error");
            result.put("message", "Email này đã tồn tại.");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
            return;
        }
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));

        HttpSession session = request.getSession();
        session.setAttribute("otpCode", otp);
        session.setAttribute("otpTime", System.currentTimeMillis());

        try {
            MailUtil.sendMail(email, "Mã xác thực - JUICY", "Mã của bạn là: " + otp);
            result.put("status", "success");
            result.put("message", "Mã OTP đã được gửi!");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Lỗi dịch vụ gửi mail, vui lòng thử lại sau.");
        }

        response.getWriter().write(new com.google.gson.Gson().toJson(result));
    }
}

