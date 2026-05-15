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
                    <form action="/admin/banner" method="get">
                        <input type="text" name="search" class="search-input"
                               placeholder="Nhập tên banner để tìm kiếm" value=${currentSearch}>
                        <button type="submit" class="btn p-0 border-0">
                            <i class="bi bi-search search-icon"></i>
                        </button>
                    </form>
                </div>
                <!--Hiển thị danh sách banner tìm kiếm chạy bằng ajax -->
                <div class="search-container position-relative">
                    <input type="text" id="searchInput" name="search" class="search-input" autocomplete="off" ...>
                    <div id="searchSuggestions" class="list-group position-absolute w-100 shadow-lg" style="z-index: 1000; display: none;">
                    </div>
                </div>
                <!--Nút tạo banner mơis-->
                <button class="btn btn-success w-100 mt-4 fw-semibold rounded-pill"
                        data-bs-toggle="modal" data-bs-target="#addBannerModal">
                    Tạo Banner mới
                </button>
            </div>
        </div>
        <c:choose>
            <c:when test="${not empty banners}">
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">Không tìm thấy banner nào khớp với từ khóa "${currentSearch}"</div>
            </c:otherwise>
        </c:choose>
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
                                            <button type="button" class="btn btn-sm btn-warning"
                                                    data-bs-toggle="modal" data-bs-target="#updateBannerModal"
                                                    onclick="openEditModal('${b.id}', '${b.title}', '${b.imageUrl}', '${b.linkUrl}', '${b.priority}', '${b.isActive}')">
                                                Sửa
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
    <!-- Modal chỉnh sửa banner -->
    <div class="modal fade" id="updateBannerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content shadow-lg">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title">
                        <i class="bi bi-pencil-square me-2"></i>Chỉnh sửa Banner
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/admin/banner" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" id="edit_id">
                        <div class="row">
                            <!-- Tiêu đề -->
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-semibold text-secondary">Tiêu đề Banner</label>
                                <input type="text" name="title" id="edit_title" class="form-control" required>
                            </div>

                            <!-- Ảnh hiện tại, Chọn ảnh mới -->
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-semibold text-secondary">Hình ảnh Banner</label>
                                <div class="mb-2">
                                    <small class="text-muted">Ảnh hiện tại:</small><br>
                                    <img src="" id="edit_preview_old" class="rounded border" style="max-height: 100px;">
                                </div>
                                <input type="file" name="bannerImage" class="form-control" accept="image/*" onchange="previewImageUpdate(this)">
                                <div class="form-text">Để trống nếu không muốn thay đổi ảnh.</div>
                                <div id="imagePreviewUpdate" class="mt-2 d-none">
                                    <small class="text-success">Ảnh mới chọn:</small><br>
                                    <img src="" class="img-thumbnail" style="max-height: 100px;">
                                </div>
                            </div>

                            <!-- Link URL -->
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-semibold text-secondary">Đường dẫn liên kết (URL)</label>
                                <input type="text" name="link_url" id="edit_link_url" class="form-control">
                            </div>

                            <!-- Độ ưu tiên -->
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Thứ tự hiển thị</label>
                                <input type="number" name="priority" id="edit_priority" class="form-control" min="1" required>
                            </div>

                            <!-- Trạng thái -->
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold text-secondary">Trạng thái</label>
                                <select name="is_active" id="edit_is_active" class="form-select">
                                    <option value="true">Hiển thị</option>
                                    <option value="false">Ẩn</option>
                                </select>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                            <button type="button" class="btn btn-light px-4 border" data-bs-dismiss="modal">Hủy bỏ</button>
                            <button type="submit" class="btn btn-primary px-4">Cập nhật Banner</button>
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