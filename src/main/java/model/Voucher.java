package model;

import java.sql.Timestamp;

public class Voucher {
    private int id;
    private String code;
    private int promotionId;
    private Timestamp startDate;
    private Timestamp endDate;
    private double discountValue;
    private String discountType;
    public Voucher() {}

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }
    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
}