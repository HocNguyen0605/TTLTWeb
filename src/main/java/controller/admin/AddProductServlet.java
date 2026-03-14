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
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        // No JSON response content type

        // 1. Lấy dữ liệu form
        String name = request.getParameter("name");
        String supplier = request.getParameter("supplier_name");
        String description = request.getParameter("description");

        // Handle optional input gracefully
        double price = 0;
        int volume = 0;
        int quantity = 0;

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

        // 2. Prepare upload logic
        String uploadPath = getServletContext().getRealPath("/images/product");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();

        java.util.List<String> savedFileNames = new java.util.ArrayList<>();

        // Loop through all parts to find multiple "images"
        try {
            for (Part part : request.getParts()) {
                if ("images".equals(part.getName()) && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    if (fileName == null || fileName.isEmpty())
                        continue;

                    String savedFileName = System.currentTimeMillis() + "_" + fileName;

                    // 1. Save to Runtime Directory (Immediate View)
                    part.write(uploadPath + File.separator + savedFileName);

                    // 2. Save to Source Directory (Best Effort Persistence)
                    String sourcePath = "D:\\antigravity_wnhom9\\mới\\web_nhom9\\src\\main\\webapp\\images\\product";
                    File sourceDir = new File(sourcePath);

                    if (!sourceDir.exists())
                        sourceDir.mkdirs();

                    if (sourceDir.exists()) {
                        try {
                            Path sourceFile = Paths.get(sourcePath, savedFileName);
                            Path runtimeFile = Paths.get(uploadPath, savedFileName);
                            Files.copy(runtimeFile, sourceFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    savedFileNames.add(savedFileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Fallback to URL if no files uploaded
        if (savedFileNames.isEmpty()) {
            String imageUrl = request.getParameter("image_url");
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                savedFileNames.add(imageUrl.trim());
            }
        }

        // 4. Final validation check
        if (savedFileNames.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=no_image");
            return;
        }

        // 5. Set Product
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setImg(null); // Set null so DB uses default or later update
        p.setDescription(description);
        p.setSupplier_name(supplier);
        p.setVolume(volume);

        // 6. Insert Product to DB
        ProductDAO dao = new ProductDAO();
        int newProductId = 0;
        try {
            newProductId = dao.insert(p);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?error=db_insert_failed");
            return;
        }

        // 7. Insert Images and update Main Product Image
        try {
            boolean first = true;
            for (String img : savedFileNames) {
                // Insert image to DB and get the ID
                int imageId = dao.insertImage(newProductId, img);

                if (first) {
                    // Update the product's main image FK
                    dao.updateProductImage(newProductId, String.valueOf(imageId));
                    first = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Even if image update fails, the product is created.
            // We might want to warn the user, but for now redirect with success (since
            // product exists)
            // or specific warning.
            response.sendRedirect(request.getContextPath() + "/admin/products?success=true&warning=image_error");
            return;
        }

        // Success Response - Redirect
        response.sendRedirect(request.getContextPath() + "/admin/products?success=true");
    }
}