

package controller;

import com.google.gson.JsonObject;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.ShippingInfoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

        // Lấy cart từ session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getAllItems().isEmpty()) {
            response.sendRedirect("product.jsp");
            return;
        }

        // Lấy dữ liệu từ form checkout.jsp
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");
        String provinceIdStr = request.getParameter("provinceId");
        String districtIdStr = request.getParameter("districtId");
        String wardCode = request.getParameter("wardCode");

        // Tạo Order
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
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); //TRANSACTION

            ProductDAO productDAO = new ProductDAO(conn);
            double checkedTotalPrice = 0;
            boolean hasCheckedItem = false;
            //Kiểm tra có các item có đủ bán so với kho
            for (CartItem item : cart.getAllItems()) {
                if (!item.isChecked()) continue;
                hasCheckedItem = true;
                checkedTotalPrice += item.getTotalPrice();
                Product p = productDAO.getProductForUpdate(item.getProduct().getId());
                if (p == null || p.getQuantity() < item.getQuantity()) {
                    throw new Exception("Sản phẩm " + item.getProduct().getName() + " không đủ tồn kho!");
                }
                boolean isSuccess = productDAO.updateProductQuantity(item.getProduct().getId(), item.getQuantity(), p.getVersion());
                if (!isSuccess) {
                    throw new Exception("Sản phẩm " + item.getProduct().getName() + " vừa thay đổi, vui lòng thử lại!");
                }
            }
            order.setTotalPrice(checkedTotalPrice);

            if (!hasCheckedItem) {
                throw new Exception("Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
            }

            // Sau khi chắc chắn đủ hàng mới bắt đầu ghi order
            OrderDAO orderDAO = new OrderDAO(conn);
            int orderId = orderDAO.insertAndReturnId(order);

            OrderItemDAO orderItemDAO = new OrderItemDAO(conn);
            for (CartItem item : cart.getAllItems()) {
                if (!item.isChecked()) continue;
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
            
            String shippingFeeStr = request.getParameter("shippingFee");
            double shippingFee = 0;
            if (shippingFeeStr != null && !shippingFeeStr.isEmpty()) {
                try {
                    shippingFee = Double.parseDouble(shippingFeeStr);
                } catch (Exception e) {}
            } else if (districtIdStr != null && !districtIdStr.isEmpty() && wardCode != null && !wardCode.isEmpty()) {
                try {
                    int weight = 0;
                    if (cart != null) {
                        for (model.CartItem item : cart.getAllItems()) {
                            if (item.isChecked()) {
                                weight += item.getProduct().getVolume() * item.getQuantity();
                            }
                        }
                    }
                    JsonObject feeJson = util.GHNUtils.calculateFee(Integer.parseInt(districtIdStr), wardCode, weight);
                    if (feeJson != null && feeJson.has("code") && feeJson.get("code").getAsInt() == 200) {
                        shippingFee = feeJson.getAsJsonObject("data").get("total").getAsDouble();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            shippingInfo.setShippingFee(shippingFee);
            
            shippingInfo.setNote("");
            if (provinceIdStr != null && !provinceIdStr.isEmpty()) {
                shippingInfo.setProvinceId(Integer.parseInt(provinceIdStr));
            }
            if (districtIdStr != null && !districtIdStr.isEmpty()) {
                shippingInfo.setDistrictId(Integer.parseInt(districtIdStr));
            }
            shippingInfo.setWardCode(wardCode);
            shippingDAO.insert(shippingInfo);
            conn.commit(); // Thành công
            session.removeAttribute("cart");
            if (session.getAttribute("auth") != null) {
                User auth = (User) session.getAttribute("auth");
                new dao.CartDAO(conn).clearCart(auth.getId());
            }
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException) && e.getCause() == null
                    && e.getClass() == Exception.class) {
            } else {
                e.printStackTrace();
            }
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