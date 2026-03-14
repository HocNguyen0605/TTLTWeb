package controller.cart;

import model.Cart;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Product;

import java.io.IOException;

@WebServlet("/add_cart")
public class AddCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy session (cho phép tạo mới để lưu cart)
        HttpSession session = request.getSession();

        // 2 đăng nhập mới được add cart
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        // 3. Validate dữ liệu
        int id;
        int quantity;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp");
            return;
        }

        if (quantity <= 0) {
            response.sendRedirect("error.jsp");
            return;
        }

        // 4. Lấy product
        ProductDAO dao = new ProductDAO();
        Product p = dao.findById(id);
        if (p == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        // 5. Lấy hoặc tạo cart
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        cart.addProduct(p, quantity);
        session.setAttribute("cart", cart);

        // 6. Redirect về trang sản phẩm / giỏ hàng
        response.sendRedirect("product.jsp");
        // hoặc: response.sendRedirect("cart.jsp");
    }
}
