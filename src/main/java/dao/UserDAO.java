package dao;

import model.GooglePojo;
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
                        rs.getString("phone"),
                        rs.getString("address"),
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0,
                        null,
                        "LOCAL"))
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

    public void saveUserToken(int userId, String token) {
        String query = "INSERT INTO user_tokens (user_id, token_value, expiry_date) " +
                "VALUES (:uid, :token, DATE_ADD(NOW(), INTERVAL 7 DAY))";

        DBContext.getJdbi().useHandle(handle ->
                handle.createUpdate(query)
                        .bind("uid", userId)
                        .bind("token", token)
                        .execute()
        );
    }

    public User getUserByToken(String token) {
        String query = "SELECT a.id, a.username, a.password, a.role, u.name, u.email, u.phone, u.address " +
                "FROM account a " +
                "JOIN user u ON a.id = u.id_account " +
                "JOIN User_Tokens ut ON a.id = ut.user_id " +
                "WHERE ut.token_value = :token AND ut.expiry_date > NOW()";

        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("token", token)
                .map((rs, ctx) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0,
                        null,
                        "LOCAL"))
                .findFirst()
                .orElse(null));
    }

    public java.util.List<User> getAllUsers() {
        String query = "SELECT a.id, a.username, a.password, a.role, u.name, u.email, u.phone, u.address " +
                "FROM account a LEFT JOIN user u ON a.id = u.id_account";

        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .map((rs, ctx) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0,
                        null,
                        "LOCAL"))
                .list());
    }

    public boolean updateUserRole(int accountId, String newRole) {
        String query = "UPDATE account SET role = :role WHERE id = :id";
        return DBContext.getJdbi().withHandle(handle -> handle.createUpdate(query)
                .bind("role", newRole)
                .bind("id", accountId)
                .execute() > 0);
    }

    public boolean deleteAccount(int accountId) {
        return DBContext.getJdbi().inTransaction(handle -> {
            try {
                handle.createUpdate("DELETE FROM user WHERE id_account = :id")
                        .bind("id", accountId)
                        .execute();

                int rows = handle.createUpdate("DELETE FROM account WHERE id = :id")
                        .bind("id", accountId)
                        .execute();
                return rows > 0;
            } catch (Exception e) {
                // If it fails (e.g. foreign key orders), just return false
                return false;
            }
        });
    }

    public User getUserByEmail(String email) {
        String query = "SELECT a.id, a.username, a.password, a.role, a.googleId, a.authProvider, " +
                "u.name, u.email, u.phone, u.address " +
                "FROM account a JOIN user u ON a.id = u.id_account " +
                "WHERE u.email = :email";

        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("email", email)
                .map((rs, ctx) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0,
                        rs.getString("googleId"),
                        rs.getString("authProvider")))
                .findFirst()
                .orElse(null));
    }

    public User processGoogleLogin(GooglePojo googlePojo) {
        User user = getUserByEmail(googlePojo.getEmail());

        if (user == null) {
            int userId = createGoogleUser(googlePojo);
            return getUserById(userId);
        } else {
            // 3. Nếu đã có: Cập nhật googleId (nếu chưa có) và trả về user
            if (user.getGoogleId() == null || user.getGoogleId().isEmpty()) {
                updateGoogleId(user.getId(), googlePojo.getId());
                user.setGoogleId(googlePojo.getId());
            }
            return user;
        }
    }

    public int createGoogleUser(GooglePojo googlePojo) {
        return DBContext.getJdbi().inTransaction(handle -> {
            int accountId = handle.createUpdate(
                            "INSERT INTO account (username, password, role, googleId, authProvider) " +
                                    "VALUES (:username, :password, :role, :gid, :provider)")
                    .bind("username", googlePojo.getEmail()) // Dùng email làm username tạm
                    .bind("password", (String) null)         // Không có mật khẩu
                    .bind("role", "user")
                    .bind("gid", googlePojo.getId())
                    .bind("provider", "GOOGLE")
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            handle.createUpdate("INSERT INTO user (id_account, name, email) VALUES (:aid, :name, :email)")
                    .bind("aid", accountId)
                    .bind("name", googlePojo.getName())
                    .bind("email", googlePojo.getEmail())
                    .execute();

            return accountId;
        });
    }

    public User getUserById(int id) {
        String query = "SELECT a.id, a.username, a.password, a.role, a.googleId, a.authProvider, " +
                "u.name, u.email, u.phone, u.address " +
                "FROM account a JOIN user u ON a.id = u.id_account " +
                "WHERE a.id = :id";

        return DBContext.getJdbi().withHandle(handle -> handle.createQuery(query)
                .bind("id", id)
                .map((rs, ctx) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        "admin".equalsIgnoreCase(rs.getString("role")) ? 1 : 0,
                        rs.getString("googleId"),
                        rs.getString("authProvider")))
                .findFirst()
                .orElse(null));
    }

    public void updateGoogleId(int accountId, String googleId) {
        String query = "UPDATE account SET googleId = :gid, authProvider = 'GOOGLE' WHERE id = :id";
        DBContext.getJdbi().useHandle(handle -> handle.createUpdate(query)
                .bind("gid", googleId)
                .bind("id", accountId)
                .execute());
    }
}
