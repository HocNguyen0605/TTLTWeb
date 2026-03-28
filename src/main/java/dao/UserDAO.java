package dao;

import model.User;
import util.DBContext;


public class UserDAO {
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

    public boolean isUserEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = :email";
        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("email", email)
                .mapTo(Integer.class)
                .one() > 0);
    }

    public boolean isUserNameExists(String username) {
        String query = "SELECT COUNT(*) FROM account WHERE username = :username";
        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("username", username)
                .mapTo(Integer.class)
                .one() > 0);
    }

    public void register(User user) {
        DBContext.getJdbi().useTransaction(handle -> {
            int accountId = handle
                    .createUpdate("INSERT INTO account (username, password, role) VALUES (:username, :password, :role)")
                    .bind("username", user.getUsername())
                    .bind("password", user.getPassword())
                    .bind("role", user.getRole() == 1 ? "admin" : "user")
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            handle.createUpdate("INSERT INTO user (id_account, name, email) VALUES (:aid, :name, :email)")
                    .bind("aid", accountId)
                    .bind("name", user.getFullName())
                    .bind("email", user.getEmail())
                    .execute();
        });
    }

    public boolean updatePassword(String email, String newPassword) {
        return DBContext.getJdbi().inTransaction(handle -> {
            Integer accountId = handle.createQuery("SELECT id_account FROM user WHERE email = :email")
                    .bind("email", email)
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(null);

            if (accountId == null)
                return false;

            int rows = handle.createUpdate("UPDATE account SET password = :pass WHERE id = :id")
                    .bind("pass", newPassword)
                    .bind("id", accountId)
                    .execute();

            return rows > 0;
        });
    }

    public boolean updateProfile(User user) {
        return DBContext.getJdbi().inTransaction(handle -> {
            handle.createUpdate("UPDATE account SET username = :user WHERE id = :id")
                    .bind("user", user.getUsername())
                    .bind("id", user.getId())
                    .execute();

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
