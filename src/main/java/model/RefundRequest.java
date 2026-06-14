package model;

import java.sql.Timestamp;

public class RefundRequest {
    private int id;
    private int orderId;
    private int userId;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private double amount;
    private Timestamp createdAt;
    private Timestamp reviewedAt;
    private Integer reviewedBy; // admin id

    public RefundRequest() {}

    public RefundRequest(int orderId, int userId, String reason, double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
        this.amount = amount;
        this.status = "PENDING";
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Timestamp reviewedAt) { this.reviewedAt = reviewedAt; }
    public Integer getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Integer reviewedBy) { this.reviewedBy = reviewedBy; }
}