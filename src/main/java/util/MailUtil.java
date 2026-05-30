package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Date;
import java.util.Properties;
import model.Contact;

public class MailUtil {

    private static final String FROM_EMAIL = ConfigLoader.getProperty("mail.from");
    private static final String APP_PASSWORD = ConfigLoader.getProperty("mail.password");
    private static final String TO_EMAIL_DEFAULT = ConfigLoader.getProperty("mail.to.default");

    private static Properties getSmtpProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", ConfigLoader.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", ConfigLoader.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", ConfigLoader.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", ConfigLoader.getProperty("mail.smtp.starttls.enable"));
        return props;
    }
    public static void sendContactMail(Contact c) {
        Properties props = getSmtpProperties();

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
                    InternetAddress.parse(TO_EMAIL_DEFAULT)
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
        Properties props = getSmtpProperties();
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
