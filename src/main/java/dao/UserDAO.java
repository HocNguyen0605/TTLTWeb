package dao;

import java.util.ArrayList;
import java.util.List;
import model.User;
import util.DBContext;


public class UserDAO {
    // Check login: Join account and user tables
    public User login(String emailOrUsername, String password) {
        // 1. Cập nhật Query: Lấy thêm u.phone và u.address
        String query = "SELECT a.id, a.username, a.password, a.role, u.name, u.email, u.phone, u.address " +
                "FROM account a LEFT JOIN user u ON a.id = u.id_account " +
                "WHERE (a.username = :user OR u.email = :user) AND a.password = :pass";

        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("user", emailOrUsername)
                .bind("pass", password)
                .map((rs, ctx) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),    // Thêm phone vào đây
                        rs.getString("address"),  // Thêm address vào đây
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0))
                .findFirst()
                .orElse(null));
    }

    // Check if email exists in user table
    public boolean isUserEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = :email";
        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("email", email)
                .mapTo(Integer.class)
                .one() > 0);
    }

    // Check if username exists in account table
    public boolean isUserNameExists(String username) {
        String query = "SELECT COUNT(*) FROM account WHERE username = :username";
        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("username", username)
                .mapTo(Integer.class)
                .one() > 0);
    }

    // Register: Transaction to insert into account then user
    public void register(User user) {
        DBContext.getJdbi().useTransaction(handle -> {
            // 1. Insert into account
            int accountId = handle
                    .createUpdate("INSERT INTO account (username, password, role) VALUES (:username, :password, :role)")
                    .bind("username", user.getUsername())
                    .bind("password", user.getPassword())
                    .bind("role", user.getRole() == 1 ? "admin" : "user")
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            // 2. Insert into user
            handle.createUpdate("INSERT INTO user (id_account, name, email) VALUES (:aid, :name, :email)")
                    .bind("aid", accountId)
                    .bind("name", user.getUsername())
                    .bind("email", user.getEmail())
                    .execute();
        });
    }

    // Update password
    public boolean updatePassword(String email, String newPassword) {
        return DBContext.getJdbi().inTransaction(handle -> {
            // Find account id from email
            Integer accountId = handle.createQuery("SELECT id_account FROM user WHERE email = :email")
                    .bind("email", email)
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(null);

            if (accountId == null)
                return false;

            // Update password in account table
            int rows = handle.createUpdate("UPDATE account SET password = :pass WHERE id = :id")
                    .bind("pass", newPassword)
                    .bind("id", accountId)
                    .execute();

            return rows > 0;
        });
    }

    public boolean updateProfile(User user) {
        return DBContext.getJdbi().inTransaction(handle -> {
            // 1. Cập nhật bảng account (đổi username)
            handle.createUpdate("UPDATE account SET username = :user WHERE id = :id")
                    .bind("user", user.getUsername())
                    .bind("id", user.getId())
                    .execute();

            // 2. Cập nhật bảng user (name, email, phone, address)
            String queryUser = "UPDATE user SET name = :name, email = :email, " +
                    "phone = :phone, address = :address " +
                    "WHERE id_account = :id";

            int rows = handle.createUpdate(queryUser)
                    .bind("name", user.getFullName())
                    .bind("email", user.getEmail())
                    .bind("phone", user.getPhone())
                    .bind("address", user.getAddress())
                    .bind("id", user.getId())
                    .execute();

            return rows > 0;
        });
    }
}
