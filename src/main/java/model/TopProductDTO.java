package model;

public class TopProductDTO {
    private String name;
    private int sold;
    private double revenue;

    public TopProductDTO(String name, int sold, double revenue) {
        this.name = name;
        this.sold = sold;
        this.revenue = revenue;
    }

    public TopProductDTO(String name, int sold) {
        this.name = name;
        this.sold = sold;
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
