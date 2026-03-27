<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Hồ sơ cá nhân" />
</jsp:include>

<section class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10">
            <div class="auth-container shadow-sm p-4 bg-white rounded">

                <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i> ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3 class="fw-bold mb-0">Hồ sơ cá nhân</h3>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm fw-bold">
                        <i class="bi bi-box-arrow-right me-1"></i> Đăng xuất
                    </a>
                </div>

                <ul class="nav nav-tabs mb-4" id="profileTab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link ${activeTab != 'password' ? 'active' : ''}" id="info-tab"
                                data-bs-toggle="tab" data-bs-target="#info-pane" type="button" role="tab">
                            <i class="bi bi-person-lines-fill me-2"></i>Thông tin cá nhân
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link ${activeTab == 'password' ? 'active' : ''}" id="password-tab"
                                data-bs-toggle="tab" data-bs-target="#password-pane" type="button" role="tab">
                            <i class="bi bi-shield-lock-fill me-2"></i>Đổi mật khẩu
                        </button>
                    </li>
                </ul>

                <div class="tab-content" id="profileTabContent">

                    <div class="tab-pane fade ${activeTab != 'password' ? 'show active' : ''}" id="info-pane" role="tabpanel">
                        <form action="${pageContext.request.contextPath}/profile" method="POST">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Tên đăng nhập</label>
                                    <input type="text" name="username" class="form-control bg-light"
                                           value="${auth.username}" readonly>
                                    <small class="text-muted">Tên đăng nhập không thể thay đổi.</small>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Họ và tên</label>
                                    <input type="text" name="fullName" class="form-control"
                                           value="${auth.fullName}" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Email</label>
                                <input type="email" name="email" class="form-control"
                                       value="${auth.email}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Số điện thoại</label>
                                <input type="text" name="phone" class="form-control"
                                       value="${auth.phone}" placeholder="Chưa cập nhật">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Địa chỉ</label>
                                <textarea name="address" class="form-control" rows="2"
                                          placeholder="Chưa cập nhật">${auth.address}</textarea>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" name="action" value="updateProfile" class="btn btn-primary-custom w-100 fw-bold py-2">
                                    Cập nhật thông tin
                                </button>
                            </div>
                        </form>
                    </div>

                    <div class="tab-pane fade ${activeTab == 'password' ? 'show active' : ''}" id="password-pane" role="tabpanel">
                        <form action="${pageContext.request.contextPath}/profile" method="POST">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Mật khẩu hiện tại</label>
                                <input type="password" name="oldPassword" class="form-control" required
                                       placeholder="Nhập mật khẩu đang dùng">
                            </div>
                            <hr>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Mật khẩu mới</label>
                                <input type="password" name="newPassword" class="form-control" required
                                       placeholder="Tối thiểu 8 ký tự">
                            </div>
                            <div class="mb-4">
                                <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
                                <input type="password" name="confirmPassword" class="form-control" required
                                       placeholder="Nhập lại mật khẩu mới">
                            </div>
                            <button type="submit" name="action" value="changePassword" class="btn btn-primary-custom w-100 fw-bold py-2">
                                Xác nhận đổi mật khẩu
                            </button>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </div>
</section>

<%@include file="/view/user/include/footer.jsp" %>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        var activeTab = "${activeTab}";
        if (activeTab === "password") {
            var triggerEl = document.querySelector('#password-tab');
            if(triggerEl) {
                var tab = new bootstrap.Tab(triggerEl);
                tab.show();
            }
        }
    });
</script>