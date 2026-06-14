package dao;

import model.OrderItem;
import util.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO extends DBContext {
    private Connection conn;

    public OrderItemDAO(Connection conn) {
        this.conn = conn;
    }
//Thêm hàng
    public boolean insertOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO orderitems (id_order, id_product, quantity, price_at_time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPriceAtTime());

            return ps.executeUpdate() > 0;
        }
    }

    //Lấy ra các sp có orderID là x
    public List<OrderItem> getItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM orderitems WHERE id_order = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("id_order"),
                            rs.getInt("id_product"),
                            rs.getInt("quantity"),
                            rs.getDouble("price_at_time")
                    );
                    list.add(item);
                }
            }
        }
        return list;
    }
}