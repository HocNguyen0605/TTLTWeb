package controller;

import com.google.gson.Gson;
import dao.ProductDAO;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SearchSuggestServlet", value = "/search-suggest")
public class SearchSuggestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.searchByName(query);

        if (products.size() > 8) {
            products = products.subList(0, 8);
        }

        Gson gson = new Gson();
        String json = gson.toJson(products);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
