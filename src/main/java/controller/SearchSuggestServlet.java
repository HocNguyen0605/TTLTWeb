package controller;

import com.google.gson.Gson;
import model.Product;
import service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchSuggestServlet", value = "/search-suggest")
public class SearchSuggestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String keyword = request.getParameter("query");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        ProductService service = new ProductService();
        // Lấy danh sách sản phẩm gợi ý, giới hạn 5-10 sản phẩm (hiện tại searchByName trả về tất cả)
        List<Product> list = service.searchByName(keyword.trim());

        // Cắt bớt danh sách nếu quá dài, để không chiếm diện tích màn hình
        if (list.size() > 8) {
            list = list.subList(0, 8);
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);

        response.getWriter().write(json);
    }
}
