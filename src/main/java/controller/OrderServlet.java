package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Cart;
import model.CartItem;
import model.Order;
import model.User;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Lấy cart từ session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getAllItems().isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        // 2. Lấy dữ liệu từ form checkout.jsp
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        // 3. Tạo Order
        Order order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("PENDING");
        order.setOrderDate(new Date());

        // lấy user từ session
        User user = (User) session.getAttribute("auth");
        if (user != null) {
            order.setUserId(user.getId());
        }

        Connection conn = null;

        try {
            // 4. Lấy connection
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // TRANSACTION

            OrderDAO orderDAO = new OrderDAO(conn);
            OrderItemDAO orderItemDAO = new OrderItemDAO();

            // 5. Insert order → lấy orderId
            int orderId = orderDAO.insertAndReturnId(order);

            // 6. Insert order_items từ cart
            for (CartItem item : cart.getAllItems()) {
                model.OrderItem orderItem = new model.OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(item.getProduct().getId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPriceAtTime(item.getProduct().getPrice());

                orderItemDAO.insertOrderItem(orderItem);
            }

            // 6.5 Insert shipping info
            dao.ShippingInfoDAO shippingDAO = new dao.ShippingInfoDAO(conn);
            model.ShippingInfo shippingInfo = new model.ShippingInfo();
            shippingInfo.setOrderId(orderId);
            shippingInfo.setReceiverName(fullName);
            shippingInfo.setReceiverPhone(phone);
            shippingInfo.setAddress(address);
            shippingInfo.setShippingFee(0); // Mặc định 0
            shippingInfo.setNote("");

            shippingDAO.insert(shippingInfo);

            // 7. Commit transaction
            conn.commit();

            // 8. Xóa cart
            session.removeAttribute("cart");

            // 9. Redirect sang trang thành công
            response.sendRedirect("order-success.jsp");

        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new ServletException(e);
        }
    }

}