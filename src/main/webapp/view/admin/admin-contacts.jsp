<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Quản lý Liên hệ | Juicy</title>

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
        <a class="navbar-brand fw-bold text-success fs-3"
           href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/images/logo/logo-juicy.png" height="40"
                 class="me-2">
            JUICY
        </a>
    </nav>
</header>

<div class="d-flex">
    <!-- Sidebar -->
    <div class="bg-success text-white "
         style="width: 250px; min-height: 100vh;">
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
                <ol class="collapse show" id="menuQL">
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
                    <li>
                        <a class="nav-link text-white ms-3 active"
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
        <h2 class="mb-4">Danh sách Liên hệ / Ý kiến khách hàng</h2>

        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.successMsg}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="successMsg" scope="session" />
        </c:if>

        <c:if test="${not empty sessionScope.errorMsg}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${sessionScope.errorMsg}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="errorMsg" scope="session" />
        </c:if>

        <div class="table-responsive bg-white shadow-sm rounded">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-success">
                <tr>
                    <th>ID</th>
                    <th>Khách hàng</th>
                    <th>Email/SĐT</th>
                    <th>Chủ đề</th>
                    <th>Nội dung</th>
                    <th>Ngày gửi</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="c" items="${contacts}">
                    <tr>
                        <td>${c.idContact}</td>
                        <td>${c.fullName}</td>
                        <td>
                            <div><i class="bi bi-envelope"></i> ${c.email}</div>
                            <div><i class="bi bi-telephone"></i> ${c.phone}</div>
                        </td>
                        <td class="fw-bold">${c.subject}</td>
                        <td style="max-width: 250px;" class="text-truncate" title="${c.message}">${c.message}</td>
                        <td>
                            <fmt:parseDate value="${c.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${c.status == 'Đã phản hồi'}">
                                    <span class="badge bg-success">Đã phản hồi</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning text-dark">Chờ xử lý</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#replyModal${c.idContact}">
                                <i class="bi bi-reply"></i> Phản hồi
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty contacts}">
                    <tr>
                        <td colspan="8" class="text-center py-4">Chưa có ý kiến phản hồi nào.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Render Modals Outside Table to ensure Valid HTML -->
<c:forEach var="c" items="${contacts}">
    <div class="modal fade" id="replyModal${c.idContact}" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/admin/contacts" method="post">
                    <input type="hidden" name="action" value="reply">
                    <input type="hidden" name="idContact" value="${c.idContact}">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title">Phản hồi khách hàng</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="fw-bold">Gửi đến:</label>
                            <input type="text" class="form-control" value="${c.fullName} (${c.email})" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="fw-bold">Ý kiến khách hàng:</label>
                            <textarea class="form-control bg-light" rows="3" readonly>${c.message}</textarea>
                        </div>
                        <div class="mb-3">
                            <label class="fw-bold text-success">Nội dung phản hồi:</label>
                            <textarea class="form-control border-success" name="replyMessage" rows="5" placeholder="Nhập nội dung phản hồi sẽ được gửi qua email..." required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-success"><i class="bi bi-send"></i> Gửi Mail</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:forEach>

<footer class="bg-dark text-white text-center py-3 mt-auto">
    © 2024 Juicy. All Rights Reserved.
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
