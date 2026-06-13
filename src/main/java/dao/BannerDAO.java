package dao;

import util.DBContext;
import model.Banner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BannerDAO {
    private Connection conn;

    public BannerDAO() {
    }

    public BannerDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả banner. Sắp xếp theo priority
    public List<Banner> getAllBanners() {
        List<Banner> list = new ArrayList<>();
        String sql = "SELECT * FROM banners ORDER BY priority ASC";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Banner(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("link_url"),
                        rs.getInt("priority"),
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy banner đang hoạt động
    public List<Banner> getActiveBanners() {
        List<Banner> list = new ArrayList<>();
        String sql = "SELECT * FROM banners WHERE is_active = TRUE ORDER BY priority ASC";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Banner(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("link_url"),
                        rs.getInt("priority"),
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm banner mới
    public int insertBanner(Banner b) throws SQLException {
        String sql = "INSERT INTO banners (title, image_url, link_url, priority, is_active) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getImageUrl());
            ps.setString(3, b.getLinkUrl());
            ps.setInt(4, b.getPriority());
            ps.setBoolean(5, b.getIsActive());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
    }

    // Cập nhật trạng thái Ẩn/Hiện
    public boolean updateStatus(int id, boolean status) {
        String sql = "UPDATE banners SET is_active = ? WHERE id = ?";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // update banner
    public boolean updateBanner(Banner b) {
        String sql = "UPDATE banners SET title = ?, image_url = ?, link_url = ?, priority = ?, is_active = ? WHERE id = ?";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getImageUrl());
            ps.setString(3, b.getLinkUrl());
            ps.setInt(4, b.getPriority());
            ps.setBoolean(5, b.getIsActive());
            ps.setInt(6, b.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // lấy ảnh cũ nếu người dùng không upload ảnh mới
    public Banner getBannerById(int id) {
        String sql = "SELECT * FROM banners WHERE id = ?";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Banner(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("image_url"),
                            rs.getString("link_url"),
                            rs.getInt("priority"),
                            rs.getBoolean("is_active"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //Tìm banner bằng tên
    public List<Banner> getBannerByTitle(String title) {
        List<Banner> list = new ArrayList<>();
        String sql = "SELECT * FROM banners WHERE title LIKE ?";
        try ( 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Banner banner = new Banner();
                    banner.setId(rs.getInt("id"));
                    banner.setTitle(rs.getString("title"));
                    banner.setImageUrl(rs.getString("image_url"));
                    banner.setLinkUrl(rs.getString("link_url"));
                    banner.setPriority(rs.getInt("priority"));
                    banner.setIsActive(rs.getBoolean("is_active"));
                    banner.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(banner);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}