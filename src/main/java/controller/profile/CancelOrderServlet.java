package controller.profile;


import com.google.gson.Gson;
import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/cancel-order")
public class CancelOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("status", "error");
            result.put("messege", "Vui lòng đăng nhập.");
            response.getWriter().write(new Gson().toJson(result));
            return;
        }
        String orderIdStr = request.getParameter("orderId");
        String reason = request.getParameter("reason");

        if (orderIdStr == null || orderIdStr.trim().isEmpty() || reason == null || reason.trim().length() < 10) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("status", "error");
            result.put("message", "Vui lòng cung cấp lý do hủy đơn (tối thiểu 10 ký tự).");
            response.getWriter().write(new Gson().toJson(result));
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO dao = new OrderDAO();
            boolean success = dao.cancelOrder(orderId, user.getId(), reason);

            if (success) {
                result.put("status", "success");
                result.put("message", "Hủy đơn hàng thành công.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("status", "error");
                result.put("message", "Không thể hủy đơn hàng này.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("status", "error");
            result.put("message", "ID đơn hàng không hợp lệ.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("status", "error");
            result.put("message", "Lỗi hệ thống.");
        }

        response.getWriter().write(new Gson().toJson(result));
    }
}
