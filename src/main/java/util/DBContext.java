package util;

import org.jdbi.v3.core.Jdbi; // Thêm import này
import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    private static final String HOST = ConfigLoader.getProperty("db.host");
    private static final String PORT = ConfigLoader.getProperty("db.port");
    private static final String DB_NAME = ConfigLoader.getProperty("db.dbName");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME ;

    private static final String USER = ConfigLoader.getProperty("db.username");
    private static final String PASSWORD = ConfigLoader.getProperty("db.password");
    //Tao Ham rỗng
    private static Jdbi jdbi;

    public static Jdbi getJdbi() {
        if (jdbi == null) {
            try {
                // Driver MySQL trước khi Jdbi khởi tạo
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