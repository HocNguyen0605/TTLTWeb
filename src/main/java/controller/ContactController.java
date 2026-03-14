package controller;

import dao.ContactDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Contact;
import util.MailUtil;

import java.io.IOException;

@WebServlet("/contact")
public class ContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Chuyển hướng người dùng đến trang contact.jsp khi truy cập link
        req.getRequestDispatcher("/view/user/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Đảm bảo nhận được tiếng Việt từ Form
        request.setCharacterEncoding("UTF-8");

        try {
            // Lấy dữ liệu từ các ô input (Lưu ý: name trong input phải khớp với các chuỗi này)
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String subject = request.getParameter("subject"); // Thêm phần tiêu đề
            String message = request.getParameter("message");

            // 1. Đóng gói dữ liệu vào Model
            Contact contact = new Contact();
            contact.setFullName(fullName);
            contact.setEmail(email);
            contact.setPhone(phone);
            contact.setSubject(subject != null ? subject : "Liên hệ từ khách hàng");
            contact.setMessage(message);
            contact.setIdUser(null); // Khách vãng lai

            // 2. Lưu vào Database (Sửa lỗi Static bằng cách tạo 'new ContactDAO()')
            ContactDAO dao = new ContactDAO();
            dao.insert(contact);

            // 3. Gửi mail thông báo
            MailUtil.sendContactMail(contact);

            // 4. Trả về thông báo thành công
            request.setAttribute("success", "Gửi liên hệ thành công! Chúng tôi sẽ phản hồi sớm.");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại sau.");
        }

        // Quay lại trang contact để hiển thị thông báo
        request.getRequestDispatcher("/view/user/contact.jsp").forward(request, response);
    }
}