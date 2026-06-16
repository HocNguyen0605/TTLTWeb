package util;

import org.jdbi.v3.core.Jdbi; // Thêm import này
import java.sql.Connection;
import java.sql.DriverManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.Connection;
import java.sql.SQLException;

public class DBContext {
    private static final String HOST = ConfigLoader.getProperty("db.host");
    private static final String PORT = ConfigLoader.getProperty("db.port");
    private static final String DB_NAME = ConfigLoader.getProperty("db.dbName");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    private static final String USER = ConfigLoader.getProperty("db.username");
    private static final String PASSWORD = ConfigLoader.getProperty("db.password");

    private static Jdbi jdbi;
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // Đọc các cấu hình Pool
            config.setMaximumPoolSize(Integer.parseInt(ConfigLoader.getProperty("db.pool.maximumPoolSize") != null ? ConfigLoader.getProperty("db.pool.maximumPoolSize") : "20"));
            config.setMinimumIdle(Integer.parseInt(ConfigLoader.getProperty("db.pool.minimumIdle") != null ? ConfigLoader.getProperty("db.pool.minimumIdle") : "5"));
            config.setConnectionTimeout(Long.parseLong(ConfigLoader.getProperty("db.pool.connectionTimeout") != null ? ConfigLoader.getProperty("db.pool.connectionTimeout") : "30000"));
            config.setIdleTimeout(Long.parseLong(ConfigLoader.getProperty("db.pool.idleTimeout") != null ? ConfigLoader.getProperty("db.pool.idleTimeout") : "600000"));
            config.setMaxLifetime(Long.parseLong(ConfigLoader.getProperty("db.pool.maxLifetime") != null ? ConfigLoader.getProperty("db.pool.maxLifetime") : "1800000"));

            // Tối ưu hóa hiệu năng kết nối cho MySQL
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);

            jdbi = Jdbi.create(dataSource);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi nghiêm trọng khi khởi tạo HikariCP Connection Pool!", e);
        }
    }

    // Trả về Jdbi lấy từ Pool
    public static Jdbi getJdbi() {
        return jdbi;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("HikariDataSource chưa được khởi tạo!");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}