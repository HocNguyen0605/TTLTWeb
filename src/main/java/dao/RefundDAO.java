package dao;

import model.RefundRequest;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RefundDAO {
    private Connection conn;

    public RefundDAO(Connection conn) {
        this.conn = conn;
    }

    //Tạo yêu cầu hoàn tiền
    public void createRefund(RefundRequest r) throws SQLException {
        String sql = "INSERT INTO refund_requests (order_id, user_id, reason, status, amount, created_at) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getOrderId());
            ps.setInt(2, r.getUserId());
            ps.setString(3, r.getReason());
            ps.setString(4, r.getStatus());
            ps.setDouble(5, r.getAmount());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setId(rs.getInt(1));
                }
            }
        }
    }

    //Get yêu cầu refund
    public RefundRequest getById(int id) throws SQLException {
        String sql = "SELECT * FROM refund_requests WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RefundRequest r = mapRow(rs);
                    return r;
                }
            }
        }
        return null;
    }

    //Get yêu cầu đang chờ
    public List<RefundRequest> getPendingRefunds() throws SQLException {
        List<RefundRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM refund_requests WHERE status = 'PENDING'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    //Lấy yêu cầu hoàn tiền mới nhất theo order_id
    public RefundRequest getLatestRefundByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM refund_requests WHERE order_id = ? ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    //Cập nhập status
    public void updateStatus(int id, String status, int adminId) throws SQLException {
        String sql = "UPDATE refund_requests SET status = ?, reviewed_at = CURRENT_TIMESTAMP, reviewed_by = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, adminId);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    private RefundRequest mapRow(ResultSet rs) throws SQLException {
        RefundRequest r = new RefundRequest();
        r.setId(rs.getInt("id"));
        r.setOrderId(rs.getInt("order_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setReason(rs.getString("reason"));
        r.setStatus(rs.getString("status"));
        r.setAmount(rs.getDouble("amount"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setReviewedAt(rs.getTimestamp("reviewed_at"));
        int reviewedBy = rs.getInt("reviewed_by");
        if (!rs.wasNull()) r.setReviewedBy(reviewedBy);
        return r;
    }
}
