

package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

        // Lấy cart từ session
        Cart cart = (Cart) session.getAttribute("cart");
        List<CartItem> cartItemsChecked = cart.getSelectedItemsForCheckout();
        if (cart == null || cart.getAllItems().isEmpty()) {
            response.sendRedirect("product.jsp");
            return;
        }

        // Lấy dữ liệu từ form checkout.jsp
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        // Tạo Order
        Order order = new Order();
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setOrderDate(new Date());
        double totalPrice = 0;
        for (CartItem item : cartItemsChecked) {
            totalPrice += item.getQuantity() * item.getProduct().getPrice();
        }
        order.setTotalPrice(totalPrice + 15000);


        // lấy user từ session
        User user = (User) session.getAttribute("auth");
        if (user != null) {
            order.setUserId(user.getId());
        }
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); //TRANSACTION

            ProductDAO productDAO = new ProductDAO(conn);
            //Kiểm tra có các item có đủ bán so với kho
            for (CartItem item : cartItemsChecked) {
                Product p = productDAO.getProductForUpdate(item.getProduct().getId());
                if (p == null || p.getQuantity() < item.getQuantity()) {
                    throw new Exception("Sản phẩm " + item.getProduct().getName() + " không đủ tồn kho!");
                }
                boolean isSuccess = productDAO.updateProductQuantity(item.getProduct().getId(), item.getQuantity(), p.getVersion());
                if (!isSuccess) {
                    throw new Exception("Sản phẩm " + item.getProduct().getName() + " vừa thay đổi, vui lòng thử lại!");
                }
            }
            // Sau khi chắc chắn đủ hàng mới bắt đầu ghi order
            OrderDAO orderDAO = new OrderDAO(conn);
            int orderId = orderDAO.insertAndReturnId(order);

            OrderItemDAO orderItemDAO = new OrderItemDAO(conn);
            for (CartItem item : cartItemsChecked) {
                // Ghi chi tiết đơn hàng
                OrderItem orderItem = new OrderItem(orderId, item.getProduct().getId(), item.getQuantity(), item.getProduct().getPrice());
                orderItemDAO.insertOrderItem(orderItem);

            }

            // Ghi shipping info
            ShippingInfoDAO shippingDAO = new ShippingInfoDAO(conn);
            ShippingInfo shippingInfo = new ShippingInfo();
            shippingInfo.setOrderId(orderId);
            shippingInfo.setReceiverName(fullName);
            shippingInfo.setReceiverPhone(phone);
            shippingInfo.setAddress(address);
            shippingInfo.setShippingFee(15000);
            shippingInfo.setNote("");
            shippingDAO.insert(shippingInfo);
            conn.commit(); // Thành công
            conn.commit(); // Lưu đơn hàng thành công
            if ("BANKING".equals(paymentMethod)) {
                response.sendRedirect(request.getContextPath() + "/payment/vnpay?orderId=" + orderId);
            } else {
                for(CartItem item : cartItemsChecked) {
                    ProductDAO productDao = new ProductDAO(conn);
                    productDao.updateProductQuantity(item.getProduct().getId(), item.getQuantity(), item.getQuantity());
                }
                response.sendRedirect(request.getContextPath() + "/cart.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/view/user/cart.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {  conn.setAutoCommit(true);  conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

    }
}