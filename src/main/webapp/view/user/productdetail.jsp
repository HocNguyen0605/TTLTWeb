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

    /* Custom Scrollbar for Review List */
    .review-list {
        max-height: 400px;
        overflow-y: auto;
        padding-right: 15px;
        scrollbar-width: thin;
        scrollbar-color: #28a745 transparent;
    }

    .review-list::-webkit-scrollbar {
        width: 5px;
    }

    .review-list::-webkit-scrollbar-track {
        background: transparent;
        border-radius: 10px;
    }

    .review-list::-webkit-scrollbar-thumb {
        background: #28a745;
        border-radius: 10px;
    }

    .review-list::-webkit-scrollbar-thumb:hover {
        background: #218838;
    }
</style>
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
                    <p class="mb-1 text-muted">Dung tích: <span class="text-dark fw-bold">${product.volume}
                                        ml</span>
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
                <div class="card border-0 shadow-sm p-4" style="border: 1px solid #dee2e6 !important;">
                    <h6 class="fw-bold mb-3">Gửi đánh giá của bạn</h6>
                    <form id="reviewForm" action="${pageContext.request.contextPath}/submit-review" method="POST">
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
                            <textarea id="reviewContent" name="content" class="form-control" rows="4" placeholder="Chia sẻ cảm nhận của bạn về sản phẩm này..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-success fw-bold px-4 rounded-pill">Gửi Đánh Giá</button>
                    </form>
                </div>
            </div>
            <div class="col-md-7 mt-4 mt-md-0">
                <div class="card border-0 shadow-sm p-4" style="border: 1px solid #dee2e6 !important;">
                    <h6 class="fw-bold mb-4">Các lượt đánh giá (${reviews != null ? reviews.size() : 0})</h6>
                    <div class="review-list">
                        <c:forEach var="r" items="${reviews}">
                            <div class="review-item mb-4 pb-3 border-bottom">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold text-dark">${r.userName}</span>
                                    <small class="text-muted"><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                                </div>
                                <div class="text-warning mb-2">
                                    <c:forEach begin="1" end="${r.rating}">
                                        <i class="bi bi-star-fill"></i>
                                    </c:forEach>
                                    <c:forEach begin="${r.rating + 1}" end="5">
                                        <i class="bi bi-star"></i>
                                    </c:forEach>
                                </div>
                                <p class="text-muted mb-0">${r.content}</p>
                            </div>
                        </c:forEach>
                        <c:if test="${empty reviews}">
                            <div class="text-center py-5">
                                <i class="bi bi-chat-square-text text-muted" style="font-size: 3rem;"></i>
                                <p class="text-muted mt-3 mb-0">Chưa có đánh giá nào cho sản phẩm này.</p>
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
                        <div class="product-img-wrapper" style="height: 250px; overflow: hidden;">
                            <c:choose>
                                <c:when test="${rp.img != null && rp.img.contains('http')}">
                                    <img src="${rp.img}" class="card-img-top h-100 w-100"
                                         style="object-fit: cover;" alt="${rp.name}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                </c:when>
                                <c:when
                                        test="${rp.img != null && (rp.img.contains('/') || rp.img.contains('\\\\'))}">
                                    <img src="${pageContext.request.contextPath}/${rp.img}"
                                         class="card-img-top h-100 w-100" style="object-fit: cover;"
                                         alt="${rp.name}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/product/${rp.img}"
                                         class="card-img-top h-100 w-100" style="object-fit: cover;"
                                         alt="${rp.name}"
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
    document.addEventListener("DOMContentLoaded", function () {
        const stars = document.querySelectorAll('#starRating i');
        const ratingInput = document.getElementById('ratingValue');
        const reviewForm = document.getElementById('reviewForm');
        const reviewContent = document.getElementById('reviewContent');
        const productId = "${product.id}";

        // 1. Logic chọn sao
        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                const val = star.getAttribute('data-value');
                ratingInput.value = val;

                stars.forEach((s, i) => {
                    if (i <= index) {
                        s.classList.remove('bi-star');
                        s.classList.add('bi-star-fill');
                    } else {
                        s.classList.remove('bi-star-fill');
                        s.classList.add('bi-star');
                    }
                });
            });
        });

        // 2. Kiểm tra xem có review đang chờ từ localStorage không
        const pendingReview = JSON.parse(localStorage.getItem('pendingReview'));
        if (pendingReview && pendingReview.productId === productId) {
            reviewContent.value = pendingReview.content;
            ratingInput.value = pendingReview.rating;
            // Cập nhật giao diện sao
            stars.forEach((s, i) => {
                if (i < pendingReview.rating) {
                    s.classList.remove('bi-star');
                    s.classList.add('bi-star-fill');
                } else {
                    s.classList.remove('bi-star-fill');
                    s.classList.add('bi-star');
                }
            });
            localStorage.removeItem('pendingReview');

            // Nếu đã đăng nhập thì tự động kích hoạt submit
            <c:if test="${not empty auth}">
            setTimeout(() => {
                reviewForm.querySelector('button[type="submit"]').click();
            }, 500);
            </c:if>
        }

        // 3. Xử lý khi nhấn Gửi Đánh Giá
        if (reviewForm) {
            reviewForm.addEventListener('submit', function(e) {
                e.preventDefault();

                <c:if test="${empty auth}">
                // Lưu vào localStorage nếu chưa đăng nhập
                const data = {
                    productId: productId,
                    rating: ratingInput.value,
                    content: reviewContent.value
                };
                localStorage.setItem('pendingReview', JSON.stringify(data));

                // Chuyển hướng đến trang login với returnUrl
                const currentUrl = window.location.href;
                window.location.href = "${pageContext.request.contextPath}/login?returnUrl=" + encodeURIComponent(currentUrl);
                return;
                </c:if>

                // Nếu đã đăng nhập, gửi AJAX
                const formData = new FormData(reviewForm);
                const searchParams = new URLSearchParams(formData);

                fetch("${pageContext.request.contextPath}/submit-review", {
                    method: 'POST',
                    body: searchParams,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'success') {
                            // Thông báo thành công bằng SweetAlert2
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: data.message,
                                timer: 2000,
                                showConfirmButton: false
                            });

                            // Thêm đánh giá mới vào UI mà không reload
                            const reviewList = document.querySelector('.review-list');
                            const emptyMsg = reviewList.querySelector('.text-center.py-5');
                            if (emptyMsg) emptyMsg.remove();

                            const r = data.review;
                            const dateStr = new Date(r.createdAt).toLocaleString('vi-VN', {
                                day: '2-digit', month: '2-digit', year: 'numeric',
                                hour: '2-digit', minute: '2-digit'
                            }).replace(',', '');

                            let starsHtml = '';
                            for (let i = 1; i <= 5; i++) {
                                starsHtml += `<i class="bi bi-star${i <= r.rating ? '-fill' : ''}"></i> `;
                            }

                            const newReviewHtml = `
                            <div class="review-item mb-4 pb-3 border-bottom animate__animated animate__fadeIn">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold text-dark">${r.userName}</span>
                                    <small class="text-muted">${dateStr}</small>
                                </div>
                                <div class="text-warning mb-2">${starsHtml}</div>
                                <p class="text-muted mb-0">${r.content}</p>
                            </div>
                        `;

                            reviewList.insertAdjacentHTML('afterbegin', newReviewHtml);
                            reviewForm.reset();
                            ratingInput.value = 5;
                            stars.forEach(s => {
                                s.classList.remove('bi-star');
                                s.classList.add('bi-star-fill');
                            });

                            // Cập nhật số lượng đánh giá trên UI
                            const countEl = document.querySelector('h6.mb-4');
                            if (countEl) {
                                const match = countEl.innerText.match(/\((\d+)\)/);
                                if (match) {
                                    const newCount = parseInt(match[1]) + 1;
                                    countEl.innerText = `Các lượt đánh giá (${newCount})`;
                                }
                            }
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: data.message
                            });
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Đã xảy ra lỗi khi gửi đánh giá.'
                        });
                    });
            });
        }
    });
</script>

<%@include file="/view/user/include/footer.jsp" %>