<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="images/png" href="images/logo/logo-juicy.png" sizes="32x32">
    <link rel="shortcut icon" href="images/logo/logo-juicy.png" type="image/png">
    <title>Juicy - Nước Ép Tươi Ngon &amp; Healthy</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/product.css">
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/about.css">

</head>

<body>
<!-- HEADER -->
<header class="sticky-top shadow-sm">
    <nav class="navbar navbar-expand-lg navbar-light bg-white py-3">
        <div class="container">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand fw-bold text-success fs-3"
               href="${pageContext.request.contextPath}/home">
                <img src="images/logo/logo-juicy.png" alt="Juicy Logo" height="40" class="me-2">
                JUICY <span class="text-warning"></span>
            </a>

            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto align-items-lg-center">
                    <li class="nav-item">
                        <a class="nav-link ${param.activePage == 'home' ? 'active' : ''} fw-semibold"
                           href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.activePage == 'products' ? 'active' : ''} fw-semibold"
                           href="${pageContext.request.contextPath}/products">Sản phẩm</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.activePage == 'about' ? 'active' : ''} fw-semibold"
                           href="${pageContext.request.contextPath}/about">Giới thiệu</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.activePage == 'contact' ? 'active' : ''} fw-semibold"
                           href="${pageContext.request.contextPath}/contact">Liên hệ</a>
                    </li>
                </ul>
                <c:choose>
                    <c:when test="${not empty sessionScope.auth}">
                        <div id="userInfoContainer">
                            <a href="${pageContext.request.contextPath}/profile"
                               class="btn btn-warning rounded-pill ms-lg-3 my-2 my-lg-0 fw-semibold shadow-sm">
                                Thông Tin</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="loginButtonContainer">
                            <a href="${pageContext.request.contextPath}/login"
                               class="btn btn-warning rounded-pill ms-lg-3 my-2 my-lg-0 fw-semibold shadow-sm">
                                Đăng Nhập</a>
                        </div>

                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/cart"
                   class="btn btn-warning rounded-pill ms-lg-3 my-2 my-lg-0 fw-semibold shadow-sm position-relative">
                    <i class="bi bi-cart me-1"></i> Giỏ Hàng
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalItems > 0}">
        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger border border-light">
            ${sessionScope.cart.totalItems}
             <span class="visually-hidden">sản phẩm</span>
         </span>
                    </c:if>
                </a>
                </a>
            </div>
        </div>
    </nav>
</header>
