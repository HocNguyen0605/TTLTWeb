package dao;

import model.Contact;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class  ContactDAO {

    // Hàm gốc: Chèn dữ liệu bằng các tham số rời
    public void insert(String name, String email, String phone,
                       String subject, String message, Integer idUser)
            throws Exception {

        String sql = """
            INSERT INTO Contact
            (full_name, email, phone, subject, message, id_user)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, subject);
            ps.setString(5, message);

            if (idUser == null)
                ps.setNull(6, java.sql.Types.INTEGER);
            else
                ps.setInt(6, idUser);

            ps.executeUpdate();
        }
    }

    // Hàm mới: Chèn dữ liệu trực tiếp từ Object Contact (Sẽ dùng hàm này ở Controller)
    public void insert(Contact c) throws Exception {
        insert(c.getFullName(), c.getEmail(), c.getPhone(),
                c.getSubject(), c.getMessage(), c.getIdUser());
    }

    public java.util.List<Contact> getAll() {
        java.util.List<Contact> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Contact ORDER BY created_at DESC";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Contact c = new Contact();
                c.setIdContact(rs.getInt("id_contact"));
                c.setIdUser(rs.getObject("id_user") != null ? rs.getInt("id_user") : null);
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setSubject(rs.getString("subject"));
                c.setMessage(rs.getString("message"));
                c.setStatus(rs.getString("status"));
                java.sql.Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    c.setCreatedAt(ts.toLocalDateTime());
                }
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Contact findById(int id) {
        String sql = "SELECT * FROM Contact WHERE id_contact = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Contact c = new Contact();
                    c.setIdContact(rs.getInt("id_contact"));
                    c.setIdUser(rs.getObject("id_user") != null ? rs.getInt("id_user") : null);
                    c.setFullName(rs.getString("full_name"));
                    c.setEmail(rs.getString("email"));
                    c.setPhone(rs.getString("phone"));
                    c.setSubject(rs.getString("subject"));
                    c.setMessage(rs.getString("message"));
                    c.setStatus(rs.getString("status"));
                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        c.setCreatedAt(ts.toLocalDateTime());
                    }
                    return c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE Contact SET status = ? WHERE id_contact = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}