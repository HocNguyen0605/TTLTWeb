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
                <a class="nav-link text-white " href="#menuQL" data-bs-toggle="collapse">
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
                           href="${pageContext.request.contextPath}/admin/banner">
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
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('reviews') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/reviews">
                            Quản lý Đánh giá </a>
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
                <h2 class="fw-bold text-success mb-1">Quản Lý Banner </h2>
                <p class="text-muted mb-0">Xem và quản lý tất cả Banner hiện có</p>
            </div>

            <div class="d-flex gap-3 mt-3 mt-md-0 align-items-center">
                <div class="search-container d-none d-md-block">
                    <form action="products" method="get">
                        <i class="bi bi-search search-icon"></i>
                        <input type="text" name="search" class="search-input"
                               placeholder="Tìm kiếm banner..." value="${param.search}">
                    </form>
                </div>
                <button class="btn btn-success w-100 mt-4 fw-semibold rounded-pill"
                        data-bs-toggle="modal" data-bs-target="#addBannerModal">
                    Tạo Banner mới
                </button>
            </div>
        </div>

        <div class="card card-custom animate__animated animate__fadeInUp">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom mb-0">
                        <thead>
                        <tr>
                            <th class="ps-4">Banner</th>
                            <th>Tên</th>
                            <th>Vị trí</th>
                            <th>Trạng thái</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>

                            <c:when test="${empty banners}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">
                                        Hiện không bất kì banner nào!!!
                                    </td>
                                </tr>
                            </c:when>

                            <c:otherwise>
                                <c:forEach var="b" items="${banners}">
                                    <tr>
                                        <td class="ps-4">
                                            <div class="d-flex align-items-center">
                                                <div class="d-flex align-items-center">
                                                    <img src="${b.imageUrl}"
                                                         width="60"
                                                         class="rounded me-3"
                                                         alt="${b.title}">
                                                </div>
                                            </div>
                                        </td>

                                        <td>${b.title}</td>
                                        <td>${b.priority}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${b.isActive}">
                                                    <span class="badge bg-success">Đang hiện</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">Đang ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <button type="button" class="btn btn-success w-30 "
                                                    data-bs-toggle="modal" data-bs-target="#updateBannerModal">
                                                <i class="bi bi-credit-card me-1"></i> Chỉnh sửa
                                            </button>
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

    <!-- Modal thêm banner -->
    <div class="modal fade" id="addBannerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content shadow-lg">
                <!-- Header: Thay đổi icon và tiêu đề -->
                <div class="modal-header ">
                    <h5 class="modal-title">
                        <i class="bi bi-image-fill me-2"></i>Thêm Banner Quảng Cáo Mới
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <!-- Body -->
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/admin/banner" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="add">

                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-semibold text-secondary">Tiêu đề Banner</label>
                                <input type="text" name="title" class="form-control"
                                       placeholder="Ví dụ: Khuyến mãi mùa hè - Nước ép cam" required>
                            </div>

                            <!-- Chọn File Ảnh -->
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-semibold text-secondary">Hình ảnh Banner</label>
                                <input type="file" name="bannerImage" class="form-control"
                                       accept="image/*" required onchange="previewImage(this)">
                                <div class="form-text">Lưu ý: Nên chọn ảnh tỉ lệ ngang (16:9) để hiển thị đẹp nhất.</div>
                                <div id="imagePreview" class="mt-2 d-none">
                                    <img src="" alt="Preview" class="img-thumbnail" style="max-height: 150px;">
                                </div>
                            </div>

                            <!-- Đường dẫn liên kết (Link URL) -->
                            <div class="col-md-8 mb-3">
                                <label class="form-label fw-semibold text-secondary">Đường dẫn khi click (URL)</label>
                                <input type="text" name="link_url" class="form-control"
                                       placeholder="/san-pham/nuoc-ep-tao hoặc https://...">
                            </div>

                            <!-- Độ ưu tiên (Priority) -->
                            <div class="col-md-4 mb-3">
                                <label class="form-label fw-semibold text-secondary">Thứ tự hiển thị</label>
                                <input type="number" name="priority" class="form-control"
                                       placeholder="1, 2, 3..." value="1" min="1" required>
                            </div>
                        </div>

                        <!-- Nút bấm -->
                        <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                            <button type="button" class="btn btn-light px-4 border" data-bs-dismiss="modal">Hủy bỏ</button>
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="bi bi-cloud-arrow-up-fill me-1"></i> Upload & Lưu Banner
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal thêm CTKM -->
    <div class="modal fade" id="updateBannerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content shadow-lg">
                <!-- Header -->
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="bi bi-box-seam-fill me-2"></i>Thêm CTKM Mới
                    </h5>
                    <p class="text-muted small">Áp dụng tối đa cho 2 sản phẩm khác nhau</p>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    <hr>
                </div>
                <!-- Body -->
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/admin/addCTKM">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="type" value="combo">
                        <input type="hidden" name="status" value="active">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Tên Chương trình</label>
                                <input type="text" name="name" class="form-control"
                                       placeholder="Khuyến mãi mùa hè..." required>
                            </div>
                        </div>

                        <hr class="m-3 text-secondary">
                        <h6 class="mb-3 fw-bold text-primary"><i class="bi bi-box-seam me-2"></i>Sản phẩm áp dụng</h6>

                        <div class="row">
                            <div class="col-md-8 mb-3">
                                <label class="form-label fw-semibold text-secondary">Sản phẩm 1 (Bắt buộc)</label>
                                <select name="product_id_1" class="form-select select2-enable" required>
                                    <option value="">-- Nhập tên sản phẩm chính --</option>
                                    <c:forEach items="${productList}" var="p">
                                        <option value="${p.id}">${p.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label class="form-label fw-semibold text-secondary">Số lượng</label>
                                <input type="number" name="quantity_1" class="form-control" value="1" min="1">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-8 mb-3">
                                <label class="form-label fw-semibold text-secondary">Sản phẩm 2 (Tùy chọn)</label>
                                <select name="product_id_2" class="form-select">
                                    <option value="0">-- Không chọn thêm sản phẩm --</option>
                                    <c:forEach items="${productList}" var="p">
                                        <option value="${p.id}">${p.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label class="form-label fw-semibold text-secondary">Số lượng</label>
                                <input type="number" name="quantity_2" class="form-control" value="1" min="1">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Loại giảm giá</label>
                                <select name="discount_type" class="form-select" required>
                                    <option value="">-- Chọn loại --</option>
                                    <option value="percent">Giảm theo %</option>
                                    <option value="amount">Giảm theo số tiền</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Giá trị giảm</label>
                                <input type="number" name="discount_value" class="form-control"
                                       placeholder="Ví dụ: 10 hoặc 50000" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Ngày bắt đầu</label>
                                <input type="datetime-local" name="start_date" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Ngày kết thúc</label>
                                <input type="datetime-local" name="end_date" class="form-control" required>
                            </div>
                        </div>
                        <!-- Nút -->
                        <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                            <button type="button" class="btn btn-light px-4 border"
                                    data-bs-dismiss="modal">Hủy bỏ</button>
                            <button type="submit" class="btn btn-premium px-4">
                                <i class="bi bi-check-lg me-1"></i> Lưu
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
</body>

</html>