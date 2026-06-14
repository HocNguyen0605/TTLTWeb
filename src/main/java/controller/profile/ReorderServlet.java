package controller.profile;

import dao.CartDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Cart;
import model.OrderItem;
import model.Product;
import model.User;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/user/reorder")
public class ReorderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");
        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng đăng nhập!\"}");
            return;
        }
        int orderId;
        //Lấy id đơn hàng
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try (Connection conn = DBContext.getConnection()) {
            OrderItemDAO orderItemDAO = new OrderItemDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            CartDAO cartDAO = new CartDAO(conn);
            //Tìm hàng trong đơn
            List<OrderItem> items = orderItemDAO.getItemsByOrderId(orderId);
            if (items == null || items.isEmpty()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Không tìm thấy sản phẩm nào trong đơn hàng này!\"}");
                return;
            }
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }
            boolean added = false;
            //Duyệt từng đơn hàng
            for (OrderItem item : items) {
                Product product = productDAO.findById(item.getProductId());
                if (product != null) {
                    int maxStock = productDAO.getMaxQuantityById(product.getId());
                    int currentCartQty = cart.getTotalQuantityByID(product.getId());
                    int requestedQty = item.getQuantity();
                    // Nếu số lượng thêm nhiều hơn tồn thì giảm sô lượng thêm xuống
                    if (currentCartQty + requestedQty > maxStock) {
                        requestedQty = maxStock - currentCartQty;
                    }
                    //Add khi thêm > 0
                    if (requestedQty > 0) {
                        cart.addProduct(product, requestedQty);
                        // Thêm vào giỏ
                        cartDAO.addOrUpdateCartItem(auth.getId(), product.getId(), cart.getTotalQuantityByID(product.getId()));
                        added = true;
                    }
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (added) {
                StringBuilder sb = new StringBuilder();
                for (OrderItem item : items) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(item.getProductId());
                }
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Thêm vào giỏ hàng thành công!\",\"productIds\":\"" + sb.toString() + "\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Sản phẩm không còn đủ trong kho!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Đã xảy ra lỗi hệ thống.\"}");
        }
    }
}