package controller;

import dao.ReviewDAO;
import model.Review;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/about")
public class AboutController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ReviewDAO reviewDAO = new ReviewDAO();
        List<Review> testimonials = reviewDAO.getTopPositiveReviews(3);
        req.setAttribute("testimonials", testimonials);

        req.getRequestDispatcher("/view/user/about.jsp").forward(req, resp);
    }
}
