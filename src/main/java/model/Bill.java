package model;

import java.util.Date;

public class Bill {
    private int id;
    private int idOrder; // ID của đơn hàng vừa tạo
    private Date date;
    private double discount;
    private double total;
    private double finalTotal;

    public Bill() {}

    public Bill(int id, int idOrder, Date date, double discount, double total, double finalTotal) {
        this.id = id;
        this.idOrder = idOrder;
        this.date = date;
        this.discount = discount;
        this.total = total;
        this.finalTotal = finalTotal;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getFinalTotal() { return finalTotal; }
    public void setFinalTotal(double finalTotal) { this.finalTotal = finalTotal; }
}