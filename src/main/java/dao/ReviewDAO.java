package dao;

import model.Review;
import org.jdbi.v3.core.Jdbi;
import util.DBContext;

import java.util.List;

public class ReviewDAO extends BaseDao {

    public List<Review> getByProductId(int productId, int currentUserId) {
        String sql = """
                SELECT r.id, r.product_id as productId, r.user_id as userId, r.rating, r.content, r.created_at as createdAt, 
                       r.seller_reply as sellerReply,
                       COALESCE(u.name, 'Người dùng Juicy') as userName,
                       (SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id) as likes,
                       EXISTS(SELECT 1 FROM review_likes rl WHERE rl.review_id = r.id AND rl.user_id = :currentUserId) as hasLiked
                FROM reviews r
                LEFT JOIN user u ON r.user_id = u.id_account
                WHERE r.product_id = :productId
                ORDER BY r.created_at DESC
                """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("productId", productId)
                .bind("currentUserId", currentUserId)
                .mapToBean(Review.class)
                .list());
    }

    public List<Review> getByProductId(int productId) {
        return getByProductId(productId, -1);
    }

    public List<Review> getAllReviews() {
        String sql = """
                SELECT r.id, r.product_id as productId, r.user_id as userId, r.rating, r.content, r.created_at as createdAt, 
                       r.seller_reply as sellerReply,
                       COALESCE(u.name, 'Người dùng Juicy') as userName,
                       p.product_name as productName,
                       (SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id) as likes
                FROM reviews r
                LEFT JOIN user u ON r.user_id = u.id_account
                LEFT JOIN products p ON r.product_id = p.id
                ORDER BY r.created_at DESC
                """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Review.class)
                .list());
    }

    public List<Review> getTopPositiveReviews(int limit) {
        String sql = """
                SELECT r.id, r.product_id as productId, r.user_id as userId, r.rating, r.content, r.created_at as createdAt, 
                       r.seller_reply as sellerReply,
                       COALESCE(u.name, 'Người dùng Juicy') as userName,
                       (SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id) as likes
                FROM reviews r
                LEFT JOIN user u ON r.user_id = u.id_account
                WHERE r.rating >= 4
                ORDER BY r.created_at DESC
                LIMIT :limit
                """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("limit", limit)
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

    public void delete(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = :id";
        jdbi.useHandle(handle -> handle.createUpdate(sql)
                .bind("id", reviewId)
                .execute());
    }

    public boolean toggleLike(int reviewId, int userId) {
        return jdbi.withHandle(handle -> {
            String checkSql = "SELECT COUNT(*) FROM review_likes WHERE review_id = :reviewId AND user_id = :userId";
            int count = handle.createQuery(checkSql)
                    .bind("reviewId", reviewId)
                    .bind("userId", userId)
                    .mapTo(Integer.class)
                    .one();

            if (count > 0) {
                // Đã like -> Xóa like
                handle.createUpdate("DELETE FROM review_likes WHERE review_id = :reviewId AND user_id = :userId")
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .execute();
                return false; // Trả về false nghĩa là unliked
            } else {
                // Chưa like -> Thêm like
                handle.createUpdate("INSERT INTO review_likes (review_id, user_id) VALUES (:reviewId, :userId)")
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .execute();
                return true; // Trả về true nghĩa là liked
            }
        });
    }

    public void updateSellerReply(int reviewId, String reply) {
        String sql = "UPDATE reviews SET seller_reply = :reply WHERE id = :reviewId";
        jdbi.useHandle(handle -> handle.createUpdate(sql)
                .bind("reply", reply)
                .bind("reviewId", reviewId)
                .execute());
    }
}
