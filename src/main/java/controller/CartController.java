package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Cart;
import model.Product;

import java.io.IOException;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                String quantityRaw = request.getParameter("quantity");

                // Nếu có truyền quantity (từ trang detail) thì lấy,
                // nếu không (từ trang list) thì mặc định là 1
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;

                Product product = productDAO.findById(productId);
                if (product != null) {
                    cart.addProduct(product, quantity);
                }
            } else if ("remove".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                cart.deleteProduct(productId);

            } else if ("update".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                String quantityRaw = request.getParameter("quantity");
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                cart.update(productId, quantity);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        session.setAttribute("cart", cart);
// Kiểm tra nếu là yêu cầu AJAX thì chỉ trả về mã thành công
        String xRequestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith) || request.getHeader("accept").contains("application/json")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Nếu vẫn có những form cũ chưa sửa thì dùng redirect như cũ
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // chỉ hiển thị cart
        request.getRequestDispatcher("/view/user/cart.jsp")
                .forward(request, response);
    }
}
