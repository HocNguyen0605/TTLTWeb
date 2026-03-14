package controller.admin;

import dao.OrderDAO;
import model.Order;
import model.User;
import util.DBContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
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

        if (user.getRole() != 1) { // 1 is ADMIN
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

        if (user.getRole() != 1) {
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
                // ➕ THÊM ĐƠN
                case "add":
                    // String customerName = request.getParameter("customerName");
                    // double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
                    // Order order = new Order();
                    // ...
                    // orderDAO.addOrder(order);
                    break;

                // 🔄 CẬP NHẬT TRẠNG THÁI
                case "updateStatus":
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    String status = request.getParameter("status");
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
