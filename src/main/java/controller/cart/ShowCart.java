package controller.cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.Product;
import jakarta.servlet.annotation.*;
import java.io.IOException;
public class ShowCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}
