package controller;

import dao.ProductDAO;
import dao.ReviewDAO;
import model.Product;
import model.Review;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/product-detail")
public class  ProductDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy ID sản phẩm từ URL (ví dụ: product-detail?id=5)
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                ProductDAO dao = new ProductDAO();

                // 2. Tìm sản phẩm theo ID
                Product p = dao.findById(id);

                if (p != null) {
                    // 3. Đẩy đối tượng sản phẩm sang trang JSP
                    request.setAttribute("product", p);

                    // 4. Lấy danh sách sản phẩm liên quan
                    List<Product> relatedProducts = dao.getRelatedProducts(id);
                    request.setAttribute("relatedProducts", relatedProducts);

                    // 5. Lấy danh sách đánh giá & tính toán thống kê
                    ReviewDAO reviewDAO = new ReviewDAO();
                    List<Review> reviews = reviewDAO.getByProductId(id);
                    request.setAttribute("reviews", reviews);

                    // Tính toán thống kê đánh giá
                    double avgRating = 0;
                    int[] starCounts = new int[6];
                    int commentCount = 0;
                    if (reviews != null && !reviews.isEmpty()) {
                        int sum = 0;
                        for (Review r : reviews) {
                            sum += r.getRating();
                            if (r.getRating() >= 1 && r.getRating() <= 5) {
                                starCounts[r.getRating()]++;
                            }
                            if (r.getContent() != null && !r.getContent().trim().isEmpty()) {
                                commentCount++;
                            }
                        }
                        avgRating = (double) sum / reviews.size();
                    }
                    request.setAttribute("avgRating", avgRating);
                    request.setAttribute("starCounts", starCounts);
                    request.setAttribute("commentCount", commentCount);

                    request.getRequestDispatcher("/view/user/productdetail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // ID không hợp lệ (không phải số)
            }
        }
        // Nếu không thấy ID hoặc sản phẩm, hoặc ID lỗi, về trang 404 hoặc danh sách
        response.sendRedirect("products");
    }
}
