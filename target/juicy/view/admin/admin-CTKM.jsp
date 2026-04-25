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
                <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
                <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
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
                                <button class="btn btn-success w-100 mt-4 fw-semibold rounded-pill"
                                        data-bs-toggle="modal" data-bs-target="#addVoucherModal">
                                    Tạo voucher mới
                                </button>
                                <button type="submit" class="btn btn-success w-100 mt-4 fw-semibold rounded-pill"
                                        data-bs-toggle="modal" data-bs-target="#addPromotionModal">
                                   Tạo CTKM mới
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

                                            <c:when test="${empty promotionProducts}">
                                                <tr>
                                                    <td colspan="5" class="text-center text-muted py-4">
                                                        Hiện không chương trình khuyến mãi của bất kì sản phẩm nào!!!
                                                    </td>
                                                </tr>
                                            </c:when>

                                            <c:otherwise>
                                                <c:forEach var="p" items="${promotionProducts}">
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
                                                        <td>${p.promotionName}</td>

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

                    <!-- Modal thêm voucher -->
                    <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-centered">
                            <div class="modal-content shadow-lg">
                                <!-- Header -->
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <i class="bi bi-box-seam-fill me-2"></i>Thêm Voucher Mới
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <!-- Body -->
                                <div class="modal-body">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/voucher">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="type" value="voucher">
                                        <input type="hidden" name="status" value="active">
                                        <div class="row">
                                            <!-- Tên voucher -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Tên Voucher</label>
                                                <input type="text" name="name" class="form-control"
                                                       placeholder="SALE50, GIAM10..." required>
                                            </div>

                                            <!-- Mã voucher -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Mã Voucher</label>
                                                <input type="text" name="code" class="form-control"
                                                       placeholder="SALE50, GIAM10..." required>
                                            </div>

                                            <!-- Loại giảm giá -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Loại giảm giá</label>
                                                <select name="discount_type" class="form-select" required>
                                                    <option value="">-- Chọn loại --</option>
                                                    <option value="percent">Giảm theo %</option>
                                                    <option value="amount">Giảm theo số tiền</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <!-- Giá trị giảm -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Giá trị giảm</label>
                                                <input type="number" name="discount_value" class="form-control"
                                                       placeholder="10 hoặc 50" required>
                                            </div>

                                            <!-- Số lượng -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Số lượng Voucher</label>
                                                <input type="number" name="quantity" class="form-control"
                                                       placeholder="100" required>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <!-- Ngày bắt đầu -->
                                            <div class="col-md-6 mb-3">
                                                <label class="form-label fw-semibold text-secondary">Ngày bắt đầu</label>
                                                <input type="datetime-local" name="start_date" class="form-control" required>
                                            </div>

                                            <!-- Ngày kết thúc -->
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
                                                <i class="bi bi-check-lg me-1"></i> Lưu Voucher
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Modal thêm CTKM -->
                    <div class="modal fade" id="addPromotionModal" tabindex="-1" aria-hidden="true">
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