<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="home" />
</jsp:include>

<!--Thêm thanh tìm kiếm vào dưới phần header của website-->
<section class="bg-light py-4 border-bottom">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <form class="d-flex" action="${pageContext.request.contextPath}/search" method="get">
                    <input class="form-control form-control-lg me-2 border-success" type="search"
                           name="query" placeholder="Tìm kiếm tên sản phẩm, loại trái cây..."
                           aria-label="Search">
                    <button class="btn btn-primary-custom btn-lg fw-bold" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>

<!-- HERO SECTION -->
<section class="hero-section d-flex align-items-center">
    <div class="container text-center">
        <section id="hero-carousel" class="carousel slide" data-bs-ride="carousel"
                 data-bs-interval="3000">
            <div class="carousel-inner">
                <div class="carousel-item">
                    <img src="images/banner/orangejuice.jpg" class="d-block w-100" alt="Nước Ép Cam">
                    <div class="carousel-caption  d-md-block">
                        <h2 class="fw-bold">Nước Ép Cam Tươi Mát</h2>
                        <p>Giảm ngay <span class="text-warning fw-bold">20%</span> cho đơn hàng đầu
                            tiên!</p>
                        <a href="${pageContext.request.contextPath}/products"
                           class="btn btn-warning rounded-pill px-4">Mua Ngay</a>
                    </div>
                </div>

                <div class="carousel-item active">
                    <img src="images/banner/service.jpg" class="d-block w-100" alt="dịch vụ">
                    <div class="carousel-caption  d-md-block">
                        <h2 class="fw-bold">Tiện Lợi – Nhanh Chóng – Hiệu Quả</h2>
                        <p>Mang đến trải nghiệm tốt nhất từng phút giây</p>
                        <a href="${pageContext.request.contextPath}/products"
                           class="btn btn-warning rounded-pill px-4">Xem Thêm</a>
                    </div>
                </div>

                <div class="carousel-item">
                    <img src="images/banner/delivery.jpg" class="d-block w-100"
                         alt="Giao hàng miễn phí">
                    <div class="carousel-caption  d-md-block">
                        <h2 class="fw-bold">Gọi Là Có – Giao Ngay Tận Cửa</h2>
                        <p>Miễn phí giao hàng toàn TP.HCM trong hôm nay!</p>
                        <a href="${pageContext.request.contextPath}/products"
                           class="btn btn-warning rounded-pill px-4">Đặt Ngay</a>
                    </div>
                </div>
            </div>

            <!-- Nút điều hướng -->
            <button class="carousel-control-prev" type="button" data-bs-target="#hero-carousel"
                    data-bs-slide="prev">
                <span class="carousel-control-prev-icon"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#hero-carousel"
                    data-bs-slide="next">
                <span class="carousel-control-next-icon"></span>
            </button>
        </section>

        <a href="${pageContext.request.contextPath}/products"
           class="btn btn-lg btn-primary-custom px-4 py-2 rounded-pill shadow animate__animated animate__fadeInUp">
            ĐẶT HÀNG NGAY
        </a>
    </div>
</section>

<section class="py-5 bg-light">
    <div class="container my-5">
        <div class="text-center mb-5 animate__animated animate__fadeIn">
            <h2 class="display-5 fw-bold text-success">
                SẢN PHẨM NỔI BẬT
            </h2>
            <p class="lead text-muted">
                Những lựa chọn được yêu thích nhất tại Juicy
            </p>
        </div>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
            <c:forEach items="${featuredList}" var="p">
                <div class="col">
                    <div class="card product-card h-100 text-center">
                        <img src="${p.img}" class="card-img-top" alt="${p.name}"
                             onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'"/>

                        <div class="card-body d-flex flex-column">
                            <h6 class="text-muted small">${p.volume}ml</h6>
                            <h5 class="card-title fw-bold fs-6">${p.name}</h5>
                            <p class="card-text text-danger fw-bold fs-5 my-2">
                                <fmt:formatNumber value="${p.price}" pattern="#,###" />.000đ
                            </p>
                            <div class="mt-auto pt-3">
                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}"
                                   class="btn btn-sm btn-outline-success rounded-pill px-3">Chi
                                    tiết</a>
                                <button type="button"
                                        class="btn btn-sm btn-primary-custom rounded-pill px-3 btn-add-to-cart"
                                        data-id="${p.id}">
                                    Thêm vào giỏ
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<section class="container-fluid bg-light py-5 mb-5">
    <div class="container">
        <h2 class="text-center mb-4 text-secondary fw-bold">
            VÌ SAO CHỌN JUICY?
        </h2>
        <div class="row text-center">
            <div class="col-md-4 mb-3">
                <div class="p-4 border rounded shadow-sm h-100">
                    <i class="bi bi-patch-check-fill text-success h1"></i>
                    <h3 class="text-success">
                        Tiêu Chuẩn GlobalGap
                    </h3>
                    <p>Nguyên liệu sạch, tươi, an toàn tuyệt đối, không chất bảo quản.
                    </p>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="p-4 border rounded shadow-sm h-100">
                    <i class="bi bi-truck text-warning h1"></i>
                    <h3 class="text-success">
                        Giao Hàng Tốc Độ
                    </h3>
                    <p>
                        Giao hàng nhanh trong ngày, bảo quản lạnh trọn độ tươi ngon.
                    </p>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="p-4 border rounded shadow-sm h-100">
                    <i class="bi bi-shop text-primary-custom h1"></i>
                    <h3 class="text-success">
                        Sản Phẩm Đa Dạng
                    </h3>
                    <p>
                        Đa dạng trái cây nội địa và nhập khẩu, nước ép và sinh tố tươi.
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- GIỚI THIỆU -->
<section id="gioi-thieu" class="about-section py-5">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6 mb-4 mb-md-0">
                <img src="images/banner/about-fruit.jpg" class="img-fluid rounded-4 shadow" alt="Giới thiệu Juicy">
            </div>
            <div class="col-md-6">
                <h2 class="fw-bold text-success mb-3">
                    Giới Thiệu Về Juicy 🍊
                </h2>
                <p class="lead">
                    <strong>Juicy</strong>là thương hiệu chuyên cung cấp
                    <em>trái cây tươi, nước ép lạnh, và sinh tố nguyên chất</em>,
                    mang đến nguồn dinh dưỡng lành mạnh từ thiên nhiên cho mọi gia đình Việt.
                </p>
                <p>Với cam kết “<strong>Tươi ngon – Tận tâm – Tự nhiên</strong>”, chúng tôi lựa chọn nguyên liệu từ các
                    nông trại đạt chuẩn
                    <span class="text-success fw-semibold">GlobalGAP</span>, đảm bảo an toàn và chất lượng cao nhất.
                </p>
                <ul class="list-unstyled">
                    <li>
                        <i class="bi bi-check-circle-fill text-success me-2"></i>100% trái cây sạch, không chất bảo quản
                    </li>
                    <li>
                        <i class="bi bi-check-circle-fill text-success me-2"></i>Giao hàng nhanh trong 2 giờ tại TP.HCM
                    </li>
                    <li>
                        <i class="bi bi-check-circle-fill text-success me-2"></i>Nước ép cold-press giữ nguyên dưỡng
                        chất
                    </li>
                </ul>
                <a href="about.html" class="btn btn-primary-custom mt-3">Khám phá thêm</a>
            </div>
        </div>
    </div>
    <c:if test="${not empty sessionScope.orderSuccess}">
    <script>
        // Hàm này đảm bảo mọi thứ (kể cả thư viện bên ngoài) đã load xong 100%
        window.onload = function() {
            if (typeof Swal !== 'undefined') {
                Swal.fire({
                    title: 'Đặt hàng thành công!',
                    text: '${sessionScope.orderSuccess}',
                    icon: 'success',
                    confirmButtonColor: '#198754'
                });
            } else {
                // Phương án dự phòng nếu CDN lỗi
                alert("${sessionScope.orderSuccess}");
            }
        };
    </script>
        <c:remove var="orderSuccess" scope="session" />
    </c:if>
<%@include file="/view/user/include/footer.jsp" %>
