package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Date;
import java.util.Properties;
import model.Contact;

public class MailUtil {

    private static final String FROM_EMAIL = "weebooguy@gmail.com";
    private static final String APP_PASSWORD = "jtrw hgae qbqa mhjm";
    private static final String TO_EMAIL = "luonghoaisangvn@gmail.com";

    public static void sendContactMail(Contact c) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(TO_EMAIL)
            );

            message.setSubject("Liên hệ mới từ khách hàng");

            String content = """
                    Họ tên: %s
                    Email: %s
                    SĐT: %s

                    Nội dung:
                    %s
                    """.formatted(
                    c.getFullName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getMessage()
            );

            message.setText(content);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendOTPMail(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            // 1. Cấu hình Header tiếng Việt (Áp dụng từ code mới)
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            message.setSubject("Mã xác thực đăng ký - JUICY", "UTF-8");
            message.setSentDate(new Date()); // Quy định ngày gửi

            // 2. Nội dung có định dạng HTML (Áp dụng từ code mới)
            String htmlContent = "<h3>Mã xác thực của bạn là: <b style='color:red;'>" + otp + "</b></h3>"
                    + "<p>Mã này có hiệu lực trong <b>60 giây</b>.</p>";

            message.setContent(htmlContent, "text/HTML; charset=UTF-8");

            Transport.send(message);
            System.out.println(">>> Gửi OTP thành công tới: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println(">>> LỖI GỬI MAIL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendForgotPasswordMail(String toEmail, String newPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // Đảm bảo bạn dùng đúng FROM_EMAIL và APP_PASSWORD đã cấu hình
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Tiêu đề riêng biệt
            message.setSubject("Khôi phục mật khẩu tài khoản JUICY", "UTF-8");

            // Nội dung email chuyên nghiệp hơn
            String htmlContent = "<h2>Yêu cầu cấp lại mật khẩu</h2>"
                    + "<p>Chào bạn,</p>"
                    + "<p>Chúng tôi đã nhận được yêu cầu khôi phục mật khẩu cho tài khoản liên kết với email này.</p>"
                    + "<p>Mật khẩu mới của bạn là: <b style='color: #28a745; font-size: 1.2em;'>" + newPassword + "</b></p>"
                    + "<p>Vui lòng sử dụng mật khẩu này để đăng nhập và <b>đổi lại mật khẩu mới</b> ngay lập tức để đảm bảo an toàn.</p>"
                    + "<br><p>Trân trọng,<br>Đội ngũ hỗ trợ Juicy.</p>";

            message.setContent(htmlContent, "text/HTML; charset=UTF-8");

            Transport.send(message);
            System.out.println(">>> Đã gửi mật khẩu mới tới: " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
