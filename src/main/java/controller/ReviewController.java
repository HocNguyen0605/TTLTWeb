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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/submit-review")
public class ReviewController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            result.put("status", "error");
            result.put("message", "Vui lòng đăng nhập để đánh giá.");
            response.getWriter().write(gson.toJson(result));
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String content = request.getParameter("content");

            if (content != null && !content.trim().isEmpty()) {
                Review review = new Review(productId, user.getId(), rating, content);
                review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                review.setUserName(user.getFullName());

                ReviewDAO reviewDAO = new ReviewDAO();
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

        response.getWriter().write(gson.toJson(result));
    }
}
