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

@WebServlet({"/admin/reply-review", "/add-review-comment"})
public class ReplyReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");

        if (auth == null) {
            sendResponse(response, false, "Bạn cần đăng nhập để thực hiện hành động này!");
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            // Chấp nhận cả 'reply' hoặc 'content' làm tên tham số
            String content = request.getParameter("content");
            if (content == null) content = request.getParameter("reply");

            if (content == null || content.trim().isEmpty()) {
                sendResponse(response, false, "Nội dung không được để trống!");
                return;
            }

            ReviewDAO reviewDAO = new ReviewDAO();

            // Lưu vào bảng bình luận (Threaded Comments)
            Review.ReviewComment comment = new Review.ReviewComment(reviewId, auth.getId(), content.trim());
            reviewDAO.insertComment(comment);

            sendResponse(response, true, "Đã gửi phản hồi thành công!");
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
