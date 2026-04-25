package controller;
//trước tiên là xử lú post request từ fom đánh giá 1 sản phẩm nào đóa
//sau đó kiểm tra quyền, bắt buộc phải có đanưg nhập rồi mới cho phép đánh giá sanr phẩm
// sau khi log suggestfully, redirect quay lại trang sản phẩm chi tiết đêr gửi đánh giá

import dao.ReviewDAO;
import model.Review;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/submit-review")
public class ReviewController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String content = request.getParameter("content");

            if (content != null && !content.trim().isEmpty()) {
                Review review = new Review(productId, user.getId(), rating, content);
                ReviewDAO reviewDAO = new ReviewDAO();
                reviewDAO.insert(review);

                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
            } else {
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&error=empty_content");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
