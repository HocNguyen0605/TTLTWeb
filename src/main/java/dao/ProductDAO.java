package dao;

import model.Product;

import java.util.List;

public class ProductDAO extends BaseDao {

    // Lấy tất cả sản phẩm (cho trang Products)
    public List<Product> getAll() {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    GROUP BY p.id
                """;

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Product.class)
                .list());
    }

    public List<Product> getListProduct() {
        return getAll();
    }

    // Lấy sản phẩm theo ID
    public Product findById(int id) {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.id = :id
                """;

        return get().withHandle(handle -> handle.createQuery(sql)
                .bind("id", id)
                .mapToBean(Product.class)
                .findOne()
                .orElse(null));
    }

    // Thêm sản phẩm
    public int insert(Product p) {
        String sql = """
                    INSERT INTO products
                    (product_name, price, volume, supplier_name, quantity, image, description)
                    VALUES (:name, :price, :volume, :supplier_name, :quantity, :img, :description)
                """;

        return get().withHandle(handle -> handle.createUpdate(sql)
                .bindBean(p)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .one());
    }

    public int insertImage(int productId, String imageUrl) {
        String sql = "INSERT INTO product_images (id_product, image_URL) VALUES (:pid, :url)";
        return get().withHandle(handle -> handle.createUpdate(sql)
                .bind("pid", productId)
                .bind("url", imageUrl)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .one());
    }

    public void updateProductImage(int productId, String imageRef) {
        String sql = "UPDATE products SET image = :img WHERE id = :pid";
        get().useHandle(handle -> handle.createUpdate(sql)
                .bind("pid", productId)
                .bind("img", imageRef)
                .execute());
    }

    // fill sản phẩm lọc ra sản phẩm
    public List<Product> getProducts(String sortBy) {
        // Câu query cơ bản
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    GROUP BY p.id
                """;

        // Thêm logic sắp xếp dựa trên tham số truyền vào
        if ("priceAsc".equals(sortBy)) {
            sql += " ORDER BY p.price ASC";
        } else if ("priceDesc".equals(sortBy)) {
            sql += " ORDER BY p.price DESC";
        } else if ("nameAsc".equals(sortBy)) {
            sql += " ORDER BY p.product_name ASC";
        }

        String finalSql = sql;
        return jdbi.withHandle(handle -> handle.createQuery(finalSql)
                .mapToBean(Product.class)
                .list());
    }

    // getTopBestSeller: used in HOME page -> Filter hidden
    public List<Product> getTopBestSeller() {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.quantity >= 0
                    ORDER BY RAND()
                    LIMIT 8
                """;

        return get().withHandle(h -> h.createQuery(sql)
                .mapToBean(Product.class)
                .list());
    }

    // getRelatedProducts: used in Product Detail -> Filter hidden
    public List<Product> getRelatedProducts(int currentId) {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.id != :id AND p.quantity >= 0
                    ORDER BY RAND()
                    LIMIT 8
                """;

        return get().withHandle(handle -> handle.createQuery(sql)
                .bind("id", currentId)
                .mapToBean(Product.class)
                .list());
    }

    // Lấy danh sách thể tích duy nhất để hiển thị bộ lọc
    public List<Integer> getAllVolumes() {
        String sql = "SELECT DISTINCT volume FROM products ORDER BY volume ASC";
        return get().withHandle(handle -> handle.createQuery(sql)
                .mapTo(Integer.class)
                .list());
    }

    // Lấy danh sách nhà cung cấp duy nhất
    public List<String> getAllSuppliers() {
        String sql = "SELECT DISTINCT supplier_name FROM products WHERE supplier_name IS NOT NULL ORDER BY supplier_name ASC";
        return get().withHandle(handle -> handle.createQuery(sql)
                .mapTo(String.class)
                .list());
    }

    // Lọc sản phẩm
    public List<Product> getFilteredProducts(Double minPrice, Double maxPrice, Integer minVol, Integer maxVol,
                                             String supplier,
                                             String sortBy, int offset, int limit) {
        StringBuilder sql = new StringBuilder("""
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE 1=1
                """);

        if (minPrice != null) {
            sql.append(" AND p.price >= :minPrice");
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= :maxPrice");
        }

        // Filter hidden products for valid valid
        sql.append(" AND p.quantity >= 0");

        if (minVol != null) {
            sql.append(" AND p.volume >= :minVol");
        }
        if (maxVol != null) {
            sql.append(" AND p.volume <= :maxVol");
        }

        if (supplier != null && !supplier.isEmpty()) {
            sql.append(" AND p.supplier_name = :supplier");
        }

        // Sorting
        if ("priceAsc".equals(sortBy)) {
            sql.append(" ORDER BY p.price ASC");
        } else if ("priceDesc".equals(sortBy)) {
            sql.append(" ORDER BY p.price DESC");
        } else if ("nameAsc".equals(sortBy)) {
            sql.append(" ORDER BY p.product_name ASC");
        } else {
            sql.append(" ORDER BY p.id DESC"); // Default sort: Newest first
        }

        sql.append(" LIMIT :offset, :limit");

        return get().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (minPrice != null)
                query.bind("minPrice", minPrice);
            if (maxPrice != null)
                query.bind("maxPrice", maxPrice);
            if (minVol != null)
                query.bind("minVol", minVol);
            if (maxVol != null)
                query.bind("maxVol", maxVol);
            if (supplier != null && !supplier.isEmpty())
                query.bind("supplier", supplier);
            query.bind("offset", offset);
            query.bind("limit", limit);
            return query.mapToBean(Product.class).list();
        });
    }

    public int getTotalFilteredProducts(Double minPrice, Double maxPrice, Integer minVol, Integer maxVol,
                                        String supplier) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE 1=1 ");
        if (minPrice != null)
            sql.append(" AND p.price >= :minPrice");
        if (maxPrice != null)
            sql.append(" AND p.price <= :maxPrice");

        sql.append(" AND p.quantity >= 0"); // Filter hidden

        if (minVol != null) {
            sql.append(" AND p.volume >= :minVol");
        }
        if (maxVol != null) {
            sql.append(" AND p.volume <= :maxVol");
        }
        if (supplier != null && !supplier.isEmpty())
            sql.append(" AND p.supplier_name = :supplier");

        return get().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (minPrice != null)
                query.bind("minPrice", minPrice);
            if (maxPrice != null)
                query.bind("maxPrice", maxPrice);
            if (minVol != null)
                query.bind("minVol", minVol);
            if (maxVol != null)
                query.bind("maxVol", maxVol);
            if (supplier != null && !supplier.isEmpty())
                query.bind("supplier", supplier);
            return query.mapTo(Integer.class).one();
        });
    }

    // Tìm kiếm theo tên
    public List<Product> searchByName(String keyword) {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.product_name LIKE :keyword AND p.quantity >= 0
                """;
        return get().withHandle(handle -> handle.createQuery(sql)
                .bind("keyword", "%" + keyword + "%")
                .mapToBean(Product.class)
                .list());
    }

    // Lọc theo NCC và Giá tối đa
    public List<Product> filterProducts(String supplier, Double maxPrice) {
        StringBuilder sql = new StringBuilder("""
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.quantity >= 0
                """);

        if (supplier != null && !supplier.trim().isEmpty()) {
            sql.append(" AND p.supplier_name = :supplier");
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= :maxPrice");
        }

        return get().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (supplier != null && !supplier.trim().isEmpty()) {
                query.bind("supplier", supplier);
            }
            if (maxPrice != null) {
                query.bind("maxPrice", maxPrice);
            }
            return query.mapToBean(Product.class).list();
        });
    }

    // --- ADMIN ACTION: Hide/Show Product ---
    public void updateStatus(int id, int status) {
        // status = -1 (Hidden), status >= 0 (Visible)
        String sql = "UPDATE products SET quantity = :status WHERE id = :id";
        get().useHandle(handle -> handle.createUpdate(sql)
                .bind("id", id)
                .bind("status", status)
                .execute());
    }

    public void updateQuantity(int id, int quantity) {
        String sql = "UPDATE products SET quantity = :qty WHERE id = :id";
        get().useHandle(handle -> handle.createUpdate(sql)
                .bind("id", id)
                .bind("qty", quantity)
                .execute());
    }

    // --- PHÂN TRANG ---
    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE quantity >= 0";
        return get().withHandle(handle -> handle.createQuery(sql)
                .mapTo(Integer.class)
                .one());
    }

    public List<Product> getProductsByPage(int offset, int limit) {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        CASE
                            WHEN p.image LIKE '%.%' THEN p.image
                            ELSE pi.image_URL
                        END AS img,
                        p.description
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.quantity >= 0
                    GROUP BY p.id
                    ORDER BY p.id DESC
                    LIMIT :offset, :limit
                """;
        return get().withHandle(handle -> handle.createQuery(sql)
                .bind("offset", offset)
                .bind("limit", limit)
                .mapToBean(Product.class)
                .list());
    }

}