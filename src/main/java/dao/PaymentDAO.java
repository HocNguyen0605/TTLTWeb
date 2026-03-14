package dao;

import model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection conn;
    public PaymentDAO() {
        try {
            this.conn = new util.DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. Thêm bản ghi thanh toán mới
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payments (id_order, id_method, amount, date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getIdOrder());
            ps.setInt(2, payment.getIdMethod());
            ps.setDouble(3, payment.getAmount());
            ps.setTimestamp(4, new java.sql.Timestamp(payment.getDate().getTime()));
            ps.setString(5, payment.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Lấy danh sách lịch sử thanh toán
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setIdOrder(rs.getInt("id_order"));
                p.setIdMethod(rs.getInt("id_method"));
                p.setAmount(rs.getDouble("amount"));
                p.setDate(rs.getTimestamp("date"));
                p.setStatus(rs.getString("status"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}