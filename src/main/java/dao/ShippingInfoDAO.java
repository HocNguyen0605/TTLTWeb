package dao;

import model.ShippingInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ShippingInfoDAO {
    private Connection conn;

    public ShippingInfoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(ShippingInfo info) throws SQLException {
        String sql = "INSERT INTO shippinginfo(id_order, receiver_name, receiver_phone, address, shipping_fee, note, province_id, district_id, ward_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, info.getOrderId());
            ps.setString(2, info.getReceiverName());
            ps.setString(3, info.getReceiverPhone());
            ps.setString(4, info.getAddress());
            ps.setDouble(5, info.getShippingFee());
            ps.setString(6, info.getNote() != null ? info.getNote() : "");
            
            if (info.getProvinceId() != null) ps.setInt(7, info.getProvinceId());
            else ps.setNull(7, Types.INTEGER);
            
            if (info.getDistrictId() != null) ps.setInt(8, info.getDistrictId());
            else ps.setNull(8, Types.INTEGER);
            
            ps.setString(9, info.getWardCode());
            ps.executeUpdate();
        }
    }
}
