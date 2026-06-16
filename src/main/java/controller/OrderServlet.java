

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
        String provinceIdStr = request.getParameter("provinceId");
        String districtIdStr = request.getParameter("districtId");
        String wardCode = request.getParameter("wardCode");

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
            double checkedTotalPrice = 0;
            boolean hasCheckedItem = false;
            //Kiểm tra có các item có đủ bán so với kho
            for (CartItem item : cartItemsChecked) {
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
            conn.commit(); // Lưu đơn hàng thành công

            // Xóa các item đã checkout khỏi cart trong session
            cart.removeCheckedItems();
            session.setAttribute("cart", cart);

            if ("BANKING".equals(paymentMethod)) {
                response.sendRedirect(request.getContextPath() + "/payment/vnpay?orderId=" + orderId);
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
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