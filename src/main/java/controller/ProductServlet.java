package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Product;
import service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class ProductServlet extends HttpServlet {

    private final ProductService service = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            req.setAttribute("products", service.searchByName(search));
        } else {
            req.setAttribute("products", service.getListProduct());
        }
        req.getRequestDispatcher("/view/admin/admin-products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        int id = 0;
        try {
            if (idStr != null)
                id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {

        }

        if ("hidden".equals(action)) {
            service.hideProduct(id);
        } else if ("show".equals(action)) {
            service.showProduct(id);
        } else if ("update_quantity".equals(action)) {
            String qtyStr = req.getParameter("quantity");
            int quantity = 0;
            try {
                if (qtyStr != null)
                    quantity = Integer.parseInt(qtyStr);
                if (quantity < 0)
                    quantity = 0;
            } catch (NumberFormatException e) {
            }
            service.updateQuantity(id, quantity);
        } else if ("update_price".equals(action)) {
            String priceStr = req.getParameter("price");
            double price = 0;
            try {
                if (priceStr != null)
                    price = Double.parseDouble(priceStr);
                if (price < 0)
                    price = 0;
            } catch (NumberFormatException e) {
            }
            service.updatePrice(id, price);
        }


        resp.sendRedirect(req.getContextPath() + "/admin/products?success=update");
    }

}
