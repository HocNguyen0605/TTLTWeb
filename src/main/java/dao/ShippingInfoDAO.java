package dao;

import model.ShippingInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShippingInfoDAO {
    private Connection conn;

    public ShippingInfoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(ShippingInfo info) {
        String sql = "INSERT INTO shippinginfo(id_order, receiver_name, receiver_phone, address, shipping_fee, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, info.getOrderId());
            ps.setString(2, info.getReceiverName());
            ps.setString(3, info.getReceiverPhone());
            ps.setString(4, info.getAddress());
            ps.setDouble(5, info.getShippingFee());
            ps.setString(6, info.getNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
