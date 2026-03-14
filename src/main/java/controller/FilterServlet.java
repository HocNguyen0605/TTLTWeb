package controller;

import service.ProductService;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilterServlet", value = "/filter")
public class FilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String supplier = request.getParameter("supplier");
        String priceStr = request.getParameter("maxPrice");
        Double maxPrice = null;
        try {
            if (priceStr != null && !priceStr.isEmpty())
                maxPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
        }

        ProductService service = new ProductService();
        List<Product> list = service.filterProducts(supplier, maxPrice);

        request.setAttribute("productList", list);
        request.setAttribute("currentSupplier", supplier);
        request.setAttribute("currentMaxPriceFilter", priceStr);

        request.getRequestDispatcher("/view/user/products.jsp").forward(request, response);
    }
}
