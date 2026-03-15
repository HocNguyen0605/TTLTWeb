<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="products" />
</jsp:include>
<style>
    .product-card {
        transition: transform 0.3s;
        border: none;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        border-radius: 15px;
        overflow: hidden;
    }

    .product-card:hover {
        transform: translateY(-5px);
    }

    .card-img-top {
        height: 220px;
        object-fit: contain;
        background-color: #fff;
        padding: 10px;
    }

    .btn-primary-custom {
        background-color: #28a745;
        color: white;
        border: none;
    }
</style>
<%-- SECTION TÌM KIẾM --%>
<section class="bg-light py-4 border-bottom">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <form class="d-flex" action="${pageContext.request.contextPath}/search" method="get">
                    <input class="form-control form-control-lg me-2 border-success" type="search" name="query"
                           placeholder="Tìm kiếm tên sản phẩm, loại trái cây..." aria-label="Search">
                    <button class="btn btn-primary-custom btn-lg fw-bold" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>

                    <main class="container my-5">
                        <nav aria-label="breadcrumb" class="mb-4">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/"
                                        class="text-success text-decoration-none">Trang chủ</a></li>
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product"
                                        class="text-success text-decoration-none">Sản phẩm</a></li>
                                <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
                            </ol>
                        </nav>

                        <div class="row g-5">
                            <div class="col-md-6 animate__animated animate__fadeInLeft">
                                <div class="card border-0 shadow-sm overflow-hidden text-center p-3 bg-white">
                                    <div class="product-img-wrapper"
                                        style="width: 100%; height: 500px; display: flex; align-items: center; justify-content: center;">
                                        <c:choose>
                                            <c:when test="${product.img != null && product.img.contains('http')}">
                                                <img src="${product.img}" class="img-fluid rounded"
                                                    alt="${product.name}"
                                                    style="max-width: 100%; max-height: 100%; object-fit: contain;"
                                                    onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                            </c:when>
                                            <c:when
                                                test="${product.img != null && (product.img.contains('/') || product.img.contains('\\\\'))}">
                                                <img src="${pageContext.request.contextPath}/${product.img}"
                                                    class="img-fluid rounded" alt="${product.name}"
                                                    style="max-width: 100%; max-height: 100%; object-fit: contain;"
                                                    onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/product/${product.img}"
                                                    class="img-fluid rounded" alt="${product.name}"
                                                    style="max-width: 100%; max-height: 100%; object-fit: contain;"
                                                    onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6 animate__animated animate__fadeInRight">
                                <div class="product-info-detail">
                                    <span class="badge bg-success mb-2">Nước ép tươi mỗi ngày</span>
                                    <h1 class="display-5 fw-bold text-dark mb-3">${product.name}</h1>

                                    <h2 class="text-danger fw-bold mb-4">
                                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="đ"
                                            maxFractionDigits="0" />
                                    </h2>

                                    <div class="mb-4">
                                        <p class="mb-1 text-muted">Dung tích: <span
                                                class="text-dark fw-bold">${product.volume} ml</span>
                                        </p>
                                        <p class="mb-1 text-muted">Nhà cung cấp: <span
                                                class="text-dark fw-bold">${product.supplier_name}</span></p>
                                        <p class="mb-1 text-muted">Tình trạng:
                                            <c:choose>
                                                <c:when test="${product.quantity > 0}">
                                                    <span class="text-success fw-bold">Còn hàng
                                                        (${product.quantity})</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-danger fw-bold">Hết hàng</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>

                <div class="mb-4 pb-4 border-bottom">
                    <h5 class="fw-bold">Mô tả sản phẩm:</h5>
                    <p class="text-muted leading-relaxed">
                        ${product.description != null ? product.description : "Chưa có mô tả cho sản phẩm này. Nước ép Juicy cam kết 100% nguyên chất, không đường hóa học."}
                    </p>
                </div>

                                    <form action="add-to-cart" method="POST">
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <div class="d-flex align-items-center gap-3 mb-4">
                                            <div class="input-group" style="width: 130px;">
                                                <button class="btn btn-outline-secondary" type="button"
                                                    onclick="this.parentNode.querySelector('input').stepDown()">-
                                                </button>
                                                <input type="number" name="quantity" class="form-control text-center"
                                                    value="1" min="1">
                                                <button class="btn btn-outline-secondary" type="button"
                                                    onclick="this.parentNode.querySelector('input').stepUp()">+
                                                </button>
                                            </div>
                                            <button type="submit"
                                                class="btn btn-success btn-lg px-4 flex-grow-1 fw-bold rounded-pill">
                                                <i class="bi bi-cart-plus me-2"></i> THÊM VÀO GIỎ HÀNG
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <%-- SECTION SẢN PHẨM LIÊN QUAN --%>
                            <div class="mt-5 pt-5 border-top">
                                <h3 class="fw-bold text-success mb-4 text-center text-uppercase">Có thể bạn sẽ thích
                                </h3>
                                <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4">
                                    <c:forEach items="${relatedProducts}" var="rp">
                                        <div class="col">
                                            <div class="card product-card h-100 text-center">
                                                <div class="product-img-wrapper"
                                                    style="height: 250px; overflow: hidden;">
                                                    <c:choose>
                                                        <c:when test="${rp.img != null && rp.img.contains('http')}">
                                                            <img src="${rp.img}" class="card-img-top h-100 w-100"
                                                                style="object-fit: cover;" alt="${rp.name}"
                                                                onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                                        </c:when>
                                                        <c:when
                                                            test="${rp.img != null && (rp.img.contains('/') || rp.img.contains('\\\\'))}">
                                                            <img src="${pageContext.request.contextPath}/${rp.img}"
                                                                class="card-img-top h-100 w-100"
                                                                style="object-fit: cover;" alt="${rp.name}"
                                                                onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/images/product/${rp.img}"
                                                                class="card-img-top h-100 w-100"
                                                                style="object-fit: cover;" alt="${rp.name}"
                                                                onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>

                                                <div class="card-body d-flex flex-column">
                                                    <h6 class="text-muted small">${rp.volume}ml</h6>
                                                    <h5 class="card-title fw-bold fs-6">${rp.name}</h5>
                                                    <p class="card-text text-danger fw-bold fs-5 my-2">
                                                        <fmt:formatNumber value="${rp.price}" type="currency"
                                                            currencySymbol="đ" maxFractionDigits="0" />
                                                    </p>
                                                    <div class="mt-auto pt-3">
                                                        <a href="${pageContext.request.contextPath}/product-detail?id=${rp.id}"
                                                            class="btn btn-sm btn-outline-success rounded-pill px-3">Chi
                                                            tiết</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

</main>
<%@include file="/view/user/include/footer.jsp" %>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/init.js"></script>
</body>
</html>