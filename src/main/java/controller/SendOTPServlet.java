package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.MailUtil;

import java.io.IOException;

@WebServlet("/send-otp")
public class SendOTPServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String otp = String.valueOf((int)((Math.random() * 900000) + 100000)); // Tạo mã 6 số

        HttpSession session = request.getSession();
        session.setAttribute("otpCode", otp);
        session.setAttribute("otpTime", System.currentTimeMillis()); // Lưu thời điểm tạo

        // Gửi mail (Dùng MailUtil bạn đã có)
        // Lưu ý: Bạn cần sửa MailUtil để nhận tham số email người nhận
        MailUtil.sendOTPMail(email, otp);

        response.getWriter().write("success");
    }
}
