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

        if (priceRange != null) {
            switch (priceRange) {
                case "0-50":
                    maxPrice = 50000.0;
                    break;
                case "50-100":
                    minPrice = 50000.0;
                    maxPrice = 100000.0;
                    break;
                case "100-150":
                    minPrice = 100000.0;
                    maxPrice = 150000.0;
                    break;
                case "150-200":
                    minPrice = 150000.0;
                    maxPrice = 200000.0;
                    break;
                case "200-300":
                    minPrice = 200000.0;
                    maxPrice = 300000.0;
                    break;
                case "300-500":
                    minPrice = 300000.0;
                    maxPrice = 500000.0;
                    break;
                case "above500":
                    minPrice = 500000.0;
                    break;
                default:
                    break;
            }
        }

        Integer minVol = null;
        Integer maxVol = null;

        if (volumeStr != null) {
            switch (volumeStr) {
                case "100-250":
                    minVol = 100;
                    maxVol = 250;
                    break;
                case "250-500":
                    minVol = 250;
                    maxVol = 500;
                    break;
                case "500-1000":
                    minVol = 500;
                    maxVol = 1000;
                    break;
                case "above1000":
                    minVol = 1000;
                    break;
            }
        }

        // 2. Gọi DAO để lấy danh sách đã lọc (có phân trang)
        // Note: We need to update DAO methods to accept minVol/maxVol instead of single
        // volume string
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

        // Giữ lại giá trị filter để điền lại vào form
        // Giữ lại giá trị filter để điền lại vào form
        request.setAttribute("currentPriceRange", priceRange);
        request.setAttribute("currentVolume", volumeStr);
        request.setAttribute("currentSupplier", supplier);
        request.setAttribute("currentSort", sortBy);

        // Chuyển hướng
        request.getRequestDispatcher("/view/user/products.jsp").forward(request, response);
    }
}
// @Override
// protected void doGet(HttpServletRequest request, HttpServletResponse
// response) throws ServletException, IOException {
// // Khởi tạo DAO
// ProductDAO dao = new ProductDAO();
//
// // Gọi đúng tên hàm trong DAO
// List<Product> list = dao.getListProduct();
//
// // Đẩy dữ liệu sang JSP
// request.setAttribute("productList", list);
//
// // Chuyển hướng sang file JSP (đảm bảo file này nằm trong thư mục webapp)
// request.getRequestDispatcher("product-list.jsp").forward(request, response);
// }
// }