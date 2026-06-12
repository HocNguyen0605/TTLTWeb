package controller.profile;

import dao.OrderDAO;
import dao.RefundDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.RefundRequest;
import model.User;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@WebServlet("/user/requestRefund")
public class RequestRefundServlet extends HttpServlet {

    private static final int REFUND_WINDOW_DAYS = 3;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");
        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng đăng nhập!\"}");
            return;
        }

        // 2. Kiểm tra lý do
        String reasonParam = request.getParameter("reason");
        if (reasonParam == null || reasonParam.trim().length() < 5) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Lý do hoàn tiền không hợp lệ (ít nhất 5 ký tự).\"}");
            return;
        }
        String reason = reasonParam.trim();

        // 3. Kiểm tra orderId
        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mã đơn hàng không hợp lệ.\"}");
            return;
        }

        try (Connection conn = DBContext.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            RefundDAO refundDAO = new RefundDAO(conn);

            // 4. Lấy thông tin đơn hàng
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Không tìm thấy đơn hàng.\"}");
                return;
            }

            // 5. Kiểm tra quyền sở hữu
            if (order.getUserId() != auth.getId()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Bạn không có quyền yêu cầu hoàn tiền cho đơn này.\"}");
                return;
            }

            // 6. Kiểm tra trạng thái đơn phải là 'delivered'
            if (!"delivered".equalsIgnoreCase(order.getStatus())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Chỉ có thể yêu cầu hoàn tiền cho đơn hàng đã giao thành công.\"}");
                return;
            }

            // 7. Kiểm tra thời gian giao hàng trong vòng 3 ngày
            Timestamp deliveredDate = order.getDeliveredDate();
            if (deliveredDate == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Không xác định được ngày giao hàng.\"}");
                return;
            }
            Instant cutoff = Instant.now().minus(REFUND_WINDOW_DAYS, ChronoUnit.DAYS);
            if (deliveredDate.toInstant().isBefore(cutoff)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Đã quá " + REFUND_WINDOW_DAYS + " ngày kể từ khi giao hàng. Không thể yêu cầu hoàn tiền.\"}");
                return;
            }

            // 8. Kiểm tra yêu cầu hoàn tiền trước đó
            RefundRequest existing = refundDAO.getLatestRefundByOrderId(orderId);
            if (existing != null) {
                String existingStatus = existing.getStatus();
                if ("PENDING".equalsIgnoreCase(existingStatus)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"status\":\"error\",\"message\":\"Yêu cầu hoàn tiền đang được xử lý. Vui lòng chờ phản hồi từ quản trị viên.\"}");
                    return;
                }
                if ("APPROVED".equalsIgnoreCase(existingStatus)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"status\":\"error\",\"message\":\"Yêu cầu hoàn tiền đã được chấp thuận trước đó.\"}");
                    return;
                }
                // REJECTED → cho phép gửi lại
            }

            // 9. Tạo bản ghi refund_requests
            RefundRequest newRequest = new RefundRequest(orderId, auth.getId(), reason, order.getTotalPrice());
            refundDAO.createRefund(newRequest);

            // 10. Cập nhật trạng thái đơn hàng
            orderDAO.updateStatus(orderId, "refund_requested");

            response.getWriter().write("{\"status\":\"success\",\"message\":\"Yêu cầu hoàn tiền đã được ghi nhận. Quản trị viên sẽ xử lý trong thời gian sớm nhất.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Đã xảy ra lỗi hệ thống.\"}");
        }
    }
}