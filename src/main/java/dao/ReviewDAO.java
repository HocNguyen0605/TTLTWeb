package dao;

import model.Review;
import org.jdbi.v3.core.Jdbi;
import util.DBContext;

import java.util.List;

public class ReviewDAO extends BaseDao {

    public List<Review> getByProductId(int productId) {
        String sql = """
                SELECT r.id, r.product_id as productId, r.user_id as userId, r.rating, r.content, r.created_at as createdAt, 
                       COALESCE(u.name, 'Người dùng Juicy') as userName
                FROM reviews r
                LEFT JOIN user u ON r.user_id = u.id_account
                WHERE r.product_id = :productId
                ORDER BY r.created_at DESC
                """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("productId", productId)
                .mapToBean(Review.class)
                .list());
    }

    public void insert(Review review) {
        String sql = """
                INSERT INTO reviews (product_id, user_id, rating, content)
                VALUES (:productId, :userId, :rating, :content)
                """;
        jdbi.useHandle(handle -> handle.createUpdate(sql)
                .bindBean(review)
                .execute());
    }
}
