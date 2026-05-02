package controller.admin;

import dao.ReviewDAO;
import model.Review;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reviews")
public class AdminReviewController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReviewDAO reviewDAO = new ReviewDAO();
        List<Review> reviews = reviewDAO.getAllReviews();

        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("/view/admin/admin-reviews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                ReviewDAO reviewDAO = new ReviewDAO();
                reviewDAO.delete(id);
                // Xóa thành công, tải lại trang
                response.sendRedirect(request.getContextPath() + "/admin/reviews");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/reviews?error=invalid_id");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/reviews");
        }
    }
}
