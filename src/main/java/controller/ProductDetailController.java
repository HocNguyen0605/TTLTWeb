package controller;

import dao.ProductDAO;
import model.Product;
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
