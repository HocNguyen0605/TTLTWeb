<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="products" />
</jsp:include>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/productdetail.css">
<jsp:include page="/view/user/include/search-bar.jsp" />

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
                            <img src="${product.img}" class="img-fluid rounded" alt="${product.name}"
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
                    <fmt:formatNumber value="${product.price}" pattern="#,### đ" />
                </h2>

                <div class="mb-4">
                    <div class="mb-2 text-muted">
                        Dung tích:
                        <c:choose>
                            <c:when test="${not empty groupProducts}">
                                <div class="d-flex flex-wrap gap-2 mt-2 mb-2">
                                    <c:forEach var="gp" items="${groupProducts}">
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${gp.id}"
                                           class="btn ${gp.id == product.id ? 'btn-success' : 'btn-outline-success'} btn-sm rounded-pill px-3 fw-bold">
                                                ${gp.volume} ml
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <span class="text-dark fw-bold">${product.volume} ml</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
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

                <form action="${pageContext.request.contextPath}/cart" method="POST" class="mb-4">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.id}">
                    <div class="d-flex align-items-center gap-3">
                        <div class="input-group" style="width: 130px;">
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="this.parentNode.querySelector('input').stepDown()">-
                            </button>
                            <input type="number" name="quantity" class="form-control text-center" value="1"
                                   min="1">
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

    <%-- SECTION THÔNG TIN SẢN PHẨM --%>
    <div class="mt-5 mb-5">
        <h5 class="fw-bold text-success text-uppercase mb-4">Thông tin sản phẩm</h5>
        <div class="row">
            <div class="col-md-8">
                <div class="card p-4 border rounded shadow-sm border-0"
                     style="border: 1px solid #dee2e6 !important;">
                    <h6 class="fw-bold mb-3 fs-5">Mô tả sản phẩm</h6>
                    <div class="text-muted leading-relaxed text-break">
                        ${product.description != null ? product.description : "Chưa có mô tả cho sản phẩm này. Nước ép Juicy cam kết 100% nguyên chất, không đường hóa học."}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%-- SECTION ĐÁNH GIÁ SẢN PHẨM --%>
    <div class="mt-5 mb-5 border-top pt-5">
        <h5 class="fw-bold text-success text-uppercase mb-4">Đánh giá sản phẩm</h5>
        <div class="row">
            <div class="col-md-5">
                <div class="card border-0 shadow-sm p-4"
                     style="border: 1px solid #dee2e6 !important;">
                    <h6 class="fw-bold mb-3">Gửi đánh giá của bạn</h6>

                    <c:choose>
                        <c:when test="${empty auth}">
                            <div class="text-center py-3">
                                <p class="text-muted mb-3">Vui lòng đăng nhập để gửi đánh giá cho sản phẩm này.</p>
                                <a href="${pageContext.request.contextPath}/login?returnUrl=${pageContext.request.requestURL}?${pageContext.request.queryString}"
                                   class="btn btn-outline-success btn-sm rounded-pill px-4">Đăng nhập ngay</a>
                            </div>
                        </c:when>
                        <c:when test="${not canReview}">
                            <div class="alert alert-info border-0 shadow-sm" style="background-color: #f8f9fa;">
                                <i class="bi bi-info-circle-fill text-info me-2"></i>
                                <small class="text-muted">Bạn chỉ có thể đánh giá sản phẩm này sau khi đã mua và nhận hàng thành công.</small>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form id="reviewForm" action="${pageContext.request.contextPath}/submit-review"
                                  method="POST">
                                <input type="hidden" name="productId" value="${product.id}">
                                <input type="hidden" name="rating" id="ratingValue" value="5">
                                <div class="mb-3 text-warning fs-4 d-flex gap-1" id="starRating">
                                    <i class="bi bi-star-fill" data-value="1" style="cursor: pointer;"></i>
                                    <i class="bi bi-star-fill" data-value="2" style="cursor: pointer;"></i>
                                    <i class="bi bi-star-fill" data-value="3" style="cursor: pointer;"></i>
                                    <i class="bi bi-star-fill" data-value="4" style="cursor: pointer;"></i>
                                    <i class="bi bi-star-fill" data-value="5" style="cursor: pointer;"></i>
                                </div>
                                <div class="mb-3">
                                    <textarea id="reviewContent" name="content" class="form-control"
                                              rows="4" placeholder="Chia sẻ cảm nhận của bạn về sản phẩm này..."
                                              required></textarea>
                                </div>
                                <button type="submit" class="btn btn-success fw-bold px-4 rounded-pill">Gửi
                                    Đánh Giá</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="col-md-7 mt-4 mt-md-0">
                <div class="card border-0 shadow-sm p-4"
                     style="border: 1px solid #dee2e6 !important;">
                    <h6 class="fw-bold mb-4">Các lượt đánh giá (${reviews != null ? reviews.size() :
                            0})</h6>

                    <!-- RATING SUMMARY & FILTER (SHOPEE STYLE) -->
                    <div class="rating-summary-card">
                        <div class="text-center pe-4 border-end" style="min-width: 150px;">
                            <div class="rating-score">
                                <fmt:formatNumber value="${avgRating != null ? avgRating : 0}"
                                                  pattern="0.0" />
                                <span class="fs-6 fw-normal text-muted"> trên 5</span>
                            </div>
                            <div class="rating-stars-main">
                                <c:forEach begin="1" end="5" var="i">
                                    <i
                                            class="bi bi-star${i <= avgRating ? '-fill' : (i - 0.5 <= avgRating ? '-half' : '')}"></i>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="ps-3 d-flex flex-wrap" id="reviewFilter">
                            <button class="filter-btn active" data-filter="all">Tất Cả</button>
                            <button class="filter-btn" data-filter="5">5 Sao
                                (${starCounts[5]})</button>
                            <button class="filter-btn" data-filter="4">4 Sao
                                (${starCounts[4]})</button>
                            <button class="filter-btn" data-filter="3">3 Sao
                                (${starCounts[3]})</button>
                            <button class="filter-btn" data-filter="2">2 Sao
                                (${starCounts[2]})</button>
                            <button class="filter-btn" data-filter="1">1 Sao
                                (${starCounts[1]})</button>
                            <button class="filter-btn" data-filter="has-comment">Có Bình Luận
                                (${commentCount})</button>
                        </div>
                    </div>

                    <div class="review-list">
                        <c:forEach var="r" items="${reviews}">
                            <div class="review-item mb-4 pb-3 border-bottom"
                                 data-rating="${r.rating}" data-has-comment="${not empty r.content}">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold text-dark">${r.userName}</span>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${r.createdAt}"
                                                        pattern="dd/MM/yyyy HH:mm" />
                                    </small>
                                </div>
                                <div class="text-warning mb-2" style="color: #ee4d2d !important;">
                                    <c:forEach begin="1" end="${r.rating}">
                                        <i class="bi bi-star-fill"></i>
                                    </c:forEach>
                                    <c:forEach begin="${r.rating + 1}" end="5">
                                        <i class="bi bi-star"></i>
                                    </c:forEach>
                                </div>
                                <p class="text-muted mb-0">${r.content}</p>


                                <c:if test="${not empty r.sellerReply}">
                                    <div class="seller-reply-box">
                                        <div class="title">Phản hồi từ Người bán</div>
                                        <div class="text-dark" style="font-size: 0.95rem;">
                                                ${r.sellerReply}</div>
                                    </div>
                                </c:if>

                                <div class="d-flex align-items-center mt-2">
                                    <button class="like-btn ${r.hasLiked ? 'liked' : ''}"
                                            data-review-id="${r.id}"
                                            onclick="toggleLike(this, ${r.id})">
                                        <i
                                                class="bi bi-hand-thumbs-up${r.hasLiked ? '-fill' : ''}"></i>
                                        <span>Hữu ích (<span
                                                class="like-count">${r.likes}</span>)</span>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty reviews}">
                            <div class="text-center py-5">
                                <i class="bi bi-chat-square-text text-muted"
                                   style="font-size: 3rem;"></i>
                                <p class="text-muted mt-3 mb-0">Chưa có đánh giá nào cho sản phẩm
                                    này.</p>
                            </div>
                        </c:if>
                    </div>
                </div>
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
                    <div class="card product-card h-100 text-center" style="cursor: pointer;"
                         onclick="if(!event.target.closest('.btn')) window.location.href='${pageContext.request.contextPath}/product-detail?id=${rp.id}'">
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
                            <div class="mt-auto pt-3 position-relative" style="z-index: 2;">
                                <a href="${pageContext.request.contextPath}/product-detail?id=${rp.id}"
                                   class="btn btn-sm btn-outline-success rounded-pill px-3">Chi
                                    tiết</a>
                                <button type="button"
                                        class="btn btn-sm btn-primary-custom rounded-pill px-3 btn-add-to-cart"
                                        data-id="${rp.id}">
                                    Thêm vào giỏ
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

</main>

<script>
    const CONFIG = {
        productId: "${product.id}",
        contextPath: "${pageContext.request.contextPath}",
        isAuthenticated: ${not empty auth}
    };
</script>
<script src="${pageContext.request.contextPath}/js/productdetail.js"></script>

<%@include file="/view/user/include/footer.jsp" %>
