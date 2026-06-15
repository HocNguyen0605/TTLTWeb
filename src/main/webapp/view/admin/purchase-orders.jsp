<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Đặt đơn hàng | Juicy</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/purchase-order.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/purchase-orders.js" defer></script>
    <script>window.contextPath = "${pageContext.request.contextPath}"; </script>
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
                <a class="nav-link text-white " href="#menuQL" data-bs-toggle="collapse" aria-expanded="true">
                    b. Quản lý
                </a>
                <ol class="collapse show" id="menuQL">
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('products') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/products">
                            Quản lý sản phẩm </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3" href="${pageContext.request.contextPath}/admin/banner">
                            Quản lý banner </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3" href="${pageContext.request.contextPath}/admin/CTKM">
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
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('contacts') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/contacts">
                            Quản lý Liên hệ </a>
                    </li>
                </ol>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white ${pageContext.request.requestURI.contains('purchase-orders') ? 'active' : ''}"
                   href="#menuKV" data-bs-toggle="collapse">
                    c. Quản lý kho vận
                </a>
                <ol class="collapse show" id="menuKV">
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('purchase-orders') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/purchase-orders">
                            Đặt đơn hàng </a>
                    </li>
                    <li>
                        <a class="nav-link text-white ms-3 ${pageContext.request.requestURI.contains('purchase-orders/confirm') ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/admin/purchase-orders/confirm">
                            Xác nhận đơn hàng </a>
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
    <div class="container my-5" style="overflow-y: auto;">

        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
            <h4 class="mb-0">Đặt đơn hàng - Chờ nhà cung cấp xác nhận</h4>

            <div class="d-flex gap-2">
                <div class="d-flex gap-3 mt-3 mt-md-0 align-items-center">
                    <div class="search-container d-none d-md-block" style="width: 320px;">
                        <form action="/admin/purchase-orders" method="get" class="input-group">
                            <input type="text" name="search" class="form-control search-input"
                                   placeholder="Nhập id đơn hàng" value="${currentSearch}">
                            <button type="submit" class="btn btn-success d-flex align-items-center">
                                <i class="bi bi-search search-icon"></i>
                            </button>
                        </form>
                    </div>
                </div>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addOrderModal">
                    <i class="bi bi-plus-circle"></i> Tạo đơn hàng mới
                </button>
            </div>
        </div>

        <div class="shadow p-3 bg-white rounded">
            <div class="table-responsive">
                <table class="table table-bordered align-middle" id="ordersTable">
                    <thead class="table-light">
                    <tr>
                        <th>Mã đơn</th>
                        <th>Nhà cung cấp</th>
                        <th>Ngày tạo</th>
                        <th>Số sản phẩm</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="o" items="${pendingOrders}">
                        <tr data-code="${o.orderId}">
                            <td>#${o.orderId}</td>
                            <td>${o.supplierName}</td>
                            <td><fmt:formatDate value="${o.createdDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>${o.itemCount}</td>
                            <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/></td>
                            <td><span class="badge bg-warning text-dark">Chờ NCC xác nhận</span></td>
                            <td>
                                <button type="button"
                                        class="btn btn-sm btn-outline-primary"
                                        data-bs-toggle="modal"
                                        data-bs-target="#checkOutPurchase"
                                        data-id="${o.orderId}">
                                    Xem chi tiết
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty pendingOrders}">
                        <tr>
                            <td colspan="7" class="text-center text-muted">
                                Hiện không có đơn đặt hàng nào đang chờ nhà cung cấp xác nhận.
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>

<!-- FOOTER -->
<footer class="bg-dark text-white text-center py-3">
    © 2024 Juicy. All Rights Reserved.
</footer>

<!--MODAL TẠO ĐƠN HÀNG  -->
<div class="modal fade" id="addOrderModal" tabindex="-1" aria-labelledby="addOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <form id="addOrderForm" action="${pageContext.request.contextPath}/admin/purchase-orders" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-header">
                    <h5 class="modal-title" id="addOrderModalLabel">Tạo đơn đặt hàng mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <label class="form-label">Tìm sản phẩm</label>
                                <input type="text" id="modalProductSearch" class="form-control"
                                       placeholder="Nhập tên sản phẩm...">
                            </div>
                            <label class="form-label">Nhà cung cấp</label>
                            <select id="orderSupplier" name="supplierId" class="form-select" required>
                                <option value="">-- Chọn nhà cung cấp --</option>
                                <c:forEach var="s" items="${allSuppliers}">
                                    <option value="${s.supplierId}">${s.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <!-- Danh sách sản phẩm để chọn -->
                        <div class="col-md-6">
                            <h6 class="mb-2">Danh sách sản phẩm</h6>
                            <div id="productList" class="border rounded p-2" style="max-height:300px; overflow-y:auto;">
                                <c:forEach var="p" items="${availableProducts}">
                                    <div class="product-item d-flex justify-content-between align-items-center border-bottom py-2"
                                         data-id="${p.id}"
                                         data-name="${p.name}"
                                         data-price="${p.price}"
                                         data-quantity="${p.quantity}">

                                        <div class="flex-grow-1 me-3">
                                            <div class="row align-items-center">
                                                <div class="col-8">
                                                    <div class="fw-semibold text-wrap">${p.name}</div>
                                                    <small class="text-muted">
                                                        <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫"/>
                                                    </small>
                                                </div>

                                                <div class="col-4 text-end">
                                                    <span class="text-danger fw-bold">SL: ${p.quantity}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn btn-sm btn-outline-success btn-add-product">
                                            <i class="bi bi-plus-lg"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty availableProducts}">
                                    <p class="text-muted text-center py-3 mb-0">Không có sản phẩm.</p>
                                </c:if>
                            </div>
                        </div>

                        <!-- Sản phẩm đã chọn -->
                        <div class="col-md-6">
                            <h6 class="mb-2">Sản phẩm đã chọn</h6>
                            <div class="table-responsive border rounded">
                                <table class="table table-sm align-middle mb-0" id="cartTable">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-end">Giá nhập</th>
                                        <th class="text-center" style="width:120px;">Số lượng</th>
                                        <th class="text-end">Tạm tính</th>
                                        <th style="width:50px;"></th>
                                    </tr>
                                    </thead>
                                    <tbody id="cartBody"></tbody>
                                </table>
                                <p id="cartEmpty" class="text-muted text-center py-3 mb-0">
                                    Chưa có sản phẩm nào được chọn.
                                </p>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mt-2 fw-bold">
                                <span>Tổng cộng:</span>
                                <span id="cartTotal">0 ₫</span>
                            </div>
                        </div>
                    </div>

                    <div class="mt-3">
                        <label class="form-label">Ghi chú</label>
                        <textarea name="note" class="form-control" rows="2"></textarea>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-circle"></i> Tạo đơn hàng
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<!--MODAL CHECKOUT ĐƠN HÀNG -->
<div class="modal fade" id="checkOutPurchase" tabindex="-1" aria-labelledby="checkOutPurchaseLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="checkOutPurchaseLabel">Chi tiết đơn hàng <span id="viewOrderId"></span></h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row mb-3 pb-3 border-bottom">
                    <div class="col-md-6">
                        <p class="mb-1"><strong>Ngày tạo đơn:</strong> <span id="viewCreatedDate">--/--/----</span></p>
                    </div>
                    <div class="col-md-6">
                        <p class="mb-1"><strong>Trạng thái:</strong> <span class="badge bg-warning text-dark" id="viewStatus">Chờ NCC xác nhận</span></p>
                    </div>
                </div>

                <h6 class="fw-bold mb-2">Danh sách sản phẩm đã đặt</h6>
                <div class="table-responsive">
                    <table class="table table-bordered align-middle">
                        <thead class="table-light">
                        <tr>
                            <th style="width: 80px;">Hình ảnh</th>
                            <th style="width: 60px;">ID</th>
                            <th>Tên sản phẩm</th>
                            <th class="text-center" style="width: 100px;">Số lượng</th>
                            <th class="text-end" style="width: 150px;">Tổng giá</th>
                        </tr>
                        </thead>
                        <tbody id="detailTableBody">
                        </tbody>
                    </table>
                </div>

                <div class="d-flex justify-content-end mt-3">
                    <h5 class="fw-bold text-danger">Tổng giá đơn hàng: <span id="viewTotalAmount">0 ₫</span></h5>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>
<!--template bổ sung hàng cho tbody của modal checkout-->
<template id="orderDetailRowTemplate">
    <tr>
        <td class="text-center">
            <img src="" alt="class="img-thumbnail product-img" style="max-height: 50px; max-width: 50px; object-fit: cover;">
        </td>
        <td class="product-id"></td>
        <td class="fw-semibold product-name"></td>
        <td class="text-center fw-bold product-qty"></td>
        <td class="text-end text-success fw-bold product-subtotal"></td>
    </tr>
</template>
</html>