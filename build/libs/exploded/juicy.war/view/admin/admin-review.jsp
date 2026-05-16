<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Quản Lý Đánh Giá</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
        <div class="d-flex justify-content-between align-items-center mb-4 animate__animated animate__fadeInDown">
            <div>
                <h2 class="fw-bold text-success mb-1">Quản Lý Đánh Giá</h2>
                <p class="text-muted mb-0">Quản lý phản hồi và đánh giá từ khách hàng</p>
            </div>
        </div>

        <div class="card card-custom animate__animated animate__fadeInUp">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom mb-0 table-hover align-middle">
                        <thead>
                        <tr>
                            <th class="ps-4">Khách hàng</th>
                            <th>Sản phẩm</th>
                            <th>Đánh giá</th>
                            <th style="width: 30%">Nội dung</th>
                            <th>Lượt hữu ích</th>
                            <th>Ngày đánh giá</th>
                            <th class="text-center">Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${reviews}">
                            <tr>
                                <td class="ps-4 fw-bold text-dark">${r.userName}</td>
                                <td>${r.productName}</td>
                                <td>
                                    <div class="text-warning">
                                        <c:forEach begin="1" end="${r.rating}">
                                            <i class="bi bi-star-fill"></i>
                                        </c:forEach>
                                        <c:forEach begin="${r.rating + 1}" end="5">
                                            <i class="bi bi-star"></i>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td>
                                    <p class="mb-1">${r.content}</p>
                                    <c:if test="${not empty r.sellerReply}">
                                        <div class="bg-light p-2 rounded border-start border-success border-3 mt-2">
                                            <small class="fw-bold text-success">Shop trả lời:</small><br>
                                            <small>${r.sellerReply}</small>
                                        </div>
                                    </c:if>
                                </td>
                                <td><span class="badge bg-primary rounded-pill"><i class="bi bi-hand-thumbs-up me-1"></i> ${r.likes}</span></td>
                                <td><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                                <td class="text-center">
                                    <div class="d-flex justify-content-center gap-2">
                                        <c:if test="${empty r.sellerReply}">
                                            <button class="btn btn-sm btn-outline-success" title="Trả lời" onclick="openReplyModal(${r.id}, '${r.userName}')">
                                                <i class="bi bi-reply-fill"></i>
                                            </button>
                                        </c:if>
                                        <form method="post" action="reviews" onsubmit="return confirm('Bạn có chắc chắn muốn xóa đánh giá này không?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${r.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa đánh giá">
                                                <i class="bi bi-trash-fill"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty reviews}">
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">Chưa có đánh giá nào.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div> <!-- Closing d-flex -->

<!-- Modal Trả lời Đánh giá -->
<div class="modal fade" id="replyModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-success"><i class="bi bi-reply-fill me-2"></i>Trả lời Khách Hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Trả lời đánh giá của: <strong id="replyUserName"></strong></p>
                <input type="hidden" id="replyReviewId">
                <div class="mb-3">
                    <label class="form-label">Nội dung trả lời</label>
                    <textarea class="form-control" id="replyContent" rows="4" placeholder="Nhập câu trả lời của Shop..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-success" onclick="submitReply()">Gửi trả lời</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    let replyModal;
    document.addEventListener("DOMContentLoaded", function() {
        replyModal = new bootstrap.Modal(document.getElementById('replyModal'));
    });

    function openReplyModal(reviewId, userName) {
        document.getElementById('replyReviewId').value = reviewId;
        document.getElementById('replyUserName').innerText = userName;
        document.getElementById('replyContent').value = '';
        replyModal.show();
    }

    function submitReply() {
        const reviewId = document.getElementById('replyReviewId').value;
        const content = document.getElementById('replyContent').value;

        if (!content.trim()) {
            Swal.fire('Lỗi', 'Vui lòng nhập nội dung trả lời!', 'warning');
            return;
        }

        fetch("${pageContext.request.contextPath}/reply-review", {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ 'reviewId': reviewId, 'reply': content })
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    Swal.fire('Thành công', 'Đã lưu phản hồi!', 'success').then(() => {
                        window.location.reload();
                    });
                } else {
                    Swal.fire('Lỗi', data.message, 'error');
                }
            });
    }
</script>
</body>
</html>
