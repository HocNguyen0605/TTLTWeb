package dao;

import util.DBContext; // Giả sử bạn dùng lớp DBContext để kết nối
import model.Voucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO extends DBContext {

    // Lấy tất cả danh sách voucher
    public List<Voucher> getAllVouchers() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Voucher v = new Voucher();
                v.setId(rs.getInt("id"));
                v.setCode(rs.getString("code"));
                v.setPromotionId(rs.getInt("promotion_id"));
                v.setStartDate(rs.getTimestamp("start_date"));
                v.setEndDate(rs.getTimestamp("end_date"));
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Kiểm tra voucher hợp lệ theo mã code
    // Trong file dao/VoucherDAO.java
    public Voucher getVoucherWithDiscount(String code) {
        // Câu lệnh SQL JOIN hai bảng vouchers và promotion
        String sql = "SELECT v.*, p.discount_value, p.discount_type " +
                "FROM vouchers v " +
                "JOIN promotion p ON v.promotion_id = p.id " +
                "WHERE v.code = ? " +
                "AND NOW() BETWEEN v.start_date AND v.end_date";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setId(rs.getInt("id"));
                    v.setCode(rs.getString("code"));
                    v.setPromotionId(rs.getInt("promotion_id"));

                    // Lấy giá trị giảm giá từ bảng promotion
                    v.setDiscountValue(rs.getDouble("discount_value"));
                    v.setDiscountType(rs.getString("discount_type"));

                    return v;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}