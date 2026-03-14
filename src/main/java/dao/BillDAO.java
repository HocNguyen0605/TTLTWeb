package dao;

import model.Bill;
import util.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private Connection conn;
    public BillDAO() {
        try {
            this.conn = new util.DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public BillDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertBill(Bill bill) {
        String sql = "INSERT INTO bill (id_order, date, discount, total, final_total) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bill.getIdOrder());
            ps.setTimestamp(2, new java.sql.Timestamp(bill.getDate().getTime()));
            ps.setDouble(3, bill.getDiscount());
            ps.setDouble(4, bill.getTotal());
            ps.setDouble(5, bill.getFinalTotal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Liệt kê danh sách hóa đơn (Dùng cho trang quản lý)
    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill ORDER BY date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bill b = new Bill();
                b.setId(rs.getInt("id"));
                b.setIdOrder(rs.getInt("id_order"));
                b.setDate(rs.getTimestamp("date"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTotal(rs.getDouble("total"));
                b.setFinalTotal(rs.getDouble("final_total"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}