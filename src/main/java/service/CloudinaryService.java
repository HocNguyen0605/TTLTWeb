package service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dul34qkob",
                "api_key", "385253856638365",
                "api_secret", "luC-h8ZGyI9NLtPGFP17xLohSX8"
        ));
    }

    /**
     * Tải file ảnh dưới dạng mảng byte lên Cloudinary và trả về secure URL.
     * @param fileBytes Mảng byte của ảnh
     * @param fileName Tên gốc của ảnh (tùy chọn, làm id trên Cloudinary)
     * @return Chuỗi link ảnh an toàn (HTTPS)
     * @throws IOException Bắt lỗi nạp luồng
     */
    public String uploadImage(byte[] fileBytes, String fileName) throws IOException {
        String publicId = "file_" + System.currentTimeMillis();
        if (fileName != null && fileName.contains(".")) {
            publicId = fileName.substring(0, fileName.lastIndexOf("."));
        }

        Map params = ObjectUtils.asMap(
                "public_id", "juicy_product_" + System.currentTimeMillis() + "_" + publicId,
                "folder", "juicy/products"
        );
        Map result = cloudinary.uploader().upload(fileBytes, params);
        return (String) result.get("secure_url");
    }
}
