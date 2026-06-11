package dao;

import model.Order;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    public OrderDAO() {

    }

    // Lấy danh sách đơn hàng
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = """
                SELECT
                    o.id,
                    o.total AS total_price,
                    o.status_order AS status,
                    o.date AS order_date,
                    o.delivered_date,
                    o.id_user,
                    o.tracking_code,
                    COALESCE(s.receiver_name, u.name) AS customer_name
                FROM orders o
                LEFT JOIN shippinginfo s ON o.id = s.id_order
                LEFT JOIN `user` u ON o.id_user = u.id
                ORDER BY o.date DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setStatus(rs.getString("status"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setDeliveredDate(rs.getTimestamp("delivered_date"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setUserId(rs.getInt("id_user"));
                o.setTrackingCode(rs.getString("tracking_code"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái đơn hàng
    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status_order=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean cancelOrder(int orderId, int userId, String reason) {
        return DBContext.getJdbi().inTransaction(handle -> {
            boolean updated = handle.createUpdate("UPDATE orders SET status_order='cancelled', cancel_reason=:reason WHERE id=:id AND id_user=:userId")
                    .bind("reason", reason)
                    .bind("id", orderId)
                    .bind("userId", userId)
                    .execute() > 0;
            if (updated) {
                List<java.util.Map<String, Object>> items = handle.createQuery("SELECT id_product, quantity FROM orderitems WHERE id_order = :id")
                        .bind("id", orderId)
                        .mapToMap()
                        .list();
                for (java.util.Map<String, Object> item : items) {
                    handle.createUpdate("UPDATE products SET quantity = quantity + :quantity WHERE id = :pid")
                            .bind("quantity", item.get("quantity"))
                            .bind("pid", item.get("id_product"))
                            .execute();
                }
            }
            return updated;
        });
    }

    public void deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllOrders() {
        String sql = "DELETE FROM orders";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int insertAndReturnId(Order order) throws SQLException {
        String sql = "INSERT INTO orders(total, status_order, date, delivered_date, id_user) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setDouble(1, order.getTotalPrice());
        ps.setString(2, order.getStatus());
        ps.setTimestamp(3, order.getOrderDate());
        if (order.getDeliveredDate() != null) {
            ps.setTimestamp(4, order.getDeliveredDate());
        } else {
            ps.setNull(4, java.sql.Types.TIMESTAMP);
        }
        if (order.getUserId() > 0) {
            ps.setInt(5, order.getUserId());
        } else {
            ps.setNull(5, java.sql.Types.INTEGER);
        }
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public model.ShippingInfo getShippingInfoByOrderId(int orderId) {
        String sql = "SELECT * FROM shippinginfo WHERE id_order = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    model.ShippingInfo info = new model.ShippingInfo();
                    info.setId(rs.getInt("id"));
                    info.setOrderId(rs.getInt("id_order"));
                    info.setReceiverName(rs.getString("receiver_name"));
                    info.setReceiverPhone(rs.getString("receiver_phone"));
                    info.setAddress(rs.getString("address"));
                    info.setShippingFee(rs.getDouble("shipping_fee"));
                    info.setNote(rs.getString("note"));
                    return info;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getOrdersByUserId(int userId) {
        return DBContext.getJdbi().withHandle(handle -> {
            List<Order> orders = handle.createQuery(
                            "SELECT o.id, " +
                                    "o.status_order AS status, " +
                                    "o.total AS totalPrice, " +
                                    "o.date AS orderDate, " +
                                    "o.delivered_date AS deliveredDate " +
                                    "FROM orders o " +
                                    "WHERE o.id_user = :userId " +
                                    "ORDER BY o.date DESC")
                    .bind("userId", userId)
                    .mapToBean(Order.class)
                    .list();

            for (Order order : orders) {
                List<model.OrderItemDetail> items = handle.createQuery(
                                "SELECT oi.id_product AS productId, p.product_name AS productName, " +
                                        "COALESCE(pi.image_URL, p.image) AS productImg, " + "p.volume, oi.quantity, oi.price_at_time AS priceAtTime " +
                                        "FROM orderitems oi " +
                                        "JOIN products p ON oi.id_product = p.id " +
                                        "LEFT JOIN product_images pi ON p.image = pi.id " +
                                        "WHERE oi.id_order = :orderId")
                        .bind("orderId", order.getId())
                        .mapToBean(model.OrderItemDetail.class)
                        .list();
                order.setItems(items);

                if (items != null && !items.isEmpty()) {
                    order.setItemName(items.get(0).getProductName());
                }
            }
            return orders;
        });
    }

    //Lấy thông tin đơn hàng theo id
    public model.Order getOrderById(int orderId) {
        String sql = "SELECT id, id_user, tracking_code, expected_delivery_date, status_order AS status, total AS totalPrice, delivered_date AS deliveredDate FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    model.Order o = new model.Order();
                    o.setId(rs.getInt("id"));
                    o.setUserId(rs.getInt("id_user"));
                    o.setTrackingCode(rs.getString("tracking_code"));
                    o.setExpectedDeliveryDate(rs.getTimestamp("expected_delivery_date"));
                    o.setStatus(rs.getString("status"));
                    o.setTotalPrice(rs.getDouble("totalPrice"));
                    o.setDeliveredDate(rs.getTimestamp("deliveredDate"));
                    return o;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Trả về tổng tiền của đơn
    public double getOrderTotalById(int orderId) {
        String sql = "SELECT total FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Trả về userid của đơn
    public int getUserIdByOrderId(int orderId) {
        String sql = "SELECT id_user FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_user");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // not found
    }
    //Cập nhật mã vận đơn cho đơn hàng(mã lâý từ GHN)
    public boolean updateTrackingCodeAndExpectedDate(int orderId, String trackingCode, Timestamp expectedDate) {
        String sql = "UPDATE orders SET tracking_code = ?, expected_delivery_date = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackingCode);
            if (expectedDate != null) {
                ps.setTimestamp(2, expectedDate);
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
