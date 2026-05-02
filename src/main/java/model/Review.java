package model;
//DÙng để ghi láij nội dung đsanh giá của khsach hàng

import java.sql.Timestamp;

public class Review {
    private int id;
    private int productId;
    private int userId;
    private int rating;
    private String content;
    private Timestamp createdAt;

    private String userName;
    private String productName;
    private String sellerReply;
    private int likes;
    private boolean hasLiked;

    public Review() {}

    public Review(int productId, int userId, int rating, String content) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.content = content;
    }

    public int getId() {
        return id;
    }
    public void setId(int id)    {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerReply() {
        return sellerReply;
    }
    public void setSellerReply(String sellerReply) {
        this.sellerReply = sellerReply;
    }

    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }
    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }
}
