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

    public static boolean sendMail(String toEmail, String subject, String htmlContent) {
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
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            message.setSubject(subject, "UTF-8");
            message.setContent(htmlContent, "text/HTML; charset=UTF-8");

            Transport.send(message);
            System.out.println(">>> Gửi mail thành công tới: " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
