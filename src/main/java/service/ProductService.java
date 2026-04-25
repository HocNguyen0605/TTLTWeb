package service;

import dao.ProductDAO;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final ProductDAO pdao = new ProductDAO();
    // Lấy danh sách sản phẩm
    public List<Product> getListProduct() {
        List<Product> list = pdao.getAll();
        for (Product p : list) {
            handleImage(p);
        }
        return list;
    }

    // Lấy sản phẩm theo ID (dùng chung)
    public Product getProductById(int id) {
        Product p = pdao.findById(id);
        if (p != null) {
            handleImage(p);
        }
        return p;
    }

    // Tìm kiếm theo tên (Dùng DAO)
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return pdao.getAll();
        }
        List<Product> list = pdao.searchByName(keyword);
        for (Product p : list) {
            handleImage(p);
        }
        return list;
    }

    // Lọc sản phẩm theo NCC + giá (Dùng DAO)
    public List<Product> filterProducts(String supplier, Double maxPrice) {
        List<Product> list = pdao.filterProducts(supplier, maxPrice);
        for (Product p : list) {
            handleImage(p);
        }
        return list;
    }

    // --- ADMIN: Hide/Show ---
    public void hideProduct(int id) {
        // -1: Hidden status
        pdao.updateStatus(id, -1);
    }

    public void showProduct(int id) {
        pdao.updateStatus(id, 0);
    }

    public void updateQuantity(int id, int quantity) {
        pdao.updateQuantity(id, quantity);
    }


    public void updatePrice(int id, double price) {
        pdao.updatePrice(id, price);
    }

    private void handleImage(Product p) {
        if (p.getImg() == null || p.getImg().isEmpty() || p.getImg().equals("linkanh")) {
            p.setImg("images/default-product.png");
        }
    }

    // --- PHÂN TRANG ---
    public int getTotalProducts() {
        return pdao.getTotalProducts();
    }

    public List<Product> getProductsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Product> list = pdao.getProductsByPage(offset, pageSize);
        for (Product p : list) {
            handleImage(p);
        }
        return list;
    }
}
