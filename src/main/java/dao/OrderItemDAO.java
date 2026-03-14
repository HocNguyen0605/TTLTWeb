package dao;

import model.OrderItem;
import util.DBContext;
import java.sql.*;

public class OrderItemDAO extends DBContext {

    public boolean insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO orderitems (id_order, id_product, quantity, price_at_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPriceAtTime());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}