package dao;
import model.PromotionComboItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionComboItemDAO {
    private Connection conn;

    public PromotionComboItemDAO(Connection conn) {
        this.conn = conn;
    }
    public void insert(PromotionComboItem promotionComboItem) throws SQLException {
        String sql = """
                INSERT INTO promotion_combo_item ( combo_id, product_id, quantity)
                VALUES (?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, promotionComboItem.getComboId());
            ps.setInt(2, promotionComboItem.getProductId());
            ps.setInt(3, promotionComboItem.getQuantity());
            ps.executeUpdate();
        }
    }
    public int countProductNeed(int comboId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM promotion_combo_items WHERE combo_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comboId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // Lấy danh sách tất cả các sản phẩm bắt buộc của 1 combo
    public List<PromotionComboItem> getItemsByComboId(int comboId) throws SQLException {
        List<PromotionComboItem> list = new ArrayList<>();
        String sql = "SELECT * FROM promotion_combo_items WHERE combo_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comboId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PromotionComboItem item = new PromotionComboItem();
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                list.add(item);
            }
        }
        return list;
    }
}

