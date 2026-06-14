package model;

public class TopProductDTO {
    private String name;
    private int sold;
    private double revenue;
    private String img;

    public TopProductDTO(String name, int sold, double revenue, String img) {
        this.name = name;
        this.sold = sold;
        this.revenue = revenue;
        this.img = img;
    }

    public TopProductDTO(String name, int sold, String img) {
        this.name = name;
        this.sold = sold;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
