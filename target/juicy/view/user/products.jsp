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

    .btn-primary-custom {
        background-color: #28a745;
        color: white;
        border: none;
    }

    .card-img-top {
        height: 220px;
        object-fit: contain;
        background-color: #fff;
        padding: 10px;
    }

    /* --- PHÂN TRANG CIRCULAR --- */
    .pagination-custom .page-item {
        margin: 0 5px;
    }

    .pagination-custom .page-link {
        width: 40px;
        height: 40px;
        border-radius: 50% !important;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #dee2e6;
        color: #495057;
        font-weight: 500;
        transition: all 0.3s;
    }

    .pagination-custom .page-item.active .page-link {
        background-color: #0d3d28;
        /* Dark green matching request */
        border-color: #0d3d28;
        color: white;
        box-shadow: 0 4px 8px rgba(13, 61, 40, 0.3);
    }

    .pagination-custom .page-link:hover:not(.active) {
        background-color: #f8f9fa;
        border-color: #0d3d28;
        color: #0d3d28;
    }
</style>
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

<div class="container my-5">
    <div class="row">
        <%-- SIDEBAR BỘ LỌC --%>
        <div class="col-lg-3">
            <div class="filter-sidebar">
                <div class="filter-header">
                    <h5 class="fw-bold text-success mb-0"><i class="bi bi-funnel-fill me-2"></i>Bộ
                        Lọc</h5>
                </div>
                <div class="filter-body">
                    <form action="products" method="get">
                        <%-- Lọc theo Giá --%>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark small text-uppercase">Khoảng giá</label>
                            <div class="input-group input-group-sm mb-2">
                                <span class="input-group-text bg-white">₫</span>
                                <input type="number" name="minPrice" class="form-control" placeholder="Từ"
                                       value="${currentMinPrice}">
                            </div>
                            <div class="input-group input-group-sm">
                                <span class="input-group-text bg-white">₫</span>
                                <input type="number" name="maxPrice" class="form-control" placeholder="Đến"
                                       value="${currentMaxPrice}">
                            </div>
                        </div>

                        <%-- Lọc theo Thể Tích --%>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark small text-uppercase">Thể tích</label>
                            <select name="volume" class="form-select form-select-sm">
                                <option value="">Tất cả</option>
                                <c:forEach items="${volumeList}" var="vol">
                                    <option value="${vol}" ${currentVolume==vol ? 'selected': '' }>${vol} ml
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <%-- Lọc theo Nhà cung cấp --%>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark small text-uppercase">Nhà cung cấp</label>
                            <select name="supplier" class="form-select form-select-sm">
                                <option value="">Tất cả</option>
                                <c:forEach items="${supplierList}" var="sup">
                                    <option value="${sup}" ${currentSupplier==sup? 'selected' : '' }>${sup}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <%-- Sắp xếp --%>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark small text-uppercase">Sắp xếp</label>
                            <select name="sort" class="form-select form-select-sm">
                                <option value="">Mặc định</option>
                                <option value="priceAsc" ${currentSort=='priceAsc'? 'selected' : '' }>Giá tăng dần
                                </option>
                                <option value="priceDesc" ${currentSort=='priceDesc'? 'selected' : '' }>Giá giảm dần
                                </option>
                                <option value="nameAsc" ${currentSort=='nameAsc'? 'selected' : '' }>Tên A-Z
                                </option>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary-custom w-100 fw-bold py-2 rounded-pill shadow-sm">
                            <i class="bi bi-check2-circle me-1"></i> Áp Dụng
                        </button>
                        <div class="text-center mt-3">
                            <a href="products" class="text-secondary text-decoration-none small hover-link">
                                <i class="bi bi-x-circle me-1"></i> Xóa bộ lọc
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <%-- DANH SÁCH SẢN PHẨM --%>
        <div class="col-lg-9">
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:if test="${empty productList}">
                    <div class="col-12 text-center py-5">
                        <h4 class="text-muted">Không tìm thấy sản phẩm nào phù hợp.</h4>
                    </div>
                </c:if>

                <c:forEach items="${productList}" var="p">
                    <div class="col">
                        <div class="card product-card h-100 text-center">
                            <div class="product-img-wrapper"
                                 style="height: 250px; overflow: hidden;">
                                <c:choose>
                                    <c:when test="${p.img != null && p.img.contains('http')}">
                                        <img src="${p.img}" class="card-img-top h-100 w-100"
                                             style="object-fit: cover;" alt="${p.name}"
                                             onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                    </c:when>
                                    <c:when
                                            test="${p.img != null && (p.img.contains('/') || p.img.contains('\\\\'))}">
                                        <img src="${pageContext.request.contextPath}/${p.img}"
                                             class="card-img-top h-100 w-100"
                                             style="object-fit: cover;" alt="${p.name}"
                                             onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/product/${p.img}"
                                             class="card-img-top h-100 w-100"
                                             style="object-fit: cover;" alt="${p.name}"
                                             onerror="this.src='${pageContext.request.contextPath}/images/logo/logo-juicy.png'">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="card-body d-flex flex-column">
                                <h6 class="text-muted small">${p.volume}ml</h6>
                                <h5 class="card-title fw-bold fs-6">${p.name}</h5>
                                <p class="card-text text-danger fw-bold fs-5 my-2">
                                    <fmt:formatNumber value="${p.price}" type="currency"
                                                      currencySymbol="đ" maxFractionDigits="0"/>
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

            <%-- PHÂN TRANG UI --%>
            <c:if test="${totalPages > 1}">
                <nav aria-label="Product pagination" class="mt-5">
                    <ul class="pagination pagination-custom justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="?page=1&minPrice=${currentMinPrice}&maxPrice=${currentMaxPrice}&volume=${currentVolume}&supplier=${currentSupplier}&sort=${currentSort}"
                               title="Trang đầu">
                                <i class="bi bi-chevron-double-left small"></i>
                            </a>
                        </li>

                        <c:forEach begin="${currentPage - 2 < 1 ? 1 : currentPage - 2}"
                                   end="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}"
                                   var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link"
                                   href="?page=${i}&minPrice=${currentMinPrice}&maxPrice=${currentMaxPrice}&volume=${currentVolume}&supplier=${currentSupplier}&sort=${currentSort}">${i}</a>
                            </li>
                        </c:forEach>

                        <li
                                class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="?page=${totalPages}&minPrice=${currentMinPrice}&maxPrice=${currentMaxPrice}&volume=${currentVolume}&supplier=${currentSupplier}&sort=${currentSort}"
                               title="Trang cuối">
                                <i class="bi bi-chevron-double-right small"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>
<%@include file="/view/user/include/footer.jsp" %>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>