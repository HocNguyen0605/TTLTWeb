package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getAllItems().isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        String customerName = request.getParameter("fullName");
        String paymentMethod = request.getParameter("paymentMethod");

        Connection conn = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); //  transaction

            // ===== 1. Tạo Order =====
            Order order = new Order();
            order.setTotalPrice(cart.getTotalPrice());
            order.setOrderDate(new Date());

            if ("COD".equals(paymentMethod)) {
                order.setPaymentMethod("COD");
                order.setPaymentStatus("UNPAID");
                order.setStatus("Chờ xác nhận");
            } else if ("EWALLET".equals(paymentMethod)) {
                order.setPaymentMethod("MOMO");
                order.setPaymentStatus("PENDING");
                order.setStatus("Chờ thanh toán");
            } else {
                throw new ServletException("Phương thức thanh toán không hợp lệ");
            }

            OrderDAO orderDAO = new OrderDAO(conn);
            int orderId = orderDAO.insertAndReturnId(order);

            // ===== 2. Tạo OrderItem =====
            OrderItemDAO itemDAO = new OrderItemDAO();

            for (CartItem ci : cart.getAllItems()) {
                OrderItem oi = new OrderItem();
                oi.setOrderId(orderId);
                oi.setProductId(ci.getProduct().getId());
                oi.setQuantity(ci.getQuantity());
                oi.setPriceAtTime(ci.getPrice());
                itemDAO.insertOrderItem(oi);
            }

            conn.commit(); //  OK
            session.removeAttribute("cart");

            // ===== 3. Redirect theo payment =====
            if ("COD".equals(paymentMethod)) {
                response.sendRedirect("success.jsp");
            } else {
                response.sendRedirect("momo-payment?orderId=" + orderId);
            }

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ignored) {}
            throw new ServletException(e);

        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
    }
}
