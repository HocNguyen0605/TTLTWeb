package dao;

import model.PurchaseOrder;
import model.PurchaseOrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {
    private Connection conn;

    public PurchaseOrderDAO() {
    }

    public PurchaseOrderDAO(Connection conn) {
        this.conn = conn;
    }

     //Lấy danh sách đơn đặt hàng đang chờ nhà cung cấp xác nhận
    public List<PurchaseOrder> getPendingOrders() throws SQLException {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = """
                SELECT po.id, s.name AS supplier_name, po.created_date, 
                       po.total_amount, po.status, 
                       (SELECT COUNT(*) FROM purchase_order_detail d WHERE d.order_id = po.id) AS item_count 
                FROM purchase_order po 
                JOIN suppliers s ON po.supplier_id = s.id 
                WHERE po.status = 'PENDING_SUPPLIER_CONFIRM' 
                ORDER BY po.created_date DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrder o = new PurchaseOrder();
                    o.setOrderId(rs.getInt("id"));
                    o.setSupplierName(rs.getString("supplier_name"));
                    o.setCreatedDate(rs.getTimestamp("created_date"));
                    o.setItemCount(rs.getInt("item_count"));
                    o.setTotalAmount(rs.getDouble("total_amount"));
                    o.setStatus(rs.getString("status"));
                    list.add(o);
                }
            }
        }
        return list;
    }

    //Tạo đơn hàng trả về id đơn
    public int createPurchaseOrder(int supplierId, String note, List<PurchaseOrderDetail> items) throws SQLException {
        String sqlOrder = """
                INSERT INTO purchase_order (supplier_id, created_date, status, note, total_amount) 
                VALUES (?, NOW(), 'PENDING_SUPPLIER_CONFIRM', ?, ?)
                """;

        String sqlDetail = """
                INSERT INTO purchase_order_detail (order_id, product_id, quantity, import_price) 
                VALUES (?, ?, ?, ?)
                """;

        double total = 0;
        for (PurchaseOrderDetail d : items) {
            total += d.getQuantity() * d.getImportPrice();
        }

        // Lưu lại trạng thái auto-commit cũ trước khi can thiệp transaction
        boolean oldAutoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            int orderId;
            // Chèn dữ liệu vào bảng purchase_order và lấy khóa tự tăng sinh ra
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, supplierId);
                ps.setString(2, note);
                ps.setDouble(3, total);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Không lấy được ID đơn hàng vừa tạo.");
                    }
                }
            }

            // Chèn dữ liệu hàng loạt vào bảng purchase_order_detail
            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                for (PurchaseOrderDetail d : items) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, d.getProductId());
                    ps.setInt(3, d.getQuantity());
                    ps.setDouble(4, d.getImportPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }
    //tìm order pending bằng id
    public List<PurchaseOrder> searchOrdersWithPendingStatus(String keyword) throws SQLException {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM purchase_order WHERE id LIKE ? AND status = 'PENDING_SUPPLIER_CONFIRM'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder();
                    order.setOrderId(rs.getInt("id"));
                    order.setSupplierId(rs.getInt("supplier_id"));
                    order.setCreatedDate(rs.getTimestamp("created_date"));
                    order.setStatus(rs.getString("status"));
                    order.setNote(rs.getString("note"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                }
            }
        }
        return list;
    }
    public PurchaseOrder getOrderById(int orderId) {
        String sql = "SELECT id, supplier_id, created_date, status, note, total_amount " +
                "FROM purchase_order WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder();
                    order.setOrderId(rs.getInt("id"));
                    order.setSupplierId(rs.getInt("supplier_id"));
                    order.setCreatedDate(rs.getTimestamp("created_date"));
                    order.setStatus(rs.getString("status"));
                    order.setNote(rs.getString("note"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    return order;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<PurchaseOrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<PurchaseOrderDetail> list = new ArrayList<>();

        String sql = "SELECT d.id, d.order_id, d.product_id, d.quantity, d.import_price, " +
                "       p.product_name, pi.image_URL " +
                "FROM purchase_order_detail d " +
                "INNER JOIN products p ON d.product_id = p.id " +
                "INNER JOIN product_images pi ON p.id = pi.id_product " +
                "WHERE d.order_id = ? AND pi.img_default = 'default'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setOrderId(rs.getInt("order_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setImportPrice(rs.getDouble("import_price"));

                    detail.setProductName(rs.getString("product_name"));
                    detail.setProductImg(rs.getString("image_URL"));

                    list.add(detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}