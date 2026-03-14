package model;

import java.io.Serializable;
public class CartItem implements Serializable {
    private Product product;
    private double price;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Product product, double price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getImage(Product product) {return product.getImg();}

    public void increaseQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity += quantity;
        } else {
            this.quantity += 1; // Mặc định tăng 1 nếu có lỗi truyền số lượng
        }
    }
    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}