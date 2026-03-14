package service;

import dao.ProductDAO;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final ProductDAO pdao = new ProductDAO();
    private final ProductDAO productDAO = new ProductDAO();

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

    // Giữ nguyên logic chi tiết sản phẩm
    public Product getProductDetail(int id) {
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
        // 0: Default In-Stock status (or should we restore actual quantity?
        // For now resetting to 0 (contact for stock) or just >0 is safer than random
        // number.
        // Let's set to 0. Admin can update real quantity later.
        pdao.updateStatus(id, 0);
    }

    public void updateQuantity(int id, int quantity) {
        pdao.updateQuantity(id, quantity);
    }

    //
    // // Thêm sản phẩm (CHUYỂN QUA DAO)
    // public void add(Product p) {
    // pdao.insert(p);
    // }
    //
    // // Xóa sản phẩm (CHUYỂN QUA DAO)
    // public void delete(int id) {
    // pdao.delete(id);
    // }
    private void handleImage(Product p) {
        if (p.getImg() == null || p.getImg().isEmpty() || p.getImg().equals("linkanh")) {
            p.setImg("images/default-product.png");
        }
        // }
        // public List<Product> getTop3BestSeller() {
        // return productDAO.getTop3BestSeller();
        // }
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
