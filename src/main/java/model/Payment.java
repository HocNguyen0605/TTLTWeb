package model;

import java.util.Date;

public class Payment {
    private int id;
    private int idOrder;
    private int idMethod; // 1: COD, 2: Banking, 3: MoMo...
    private double amount;
    private Date date;
    private String status; // PENDING, COMPLETED

    public Payment() {}

    public Payment(int id, int idOrder, int idMethod, double amount, Date date, String status) {
        this.id = id;
        this.idOrder = idOrder;
        this.idMethod = idMethod;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public int getIdMethod() { return idMethod; }
    public void setIdMethod(int idMethod) { this.idMethod = idMethod; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}