package controller.admin;

import dao.ProductDAO;
import dao.PurchaseOrderDAO;
import dao.SupplierDAO;
import model.Product;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PurchaseOrderServlet", urlPatterns = {"/admin/purchase-orders"})
public class PurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("getDetailJson".equals(action)) {
            response.setContentType("application/json;charset=UTF-8");
            String idParam = request.getParameter("id");

            if (idParam != null && !idParam.isBlank()) {
                try (Connection conn = DBContext.getConnection()) {
                    PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO(conn);
                    PurchaseOrder order = purchaseOrderDAO.getOrderById(Integer.parseInt(idParam));

                    if (order != null) {
                        List<PurchaseOrderDetail> orderItems = purchaseOrderDAO.getOrderDetailsByOrderId(order.getOrderId());

                        // Gom danh sách sản phẩm thành các chuỗi JSON con
                        List<String> itemJsons = new ArrayList<>();
                        for (PurchaseOrderDetail item : orderItems) {
                            String img = item.getProductImg() != null ? item.getProductImg() : "";
                            String name = item.getProductName().replace("\"", "\\\"");

                            itemJsons.add(String.format(
                                    "{\"productId\":%d,\"productName\":\"%s\",\"productImg\":\"%s\",\"quantity\":%d,\"importPrice\":%.2f}",
                                    item.getProductId(), name, img, item.getQuantity(), item.getImportPrice()
                            ));
                        }

                        // Gộp tất cả lại thành chuỗi JSON finall
                        long timestamp = (order.getCreatedDate() != null) ? order.getCreatedDate().getTime() : 0;

                        String finalJson = String.format(
                                "{\"orderId\":%d,\"createdDate\":%d,\"totalAmount\":%.2f,\"status\":\"%s\",\"items\":[%s]}",
                                order.getOrderId(), timestamp, order.getTotalAmount(), order.getStatus(),
                                String.join(",", itemJsons)
                        );
                        response.getWriter().write(finalJson);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(500);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                    return;
                }
            }
            response.setStatus(400);
            response.getWriter().write("{\"error\":\"Mã đơn hàng không hợp lệ\"}");
            return;
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("message") != null) {
            request.setAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        if (session.getAttribute("errorMessage") != null) {
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        String search = request.getParameter("search");
        List<PurchaseOrder> purchaseOrdersList = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            ProductDAO productDAO = new ProductDAO(conn);
            SupplierDAO supplierDAO = new SupplierDAO(conn);
            PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO(conn);

            if (search != null && !search.trim().isEmpty()) {
                String searchKeyword = search.trim();

                purchaseOrdersList = purchaseOrderDAO.searchOrdersWithPendingStatus(searchKeyword);
                request.setAttribute("currentSearch", searchKeyword);

                if (purchaseOrdersList.isEmpty()) {
                    request.setAttribute("errorMessage", "Không tìm thấy dữ liệu đơn hàng phù hợp với từ khóa: '" + searchKeyword + "'");
                }
            } else {
                purchaseOrdersList = purchaseOrderDAO.getPendingOrders();

                request.removeAttribute("currentSearch");
            }

            request.setAttribute("availableProducts", productDAO.getAvailableProducts());
            request.setAttribute("allSuppliers", supplierDAO.getAllSuppliers());
            request.setAttribute("pendingOrders", purchaseOrdersList);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
        }

        request.getRequestDispatcher("/view/admin/purchase-orders.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createOrder(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/purchase-orders");        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String supplierIdParam = request.getParameter("supplierId");
        String note = request.getParameter("note");
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        String[] importPrices = request.getParameterValues("importPrice");

        if (supplierIdParam == null || supplierIdParam.isBlank()) {
            session.setAttribute("errorMessage", "Vui lòng chọn nhà cung cấp.");
            response.sendRedirect(request.getContextPath() + "/view/admin/purchase-orders.jsp");
            return;
        }

        if (productIds == null || productIds.length == 0) {
            session.setAttribute("errorMessage", "Vui lòng chọn ít nhất một sản phẩm để đặt hàng.");
            response.sendRedirect(request.getContextPath() + "/view/admin/purchase-orders.jsp");
            return;
        }

        try {
            int supplierId = Integer.parseInt(supplierIdParam);
            List<PurchaseOrderDetail> items = new ArrayList<>();

            for (int i = 0; i < productIds.length; i++) {
                int quantity = Integer.parseInt(quantities[i]);
                double importPrice = Double.parseDouble(importPrices[i]);

                if (quantity <= 0 || importPrice < 0) {
                    session.setAttribute("errorMessage", "Số lượng và giá nhập của sản phẩm không hợp lệ.");
                    response.sendRedirect(request.getContextPath() + "/view/admin/purchase-orders.jsp");
                    return;
                }

                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setProductId(Integer.parseInt(productIds[i]));
                detail.setQuantity(quantity);
                detail.setImportPrice(importPrice);
                items.add(detail);
            }

            try (Connection conn = DBContext.getConnection()) {
                PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO(conn);
                int orderId = purchaseOrderDAO.createPurchaseOrder(supplierId, note, items);
                session.setAttribute("message", "Tạo đơn đặt hàng #" + orderId + " thành công, đang chờ nhà cung cấp xác nhận.");
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", "Lỗi DB khi lưu đơn hàng: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Dữ liệu số lượng hoặc giá sản phẩm gửi lên không đúng định dạng số.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/purchase-orders");    }
}