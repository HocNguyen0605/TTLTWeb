package controller.admin;

import dao.ReviewDAO;
import model.Review;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/admin/reply-review"})
public class ReplyReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");

        // Chỉ cho phép ADMIN (role admin thôi nha) trả lời đánh giá
        if (auth == null || auth.getRole() != 1) {
            sendResponse(response, false, "Bạn không có quyền thực hiện hành động này!");
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            String content = request.getParameter("reply");
            if (content == null) content = request.getParameter("content");

            if (content == null || content.trim().isEmpty()) {
                sendResponse(response, false, "Nội dung phản hồi không được để trống!");
                return;
            }

            ReviewDAO reviewDAO = new ReviewDAO();
            // Sử dụng updateSellerReply thay vì insertComment để thống nhất
            // logic phản hồi của Shop
            reviewDAO.updateSellerReply(reviewId, content.trim());

            sendResponse(response, true, "Đã gửi phản hồi từ Shop thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, false, "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void sendResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.print(String.format("{\"status\": \"%s\", \"message\": \"%s\"}", success ? "success" : "error", message));
            out.flush();
        }
    }
}
