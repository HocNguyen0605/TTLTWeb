package util;

import org.jdbi.v3.core.Jdbi; // Thêm import này
import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    private static final String URL = "jdbc:mysql://localhost:3306/juicy_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Thêm biến static để dùng chung một đối tượng Jdbi duy nhất (Singleton)
    private static Jdbi jdbi;

    public static Jdbi getJdbi() {
        if (jdbi == null) {
            try {
                // Ép buộc nạp Driver MySQL trước khi Jdbi khởi tạo
                Class.forName("com.mysql.cj.jdbc.Driver");
                jdbi = Jdbi.create(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Không tìm thấy Driver MySQL!");
            }
        }
        return jdbi;
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}