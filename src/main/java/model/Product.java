package model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private int volume;
    private String supplier_name;
    private int quantity;
    private String img;
    private String description;

    public Product() {} // Cần thiết cho Jdbi

    // --- BỔ SUNG ĐẦY ĐỦ SETTER ---
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setVolume(int volume) { this.volume = volume; }
    public void setSupplier_name(String supplier_name) { this.supplier_name = supplier_name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImg(String img) { this.img = img; }
    public void setDescription(String description) { this.description = description; }

    // --- GIỮ NGUYÊN GETTERS ---
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getVolume() { return volume; }
    public String getSupplier_name() { return supplier_name; }
    public int getQuantity() { return quantity; }
    public String getImg() { return img; }
    public String getDescription() { return description; }
}