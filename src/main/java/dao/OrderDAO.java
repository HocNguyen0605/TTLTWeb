package dao;

import model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy danh sách đơn hàng
    // Lấy danh sách đơn hàng
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        // Updated SQL to match actual DB schema: orders(id, ...) and
        // shippinginfo(id_order, receiver_name)
        String sql = """
                SELECT
                    o.id,
                    o.total AS total_price,
                    o.status_order AS status,
                    o.date AS order_date,
                    o.id_user,
                    COALESCE(s.receiver_name, u.name) AS customer_name
                FROM orders o
                LEFT JOIN shippinginfo s ON o.id = s.id_order
                LEFT JOIN user u ON o.id_user = u.id
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
                o.setCustomerName(rs.getString("customer_name"));
                o.setUserId(rs.getInt("id_user"));

                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm đơn hàng
    public void insertOrder(Order order) {
        String sql = "INSERT INTO orders( total_price, status_order, order_date) VALUES(?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
            ps.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addOrder(Order order) {
        String sql = "INSERT INTO orders(total_price, status_order, order_date) VALUES (?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
            ps.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String sql = "INSERT INTO orders(total, status_order, date, id_user) VALUES (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setDouble(1, order.getTotalPrice());
        ps.setString(2, order.getStatus());
        ps.setTimestamp(3, new java.sql.Timestamp(order.getOrderDate().getTime()));
        ps.setInt(4, order.getUserId());

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
}
