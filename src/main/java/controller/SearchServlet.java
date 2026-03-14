package controller;

import service.ProductService;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String keyword = request.getParameter("query");
        ProductService service = new ProductService();
        List<Product> list = service.searchByName(keyword);

        request.setAttribute("productList", list);
        request.setAttribute("lastSearch", keyword); // Gửi lại từ khóa

        // Forward về trang products.jsp chuẩn
        request.getRequestDispatcher("/view/user/products.jsp").forward(request, response);
    }
}