package dao;
import model.PromotionComboItem;

import java.sql.*;

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
}
