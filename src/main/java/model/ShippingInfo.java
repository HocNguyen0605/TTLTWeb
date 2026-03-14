package model;

public class ShippingInfo {
    private int id;
    private int orderId;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private double shippingFee;
    private String note;

    public ShippingInfo() {
    }

    public ShippingInfo(int id, int orderId, String receiverName, String receiverPhone, String address,
                        double shippingFee, String note) {
        this.id = id;
        this.orderId = orderId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.address = address;
        this.shippingFee = shippingFee;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
