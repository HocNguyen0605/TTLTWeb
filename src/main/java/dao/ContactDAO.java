package dao;

import model.Contact;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ContactDAO {

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
}