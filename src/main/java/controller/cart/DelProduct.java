package controller.cart;
import model.CartItem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.Product;
import jakarta.servlet.annotation.*;
import java.io.IOException;

import model.Cart;
import controller.ProductServlet;
public class DelProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        CartItem cartitem= cart.deleteProduct(id);
        if (  cartitem != null) {
            session.setAttribute("cart", cart);
            response.sendRedirect("cart.jsp");
        } else {
            response.sendRedirect("cart.jsp");

        }
    }
}
