package controller;

import dao.ProductDAO;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();

        // 1. Lấy tham số lọc từ URL
        String priceRange = request.getParameter("priceRange");
        String volumeStr = request.getParameter("volume");
        String supplier = request.getParameter("supplier");
        String sortBy = request.getParameter("sort");

        // --- PHÂN TRANG ---
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int pageSize = 12;
        int offset = (page - 1) * pageSize;

        Double minPrice = null;
        Double maxPrice = null;

        if (priceRange != null && !priceRange.trim().isEmpty()) {
            String[] parts = priceRange.trim().split("-");
            try {
                if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                    minPrice = Double.parseDouble(parts[0].trim().replace(",", ""));
                }
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    maxPrice = Double.parseDouble(parts[1].trim().replace(",", ""));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Integer minVol = null;
        Integer maxVol = null;

        if (volumeStr != null && !volumeStr.trim().isEmpty()) {
            try {
                Integer exactVol = Integer.parseInt(volumeStr.trim().replace(",", ""));
                minVol = exactVol;
                maxVol = exactVol;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 2. Gọi DAO để lấy danh sách đã lọc (có phân trang)

        List<Product> list = dao.getFilteredProducts(minPrice, maxPrice, minVol, maxVol, supplier, sortBy, offset,
                pageSize);
        int totalProducts = dao.getTotalFilteredProducts(minPrice, maxPrice, minVol, maxVol, supplier);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // 3. Lấy danh sách volume và supplier để hiển thị checkbox/select
        List<Integer> volumeList = dao.getAllVolumes();
        List<String> supplierList = dao.getAllSuppliers();

        // 4. Đẩy dữ liệu sang JSP
        request.setAttribute("productList", list);
        request.setAttribute("volumeList", volumeList);
        request.setAttribute("supplierList", supplierList);

        // Phân trang
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("currentPriceRange", priceRange != null ? priceRange : "");
        request.setAttribute("currentVolume", volumeStr);
        request.setAttribute("currentSupplier", supplier);
        request.setAttribute("currentSort", sortBy);

        request.getRequestDispatcher("/view/user/products.jsp").forward(request, response);
    }
}