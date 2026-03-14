////package dao;
////
////import com.mysql.cj.jdbc.MysqlDataSource;
////import model.Product;
////import org.jdbi.v3.core.Jdbi;
////
////import java.sql.SQLException;
////import java.util.List;
////
////public abstract class BaseDao {
////    private Jdbi jdbi;
////
////    public Jdbi get() {
////        if (jdbi == null) makeConnect();
////        return jdbi;
////    }
////
////    private void makeConnect() {
////        MysqlDataSource dataSource = new MysqlDataSource();
////        dataSource.getURL("jdbi:mysql://" + DBproperties.getDbHost() + ":" + DBproperties.getDbPort() + "/"
////        + DBproperties.getDbName());
////        dataSource.setUser(DBproperties.getUsername());
////        dataSource.setPassword(DBproperties.getPassword());
////        try {
////            dataSource.setUseCompression(true);
////            dataSource.setAutoReconnect(true);
////        } catch (SQLException throwables) {
////            throwables.printStackTrace();
////            throw new RuntimeException(throwables);
////        }
////        jdbi = jdbi.create(dataSource);
////    }
////
//////    public static void main(String[] args) {
//////        BaseDao bs = new BaseDao();
//////        Jdbi jdbi = bs.get();
//////        List<Product> products = jdbi.withHandle(h -> {
//////            return h.createQuery("select * from products").mapToBean(Product.class).list();
//////        });
//////        System.out.println(products);
//////    }
////}
//package dao;
//
//import com.mysql.cj.jdbc.MysqlDataSource;
//import org.jdbi.v3.core.Jdbi;
//import util.DBContext;
//
//import java.sql.SQLException;
//
//public class BaseDao { // Bỏ abstract để có thể test hoặc để nguyên nếu chỉ dùng để kế thừa
//    protected Jdbi jdbi;
//
//    public BaseDao() throws Exception {
//        // Thay 'JDBIConnector.get()' bằng class thực tế của bạn
//        this.jdbi = (Jdbi) DBContext.getConnection();
//    }
//    public Jdbi get() {
//        if (jdbi == null) makeConnect();
//        return jdbi;
//    }
//
//    private void makeConnect() {
//        MysqlDataSource dataSource = new MysqlDataSource();
//
//        // 1. Sửa getURL -> setURL
//        // 2. Sửa jdbi:mysql -> jdbc:mysql
//        String url = "jdbc:mysql://" + DBproperties.getDbHost() + ":" + DBproperties.getDbPort() + "/" + DBproperties.getDbName();
//        dataSource.setURL(url);
//
//        dataSource.setUser(DBproperties.getUsername());
//        dataSource.setPassword(DBproperties.getPassword());
//
//        try {
//            dataSource.setUseCompression(true);
//            dataSource.setAutoReconnect(true);
//
//            // Cần thêm cấu hình này để tránh lỗi múi giờ nếu dùng MySQL mới
//            dataSource.setServerTimezone("UTC");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            throw new RuntimeException(throwables);
//        }
//
//        // 3. Sửa jdbi.create -> Jdbi.create (Gọi static method từ Class)
//        jdbi = Jdbi.create(dataSource);
//
//        // 4. Quan trọng: Cần install plugin để Jdbi hiểu cách map Bean (Product.class)
//        // jdbi.installPlugin(new org.jdbi.v3.core.kotlin.KotlinPlugin());  Nếu dùng Kotlin
//        // jdbi.installPlugin(new org.jdbi.v3.sqlobject.SqlObjectPlugin());
//    }
//    public static void main(String[] args) {
//        try {
//            BaseDao dao = new BaseDao();
//            dao.get().withHandle(h -> {
//                System.out.println("Chúc mừng! Kết nối Navicat/Wamp thành công!");
//                return null;
//            });
//        } catch (Exception e) {
//            System.out.println("Lỗi kết nối: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
package dao;

import org.jdbi.v3.core.Jdbi;
import util.DBContext;

public class BaseDao {
    protected Jdbi jdbi;

    public BaseDao() {
        // Gán trực tiếp bằng hàm getJdbi() đã có trong DBContext của bạn
        this.jdbi = DBContext.getJdbi();
    }

    public Jdbi get() {
        if (jdbi == null) {
            this.jdbi = DBContext.getJdbi();
        }
        return jdbi;
    }
}