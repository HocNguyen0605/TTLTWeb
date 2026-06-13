package controller.admin;

import dao.ContactDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Contact;
import util.MailUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/contacts")
public class AdminContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ContactDAO dao = new ContactDAO();
        List<Contact> contacts = dao.getAll();
        req.setAttribute("contacts", contacts);
        req.getRequestDispatcher("/view/admin/admin-contacts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("reply".equals(action)) {
            try {
                int idContact = Integer.parseInt(req.getParameter("idContact"));
                String replyMessage = req.getParameter("replyMessage");

                ContactDAO dao = new ContactDAO();
                Contact contact = dao.findById(idContact);

                if (contact != null) {
                    // Send Email to the customer
                    String htmlContent = "<p>Chào " + contact.getFullName() + ",</p>"
                            + "<p>Cảm ơn bạn đã liên hệ với Juicy. Dưới đây là phản hồi của chúng tôi:</p>"
                            + "<p><i>" + replyMessage.replace("\n", "<br>") + "</i></p>"
                            + "<p>Trân trọng,<br>Đội ngũ Juicy</p>";

                    boolean mailSent = MailUtil.sendMail(contact.getEmail(), "Phản hồi từ Juicy - " + contact.getSubject(), htmlContent);

                    if (mailSent) {
                        dao.updateStatus(idContact, "Đã phản hồi");
                        req.getSession().setAttribute("successMsg", "Đã gửi phản hồi thành công!");
                    } else {
                        req.getSession().setAttribute("errorMsg", "Gửi mail thất bại. Vui lòng kiểm tra lại cấu hình mail.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.getSession().setAttribute("errorMsg", "Có lỗi xảy ra: " + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/contacts");
    }
}
