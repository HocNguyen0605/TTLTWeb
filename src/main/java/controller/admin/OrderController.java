package controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.OrderDAO;
import model.Order;
import model.User;
import util.DBContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.time.Instant;
import java.util.List;

@WebServlet("/admin/manage-orders")
public class OrderController extends HttpServlet {

    // ================== HIỂN THỊ DANH SÁCH ĐƠN ==================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("auth") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (user.getRole() < 1) { // 1 is ADMIN
            response.sendRedirect(request.getContextPath() + "/view/user/403.jsp");
            return;
        }

        try (Connection conn = DBContext.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            List<Order> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);

            request.getRequestDispatcher("/view/admin/admin-orders.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Database Error");
        }
    }

    // ================== XỬ LÝ HÀNH ĐỘNG ==================

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("auth") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (user.getRole() < 1) {
            response.sendRedirect(request.getContextPath() + "/view/user/403.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("manage-orders");
            return;
        }

        try (Connection conn = DBContext.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);

            switch (action) {
                // THÊM ĐƠN
                case "add":
                    // String customerName = request.getParameter("customerName");
                    // double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
                    // Order order = new Order();
                    // ...
                    // orderDAO.addOrder(order);
                    break;

                // CẬP NHẬT TRẠNG THÁI
                case "updateStatus":
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    String status = request.getParameter("status");

                    if ("confirmed".equals(status)) {
                        model.Order order = orderDAO.getOrderById(orderId);
                        if (order != null && (order.getTrackingCode() == null || order.getTrackingCode().trim().isEmpty())) {
                            dao.OrderItemDAO itemDAO = new dao.OrderItemDAO(conn);
                            dao.ProductDAO productDAO = new dao.ProductDAO(conn);
                            model.ShippingInfo sInfo = orderDAO.getShippingInfoByOrderId(orderId);

                            if (sInfo != null) {
                                JsonArray itemsArray = new JsonArray();
                                for (model.OrderItem oi : itemDAO.getItemsByOrderId(orderId)) {
                                    model.Product p = productDAO.getProductForUpdate(oi.getProductId());
                                    JsonObject itemJson = new JsonObject();
                                    itemJson.addProperty("name", p != null ? p.getName() : "Sản phẩm");
                                    itemJson.addProperty("quantity", oi.getQuantity());
                                    itemJson.addProperty("weight", 100); // Tạm đặt weight
                                    itemsArray.add(itemJson);
                                }

                                JsonObject ghnResp = util.GHNUtils.createOrder(
                                        sInfo.getReceiverName(),
                                        sInfo.getReceiverPhone(),
                                        sInfo.getAddress(),
                                        sInfo.getDistrictId() != null ? sInfo.getDistrictId() : 0,
                                        sInfo.getWardCode() != null ? sInfo.getWardCode() : "",
                                        itemsArray
                                );

                                if (ghnResp != null && ghnResp.has("code") && ghnResp.get("code").getAsInt() == 200) {
                                    JsonObject data = ghnResp.getAsJsonObject("data");
                                    String trackingCode = data.get("order_code").getAsString();
                                    String expectedDeliveryTimeStr = data.get("expected_delivery_time").getAsString();

                                    java.sql.Timestamp expectedDate = null;
                                    try {
                                        Instant instant = Instant.parse(expectedDeliveryTimeStr);
                                        expectedDate = java.sql.Timestamp.from(instant);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    orderDAO.updateTrackingCodeAndExpectedDate(orderId, trackingCode, expectedDate);
                                }
                            }
                        }
                    }

                    orderDAO.updateStatus(orderId, status);
                    break;

                // ❌ XÓA 1 ĐƠN
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("orderId"));
                    orderDAO.deleteOrder(deleteId);
                    break;

                // 🔥 XÓA TOÀN BỘ ĐƠN
                case "deleteAll":
                    orderDAO.deleteAllOrders();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("manage-orders");
    }
}
