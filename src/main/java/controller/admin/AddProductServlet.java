package controller.admin;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/add-product")
@MultipartConfig(
        //danhf cho add file ảnh vào đams mây
        maxFileSize = 50 * 1024 * 1024,      // 50MB
        maxRequestSize = 100 * 1024 * 1024   // 100MB
)
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu form
        String name = request.getParameter("name");
        String supplier = request.getParameter("supplier_name");
        String description = request.getParameter("description");

        // Xử lý đầu vào
        double price = 0;
        int volume = 0;
        int quantity = 0;
        String folderName="products";

        try {
            String priceStr = request.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty())
                price = Double.parseDouble(priceStr);

            String volumeStr = request.getParameter("volume");
            if (volumeStr != null && !volumeStr.isEmpty())
                volume = Integer.parseInt(volumeStr);

            String quantityStr = request.getParameter("quantity");
            if (quantityStr != null && !quantityStr.isEmpty())
                quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_number");
            return;
        }

        java.util.List<String> savedFileNames = new java.util.ArrayList<>();

        // Lặp qua tất cả các phần để tìm nhiều "hình ảnh"
        try {
            for (Part part : request.getParts()) {
                if ("images".equals(part.getName()) && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    if (fileName == null || fileName.isEmpty())
                        continue;

                    // Bơm Part vào byte stream và xả thẳng lên Cloudinary
                    try (java.io.InputStream is = part.getInputStream()) {
                        byte[] fileBytes = is.readAllBytes();
                        service.CloudinaryService cloudinaryService = new service.CloudinaryService();
                        String secureUrl = cloudinaryService.uploadImage(fileBytes, fileName,folderName);

                        savedFileNames.add(secureUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //fix bug ko thêm mới đc sản phẩm
                        response.sendRedirect(request.getContextPath() + "/admin/products?error=cloudinary_failed");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Sử dụng URL nếu không có tệp nào được tải lên.
        if (savedFileNames.isEmpty()) {
            String imageUrl = request.getParameter("image_url");
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                savedFileNames.add(imageUrl.trim());
            }
        }

        // 4. Kiểm tra xác nhận cuối cùng
        if (savedFileNames.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=no_image");
            return;
        }

        // 5. Đặt sản phẩm
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setImg(null);
        p.setDescription(description);
        p.setSupplier_name(supplier);
        p.setVolume(volume);

        // 6. Chèn thêm sản phẩm mới vào database
        ProductDAO dao = new ProductDAO();
        int newProductId = 0;
        try {
            newProductId = dao.insert(p);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?error=db_insert_failed");
            return;
        }

        // 7. Chèn hình ảnh và cập nhật hình ảnh vào trang sản phẩm chính.
        try {
            boolean first = true;
            for (String img : savedFileNames) {
                // chèn ảnh bằng id img
                int imageId = dao.insertImage(newProductId, img);

                if (first) {
                    // cập nhật sp và khóa phụ FK
                    dao.updateProductImage(newProductId, String.valueOf(imageId));
                    first = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?success=true&warning=image_error");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/products?success=true");
    }
}