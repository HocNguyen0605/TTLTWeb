package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/product")
public class ListProductController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ProductService ps = new ProductService();

        // 1. Lấy trang hiện tại
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int pageSize = 12; // 12 sản phẩm mỗi trang

        // 2. Lấy dữ liệu
        List<Product> list = ps.getProductsByPage(page, pageSize);
        int totalProducts = ps.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // 3. Set attributes
        request.setAttribute("productList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // 4. Forward
        request.getRequestDispatcher("/view/user/products.jsp").forward(request, response);
    }

    public void destroy() {
    }

}
