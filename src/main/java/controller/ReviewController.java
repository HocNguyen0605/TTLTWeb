package controller;
//trước tiên là xử lú post request từ fom đánh giá 1 sản phẩm nào đóa
//sau đó kiểm tra quyền, bắt buộc phải có đanưg nhập rồi mới cho phép đánh giá sanr phẩm
// sau khi log suggestfully, redirect quay lại trang sản phẩm chi tiết đêr gửi đánh giá

import com.google.gson.Gson;
import dao.ReviewDAO;
import model.Review;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/submit-review", "/like-review", "/reply-review"})
public class ReviewController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        switch (path) {
            case "/submit-review":
                handleSubmitReview(request, response);
                break;
            case "/like-review":
                handleLikeReview(request, response);
                break;
            case "/reply-review":
                handleReplyReview(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void handleSubmitReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            result.put("status", "error");
            result.put("message", "Vui lòng đăng nhập để đánh giá.");
            out.write(gson.toJson(result));
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String content = request.getParameter("content");

            if (content != null && !content.trim().isEmpty()) {
                ReviewDAO reviewDAO = new ReviewDAO();

                // Kiểm tra quyền đánh giá (phải mua và nhận hàng thành công) mới được quyền đánh giá!!
                if (!reviewDAO.canUserReviewProduct(user.getId(), productId)) {
                    result.put("status", "error");
                    result.put("message", "Bạn chỉ có thể đánh giá sản phẩm sau khi đã nhận hàng thành công.");
                    out.write(gson.toJson(result));
                    return;
                }

                Review review = new Review(productId, user.getId(), rating, content);
                review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                review.setUserName(user.getFullName());

                reviewDAO.insert(review);

                result.put("status", "success");
                result.put("message", "Đánh giá của bạn đã được gửi thành công!");
                result.put("review", review);
            } else {
                result.put("status", "error");
                result.put("message", "Nội dung đánh giá không được để trống.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        out.write(gson.toJson(result));
    }

    private void handleLikeReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        PrintWriter out = response.getWriter();

        User auth = (User) request.getSession().getAttribute("auth");
        if (auth == null) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Bạn cần đăng nhập để thực hiện chức năng này.");
            out.print(new Gson().toJson(jsonResponse));
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            ReviewDAO reviewDAO = new ReviewDAO();
            boolean liked = reviewDAO.toggleLike(reviewId, auth.getId());
            jsonResponse.put("status", "success");
            jsonResponse.put("liked", liked);
            out.print(new Gson().toJson(jsonResponse));
        } catch (NumberFormatException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Review ID không hợp lệ.");
            out.print(new Gson().toJson(jsonResponse));
        }
    }

    private void handleReplyReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        PrintWriter out = response.getWriter();

        User auth = (User) request.getSession().getAttribute("auth");
        if (auth == null || auth.getRole() != 1) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Bạn không có quyền thực hiện chức năng này.");
            out.print(new Gson().toJson(jsonResponse));
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            String reply = request.getParameter("reply");

            if (reply == null || reply.trim().isEmpty()) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Nội dung phản hồi không được để trống.");
                out.print(new Gson().toJson(jsonResponse));
                return;
            }

            ReviewDAO reviewDAO = new ReviewDAO();
            reviewDAO.updateSellerReply(reviewId, reply);

            jsonResponse.put("status", "success");
            jsonResponse.put("reply", reply);
            out.print(new Gson().toJson(jsonResponse));
        } catch (NumberFormatException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Dữ liệu không hợp lệ.");
            out.print(new Gson().toJson(jsonResponse));
        }
    }
}
