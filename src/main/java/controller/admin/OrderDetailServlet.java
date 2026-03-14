package controller.admin;

import com.google.gson.Gson;
import dao.OrderDAO;
import model.Order;
import model.ShippingInfo;
import util.DBContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/order-detail")
public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection conn = DBContext.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            int orderId = Integer.parseInt(idParam);

            // Fetch info
            ShippingInfo shippingInfo = orderDAO.getShippingInfoByOrderId(orderId);

            Map<String, Object> data = new HashMap<>();
            if (shippingInfo != null) {
                data.put("receiverName", shippingInfo.getReceiverName());
                data.put("receiverPhone", shippingInfo.getReceiverPhone());
                data.put("address", shippingInfo.getAddress());
                data.put("note", shippingInfo.getNote());
            } else {
                data.put("error", "Information not found");
            }

            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
