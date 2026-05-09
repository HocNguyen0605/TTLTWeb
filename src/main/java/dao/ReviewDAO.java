package dao;

import model.Review;
import model.Review.ReviewComment;
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
        return jdbi.withHandle(handle -> {
            List<Review> reviews = handle.createQuery(sql)
                    .bind("productId", productId)
                    .bind("currentUserId", currentUserId)
                    .mapToBean(Review.class)
                    .list();
            populateComments(reviews);
            return reviews;
        });
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
        return jdbi.withHandle(handle -> {
            List<Review> reviews = handle.createQuery(sql)
                    .mapToBean(Review.class)
                    .list();
            populateComments(reviews);
            return reviews;
        });
    }

    private void populateComments(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return;
        for (Review r : reviews) {
            r.setComments(getCommentsByReviewId(r.getId()));
        }
    }

    public List<ReviewComment> getCommentsByReviewId(int reviewId) {
        String sql = """
                SELECT rc.id, rc.review_id as reviewId, rc.user_id as userId, rc.content, rc.created_at as createdAt,
                       COALESCE(u.name, a.username) as userName
                FROM review_comments rc
                JOIN account a ON rc.user_id = a.id
                LEFT JOIN user u ON a.id = u.id_account
                WHERE rc.review_id = :reviewId
                ORDER BY rc.created_at ASC
                """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("reviewId", reviewId)
                .mapToBean(ReviewComment.class)
                .list());
    }

    public void insertComment(ReviewComment comment) {
        String sql = "INSERT INTO review_comments (review_id, user_id, content) VALUES (:reviewId, :userId, :content)";
        jdbi.useHandle(handle -> handle.createUpdate(sql)
                .bindBean(comment)
                .execute());
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
        return jdbi.withHandle(handle -> {
            List<Review> reviews = handle.createQuery(sql)
                    .bind("limit", limit)
                    .mapToBean(Review.class)
                    .list();
            populateComments(reviews);
            return reviews;
        });
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
                handle.createUpdate("DELETE FROM review_likes WHERE review_id = :reviewId AND user_id = :userId")
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .execute();
                return false;
            } else {
                handle.createUpdate("INSERT INTO review_likes (review_id, user_id) VALUES (:reviewId, :userId)")
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .execute();
                return true;
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
