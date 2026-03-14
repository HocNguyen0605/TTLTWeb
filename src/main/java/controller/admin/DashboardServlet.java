package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy dữ liệu từ DAO
        try (java.sql.Connection conn = util.DBContext.getConnection()) {
            dao.AdminDAO adminDAO = new dao.AdminDAO(conn);

            // 1. Số liệu tổng quan
            int todayOrders = adminDAO.countOrdersToday();
            int weekOrders = adminDAO.countOrdersWeek();
            int monthOrders = adminDAO.countOrdersMonth();
            int totalOrders = adminDAO.getTotalOrders();
            double totalRevenue = adminDAO.getTotalRevenue();

            req.setAttribute("todayOrders", todayOrders);
            req.setAttribute("weekOrders", weekOrders);
            req.setAttribute("monthOrders", monthOrders);
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("totalRevenue", totalRevenue);

            // 2. Dữ liệu biểu đồ doanh thu (7 ngày gần nhất)
            java.util.Map<java.sql.Date, Double> revenueMap = adminDAO.getRevenueLast7Days();
            // Chuyển Map thành mảng JSON cho JS
            StringBuilder labels = new StringBuilder("[");
            StringBuilder data = new StringBuilder("[");

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM");
            boolean first = true;
            for (java.util.Map.Entry<java.sql.Date, Double> entry : revenueMap.entrySet()) {
                if (!first) {
                    labels.append(",");
                    data.append(",");
                }
                labels.append("\"").append(sdf.format(entry.getKey())).append("\"");
                data.append(entry.getValue());
                first = false;
            }
            labels.append("]");
            data.append("]");

            req.setAttribute("revenueLabels", labels.toString());
            req.setAttribute("revenueData", data.toString());

            // 3. Top sản phẩm bán chạy
            java.util.List<model.TopProductDTO> topProducts = adminDAO.getTopSellingProducts(5);
            req.setAttribute("topProducts", topProducts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Đảm bảo đường dẫn này trỏ đúng đến file .jsp admin của bạn
        req.getRequestDispatcher("/view/admin/admin-dashboard.jsp").forward(req, resp);
    }
}