package dao;
import model.Cart;
import model.CartItem;
import model.Product;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CartDAO {
    private Connection conn;
    public CartDAO(Connection conn) {
        this.conn = conn;
    }

    //Thêm hoặc cập nhật(trùng sp) sẩn phẩm vào giỏ
    public void addOrUpdateCartItem(int userId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        }
    }
    //xóa sp khỏi giổ
    public void removeCartItem(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }
    //Xóa toàn bộ sp trong giỏ user
    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
    public void syncCartToSession(int userId, Cart sessionCart) throws SQLException {
        ProductDAO productDAO = new ProductDAO(conn);
        // 1. Lấy sp từ db
        String sql = "SELECT product_id, quantity FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        // Thêm vào giỏ hàng của session
                        if (sessionCart.findItemByProductId(productId) == null) {
                            sessionCart.addProduct(product, quantity);
                        } else {
                            int quantityCart = sessionCart.findItemByProductId(productId).getQuantity();
                            sessionCart.update(productId,quantityCart + quantity);
                        }
                    }
                }
            }
        }

        // 2. Thêm các sp chỉ có trong session vào db
        if (sessionCart.getAllItems() != null) {
            for (CartItem item : sessionCart.getAllItems()) {
                addOrUpdateCartItem(userId, item.getProduct().getId(), item.getQuantity());
            }
        }
    }
}

