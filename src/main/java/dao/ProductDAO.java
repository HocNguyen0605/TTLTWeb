package dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ProductDAO extends BaseDao {
    private Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public ProductDAO() {
    }

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
                        p.description,
                        p.promotion
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
                        p.description,
                        p.promotion,
                        p.group_id AS groupId
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.id = ?
                """;

        Connection currentConn = null;
        boolean shouldClose = false;
        try {
            if (this.conn != null) {
                currentConn = this.conn;
            } else {
                currentConn = util.DBContext.getConnection();
                shouldClose = true;
            }
            try (PreparedStatement ps = currentConn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setPrice(rs.getDouble("price"));
                        p.setVolume(rs.getInt("volume"));
                        p.setSupplier_name(rs.getString("supplier_name"));
                        p.setQuantity(rs.getInt("quantity"));
                        p.setImg(rs.getString("img"));
                        p.setDescription(rs.getString("description"));
                        p.setPromotion(rs.getInt("promotion"));
                        p.setGroupId(rs.getInt("groupId"));
                        return p;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldClose && currentConn != null) {
                try { currentConn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return null;
    }

    // Thêm sản phẩm
    public int insert(Product p) {
        if (p.getQuantity() == 0) {
            p.setQuantity(-1);
        }
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
                    LEFT JOIN product_images pi ON p.image = p.id WHERE p.quantity >= 0
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

    public List<Product> getProductsByGroupId(int groupId) {
        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description,
                        p.group_id AS groupId
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.group_id = ? AND p.group_id IS NOT NULL AND p.group_id > 0
                    GROUP BY p.id
                    ORDER BY p.volume ASC
                """;

        List<Product> list = new java.util.ArrayList<>();
        Connection currentConn = null;
        boolean shouldClose = false;
        try {
            if (this.conn != null) {
                currentConn = this.conn;
            } else {
                currentConn = util.DBContext.getConnection();
                shouldClose = true;
            }
            try (PreparedStatement ps = currentConn.prepareStatement(sql)) {
                ps.setInt(1, groupId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setPrice(rs.getDouble("price"));
                        p.setVolume(rs.getInt("volume"));
                        p.setSupplier_name(rs.getString("supplier_name"));
                        p.setQuantity(rs.getInt("quantity"));
                        p.setImg(rs.getString("img"));
                        p.setDescription(rs.getString("description"));
                        p.setGroupId(rs.getInt("groupId"));
                        list.add(p);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldClose && currentConn != null) {
                try { currentConn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return list;
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
        sql.append(" AND p.quantity > 0");

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
            sql.append(" ORDER BY p.id DESC");
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

        sql.append(" AND p.quantity >0");

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
                    WHERE p.quantity >0
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
        int finalQuantity = (quantity == 0) ? -1 : quantity;
        String sql = "UPDATE products SET quantity = :qty WHERE id = :id";
        get().useHandle(handle -> handle.createUpdate(sql)
                .bind("id", id)
                .bind("qty", finalQuantity)
                .execute());
    }

    public void updatePrice(int id, double price) {
        String sql = "UPDATE products SET price = :price WHERE id = :id";
        get().useHandle(handle -> handle.createUpdate(sql)
                .bind("id", id)
                .bind("price", price)
                .execute());
    }
    public void updatePromotionById(int productId, int promotionId) throws SQLException {
        String sql = "UPDATE products SET promotion = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, promotionId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
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
                    WHERE p.quantity > 0
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
    public List<Product> getProductHasPromotion() {
        String sql = """
                    SELECT
                        p.*,
                        pr.name AS promotionName
                    FROM products p
                    JOIN promotion pr ON p.promotion = pr.id
                    WHERE p.promotion IS NOT NULL
                    GROUP BY p.id
                """;

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Product.class)
                .list());
    }
    public int getMaxQuantityById(int idProduct){
        int maxQuantity = 0;
        String sql = """
                SELECT p.quantity FROM products p WHERE p.id = ?
                """;
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idProduct);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    maxQuantity=rs.getInt("quantity");
                }
            } catch(SQLException e){e.printStackTrace();}

        }catch(SQLException e){ e.printStackTrace();}
        return maxQuantity;
    }
    // Trong lớp ProductDAO.java
    public Product getProductForUpdate(int productId) throws SQLException {
        String sql = "SELECT id, product_name, quantity, volume, version FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("product_name"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setVolume(rs.getInt("volume"));
                    product.setVersion(rs.getInt("version"));
                    return product;
                }
            }
        }
        return null;
    }
    //Update lại số lượng tồn với trường hợp 2 người cùng mua 1 thời điểm cho 1 sản phẩm
    public boolean updateProductQuantity(int productId, int quantityToSubtract, int oldVersion) throws SQLException {
        String sql = "UPDATE products SET quantity = CASE WHEN quantity - ? = 0 THEN -1 ELSE quantity - ? END, version = version + 1 " +
                "WHERE id = ? AND version = ? AND quantity >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityToSubtract);
            ps.setInt(2, quantityToSubtract);
            ps.setInt(3, productId);
            ps.setInt(4, oldVersion);
            ps.setInt(5, quantityToSubtract);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public List<Product> getOutOfStockProducts() {
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
                    WHERE p.quantity = -1
                    GROUP BY p.id
                """;
        return get().withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Product.class)
                .list());
    }
    //lấy danh sách sản phẩm dựa trên danh sách id
    public List<Product> getProductsByIDs(List<Integer> listIdProduct) {
        List<Product> listProduct = new ArrayList<>();

        if (listIdProduct == null || listIdProduct.isEmpty()) {
            return listProduct;
        }

        StringJoiner placeholders = new StringJoiner(",");
        for (int i = 0; i < listIdProduct.size(); i++) {
            placeholders.add("?");
        }

        String sql = """
                    SELECT
                        p.id AS id,
                        p.product_name AS name,
                        p.price,
                        p.volume,
                        p.supplier_name,
                        p.quantity,
                        COALESCE(pi.image_URL, p.image) AS img,
                        p.description,
                        p.promotion
                    FROM products p
                    LEFT JOIN product_images pi ON p.image = pi.id
                    WHERE p.id IN (""" + placeholders.toString() + """
                    ) AND p.quantity > 0
                    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < listIdProduct.size(); i++) {
                ps.setInt(i + 1, listIdProduct.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setVolume(rs.getInt("volume"));
                    product.setSupplier_name(rs.getString("supplier_name"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setImg(rs.getString("img"));
                    product.setDescription(rs.getString("description"));
                    product.setPromotion(rs.getInt("promotion"));

                    listProduct.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listProduct;
    }
    public int hideOutOfStockProducts() {
        String sql = "UPDATE products SET status = 'inactive' WHERE stock <= 0 AND status = 'active'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public List<Product> getAvailableProducts() {
        String sql = """
                SELECT 
                    p.id AS id,
                    p.product_name AS name,
                    p.price,
                    p.volume,
                    p.supplier_name,
                    p.quantity,
                    COALESCE(pi.image_URL, p.image) AS img,
                    p.description,
                    p.promotion
                FROM products p
                LEFT JOIN product_images pi ON p.image = pi.id
                GROUP BY p.id
                ORDER BY p.quantity ASC
            """;
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Product.class)
                .list());
    }
}
