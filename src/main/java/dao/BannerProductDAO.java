package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BannerProductDAO {
    private Connection conn;

    public BannerProductDAO() {
    }
    public BannerProductDAO(Connection conn) {
        this.conn = conn;
    }
    //Thêm chi danh sách id sản phẩm của banner
    public void insertBannerProduct(int bannerId, int productId, int comboId) throws SQLException {
        String sql = "INSERT INTO banner_products (id_banner, id_product, id_promotion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bannerId);
            ps.setInt(2, productId);
            ps.setInt(3, comboId);
            ps.executeUpdate();

        }
    }
    public List<Integer> getProductLinks(int bannerId) throws SQLException {
        String sql = "SELECT * FROM banner_products WHERE id_banner=?";
        List<Integer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bannerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("id_product"));
            }
        }
        return list;
    }
    public String getPromotionName(int bannerId) throws SQLException {
        String sql = """
                SELECT p.name 
                FROM banner_products bp
                JOIN promotion p ON bp.id_promotion = p.id
                WHERE bp.id_banner = ?
                LIMIT 1
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bannerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }

        return null;
    }
    public void deleteBannerProducts(int bannerId) throws SQLException {
        String sql = "DELETE FROM banner_products WHERE id_banner = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bannerId);
            ps.executeUpdate();
        }
    }
}
