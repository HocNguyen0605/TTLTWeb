package controller.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.OrderDAO;
import model.Order;
import util.DBContext;
import util.GHNUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/user/order-tracking")
public class OrderTrackingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            out.print("{\"status\":\"error\",\"message\":\"Không có mã đơn hàng.\"}");
            return;
        }

        try (Connection conn = DBContext.getConnection()) {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = new OrderDAO(conn);
            Order order = orderDAO.getOrderById(orderId);

            if (order == null || order.getTrackingCode() == null || order.getTrackingCode().trim().isEmpty()) {
                out.print("{\"status\":\"error\",\"message\":\"Đơn hàng chưa có mã vận đơn.\"}");
                return;
            }

            String trackingData = GHNUtils.getOrderTracking(order.getTrackingCode());
            if (trackingData == null) {
                out.print("{\"status\":\"error\",\"message\":\"Không thể lấy thông tin vận chuyển.\"}");
            } else {
                JsonObject root = JsonParser.parseString(trackingData).getAsJsonObject();
                if (order.getExpectedDeliveryDate() != null) {
                    root.addProperty("expected_delivery_date", order.getExpectedDeliveryDate().toString());
                }
                out.print(root.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Lỗi hệ thống.\"}");
        }
    }
}
