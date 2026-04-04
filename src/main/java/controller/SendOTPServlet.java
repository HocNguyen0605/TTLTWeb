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
        String otp = String.valueOf((int)((Math.random() * 900000) + 100000));

        HttpSession session = request.getSession();
        session.setAttribute("otpCode", otp);
        session.setAttribute("otpTime", System.currentTimeMillis());

        String subject = "Mã xác thực đăng ký - JUICY";
        String htmlContent = "<h3>Mã xác thực của bạn là: <b style='color:red;'>" + otp + "</b></h3>"
                + "<p>Mã này có hiệu lực trong <b>60 giây</b>.</p>";

        MailUtil.sendMail(email, subject, htmlContent);

        response.getWriter().write("success");
    }
}
