<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Juicy</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<!-- HEADER -->
<header class="sticky-top shadow-sm">
    <nav class="navbar navbar-expand-lg navbar-light bg-white py-3">
        <div class="container">
            <a class="navbar-brand fw-bold text-success fs-3"
               href="${pageContext.request.contextPath}/admin/dashboard">
                <img src="${pageContext.request.contextPath}/images/logo/logo-juicy.png" height="40"
                     class="me-2">
                JUICY
            </a>
        </div>
    </nav>
</header>


<!-- Main Content -->
<div class="d-flex">
    <!-- Sidebar -->
    <div class="bg-success text-white p-3" style="width: 250px; min-height: 100vh;">
        <h4>Menu</h4>
        <ol class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link text-white ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''} "
                   href="${pageContext.request.contextPath}/admin/dashboard">a. Dashboard</a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white " href="#menuQL" data-bs-toggle="collapse" >
                    b. Quản lý
                </a>
                <ol class="collapse" id="menuQL">
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('products') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/products">
                            Quản lý sản phẩm </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('banner') ? 'active' : ''}"
                           href="#">
                            Quản lý banner </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('CTKM') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/CTKM">
                            Quản lý CTKM </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('manage-orders') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/manage-orders">
                            Quản lý đơn hàng </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('accounts') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/accounts">
                            Quản lý Account </a>
                    </li>
                </ol>
            </li>
            <li class="mt-3">
                <a href="${pageContext.request.contextPath}/logout"
                   class="btn btn-danger rounded-pill ms-3">
                    Đăng xuất
                </a>
            </li>
        </ol>
    </div>
    <div class="container my-5">
        <div
                class="d-flex flex-column flex-md-row justify-content-between align-items-center mb-4 animate__animated animate__fadeInDown">
            <div>
                <h2 class="fw-bold text-success mb-1">Quản Lý Chương Trình Khuyến Mãi</h2>
                <p class="text-muted mb-0">Xem và quản lý tất cả CTKM hiện có</p>
            </div>

            <div class="d-flex gap-3 mt-3 mt-md-0 align-items-center">
                <div class="search-container d-none d-md-block">
                    <form action="products" method="get">
                        <i class="bi bi-search search-icon"></i>
                        <input type="text" name="search" class="search-input"
                               placeholder="Tìm kiếm sản phẩm..." value="${param.search}">
                    </form>
                </div>
                <button class="btn btn-premium" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="bi bi-plus-lg me-2"></i>Thêm Mới
                </button>
            </div>
        </div>

        <div class="card card-custom animate__animated animate__fadeInUp">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom mb-0">
                        <thead>
                        <tr>
                            <th class="ps-4">Sản Phẩm</th>
                            <th>Giá Bán</th>
                            <th>Thể Tích</th>
                            <th>Tên chương trình</th>
                            <th class="text-center">Hiện trạng</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>

                            <c:when test="${empty sessionScope.product || empty sessionScope.product.promotion}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">
                                        Hiện không chương trình khuyến mãi của bất kì sản phẩm nào!!!
                                    </td>
                                </tr>
                            </c:when>

                            <c:otherwise>
                                <c:forEach var="p" items="${products}">
                                    <tr>
                                        <td class="ps-4">
                                            <div class="d-flex align-items-center">
                                                <div class="position-relative">
                                                    <c:choose>
                                                        <c:when test="${p.img != null && p.img.contains('http')}">
                                                            <img src="${p.img}" style="width:60px;height:60px;object-fit:cover;">
                                                        </c:when>
                                                        <c:when test="${p.img != null && (p.img.contains('/') || p.img.contains('\\\\'))}">
                                                            <img src="${pageContext.request.contextPath}/${p.img}" style="width:60px;height:60px;object-fit:cover;">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/images/product/${p.img}" style="width:60px;height:60px;object-fit:cover;">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="ms-3">
                                                    <h6>${p.name}</h6>
                                                    <small>ID: #${p.id}</small>
                                                </div>
                                            </div>
                                        </td>

                                        <td>${p.price}₫</td>
                                        <td>${p.volume}ml</td>
                                        <td>${p.promotion}</td>

                                        <td class="text-center">
                                            <form method="post" action="products">
                                                <input type="hidden" name="id" value="${p.id}">

                                                <c:choose>
                                                    <c:when test="${p.quantity == -1}">
                                                        <input type="hidden" name="action" value="show">
                                                        <button>Hiện</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="hidden" name="action" value="hidden">
                                                        <button>Ẩn</button>
                                                    </c:otherwise>
                                                </c:choose>

                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>

                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div> <!-- Closing d-flex -->

<!-- Modal thêm sản phẩm -->
<div class="modal fade" id="addProductModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content shadow-lg">
            <!-- Header -->
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="bi bi-box-seam-fill me-2"></i>Thêm Sản Phẩm Mới
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <form method="post" action="${pageContext.request.contextPath}/add-product"
                      enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold text-secondary">Tên sản phẩm</label>
                            <input type="text" name="name" class="form-control"
                                   placeholder="Nhập tên sản phẩm..." required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold text-secondary">Giá bán (VNĐ)</label>
                            <div class="input-group">
                                <input type="number" name="price" class="form-control" placeholder="0"
                                       required>
                                <span class="input-group-text bg-light">₫</span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold text-secondary">Thể tích (ml)</label>
                            <input type="number" name="volume" class="form-control"
                                   placeholder="Ví dụ: 500" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold text-secondary">Số lượng nhập</label>
                            <input type="number" name="quantity" class="form-control" placeholder="0"
                                   required>
                        </div>
                        <div class="col-md-12 mb-3">
                            <label class="form-label fw-semibold text-secondary">Nhà Cung Cấp</label>
                            <input type="text" name="supplier_name" class="form-control"
                                   placeholder="Nhập tên nhà cung cấp..." required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-secondary">Mô tả sản phẩm</label>
                        <textarea name="description" class="form-control" rows="3"
                                  placeholder="Nhập mô tả chi tiết sản phẩm..."></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-secondary">Link ảnh (URL)</label>
                        <input type="url" name="image_url" class="form-control"
                               placeholder="https://example.com/image.png">
                        <div class="form-text">Dùng link ảnh trực tiếp (Cloudinary, Imgur...) để không
                            bị mất ảnh.</div>
                    </div>

                    <div class="text-center text-muted my-2 fw-bold">- HOẶC -</div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold text-secondary">Upload ảnh từ máy</label>
                        <input type="file" id="imageInput" name="images" class="form-control"
                               accept="image/*">
                        <div class="form-text">Hỗ trợ định dạng: .jpg, .png. Kích thước tối đa 5MB.
                        </div>

                        <!-- Image Preview Container -->
                        <div class="mt-3 text-center">
                            <img id="previewImage" src="#" alt="Preview"
                                 style="display:none; max-width: 200px; max-height: 200px; object-fit: cover; border-radius: 10px; border: 1px solid #ddd; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                        </div>

                        <c:if test="${param.error == 'no_image'}">
                            <div class="alert alert-danger mt-2 d-flex align-items-center">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                Vui lòng chọn ảnh hoặc nhập URL!
                            </div>
                        </c:if>
                    </div>

                    <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                        <button type="button" class="btn btn-light px-4 border"
                                data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-premium px-4" id="btnSaveProduct">
                            <i class="bi bi-check-lg me-1"></i> Lưu Sản Phẩm
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>


<!--footer-->
<footer class="bg-dark text-white pt-5 pb-4">
    <div class="container text-center text-md-start">
        <div class="row text-center text-md-start">
            <div class="col-md-3 col-lg-3 col-xl-3 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">JUICY</h5>
                <p>Mang đến nguồn dinh dưỡng từ thiên nhiên, tốt cho sức khỏe.</p>
            </div>

            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">Danh Mục</h5>
                <p>
                    <a href="products.html" class="text-white text-decoration-none">Nước Ép</a>
                </p>
                <p>
                    <a href="products.html" class="text-white text-decoration-none">Trái Cây Văn
                        Phòng</a>
                </p>
                <p>
                    <a href="promotions.html" class="text-white text-decoration-none">Khuyến Mãi</a>
                </p>
            </div>

            <div class="col-md-4 col-lg-3 col-xl-3 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">Liên Hệ</h5>
                <p>
                    <i class="bi bi-geo-alt-fill me-2"></i> Đường số 7, Đông Hoà, Thủ
                    Đức, Thành phố Hồ Chí Minh, Việt Nam
                </p>
                <p><i class="bi bi-envelope-fill me-2"></i> order@juicy.vn</p>
                <p><i class="bi bi-telephone-fill me-2"></i> 0347 270 120</p>
            </div>
            <div class="col-md-3 mb-4">
                <h5 class="text-uppercase fw-bold text-success">Theo Dõi Chúng Tôi</h5>
                <a href="#" class="text-white me-3"><i class="bi bi-facebook"></i></a>
                <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
                <a href="#" class="text-white me-3"><i class="bi bi-tiktok"></i></a>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-md-12 text-center pt-3 border-top border-secondary">
                <p>© 2024 Juicy. All Rights Reserved.</p>
            </div>
        </div>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script type="module" src="js/init.js"></script>

<script>
    // 1. Preview Image Logic
    const input = document.getElementById("imageInput");
    const preview = document.getElementById("previewImage");

    if (input) {
        input.addEventListener("change", () => {
            const file = input.files[0];
            if (!file) {
                preview.style.display = "none";
                return;
            }

            const reader = new FileReader();
            reader.onload = e => {
                preview.src = e.target.result;
                preview.style.display = "block";
            };
            reader.readAsDataURL(file);
        });
    }
</script>
</body>

</html>