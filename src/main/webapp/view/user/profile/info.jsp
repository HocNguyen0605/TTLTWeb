<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<form action="${pageContext.request.contextPath}/updateProfile" method="POST">
    <div class="row">
        <div class="col-md-6 mb-3">
            <label class="form-label fw-semibold">Tên đăng nhập</label>
            <input type="text" name="username" class="form-control bg-light"
                   value="${auth.username}" readonly>
            <small class="text-muted">Tên đăng nhập không thể thay đổi.</small>
        </div>
        <div class="col-md-6 mb-3">
            <label class="form-label fw-semibold">Họ và tên</label>
            <input type="text" maxlength="50" name="fullName"
                   class="form-control ${not empty errors['fullName'] ? 'is-invalid' : ''}"
                   value="${auth.fullName}" required>
            <c:if test="${not empty errors['fullName']}">
                <div class="invalid-feedback">${errors['fullName']}</div>
            </c:if>
        </div>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Email</label>
        <input type="email" maxlength="50" name="email"
               class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}"
               value="${auth.email}" required>
        <c:if test="${not empty errors['email']}">
            <div class="invalid-feedback">${errors['email']}</div>
        </c:if>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Số điện thoại</label>
        <input type="text" maxlength="15" name="phone"
               class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}"
               value="${auth.phone}" placeholder="">
        <c:if test="${not empty errors['phone']}">
            <div class="invalid-feedback">${errors['phone']}</div>
        </c:if>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Địa chỉ</label>
        <textarea name="address" class="form-control" rows="2"
                  placeholder="">${auth.address}</textarea>
    </div>

    <div class="d-flex justify-content-end mt-4">
        <button type="submit" class="btn btn-primary-custom fw-bold py-2">
            Cập nhật thông tin
        </button>
    </div>
</form>
<hr class="my-5 border-light-subtle">

<h5 class="fw-bold mb-4">Đổi Email</h5>
<form action="${pageContext.request.contextPath}/updateEmail" method="POST" id="updateEmailForm">
    <div class="mb-3">
        <label class="form-label fw-semibold">Email mới</label>
        <input type="email" maxlength="50" name="email" id="profileEmail"
               class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}" value="${auth.email}"
                ${auth.isGoogleAccount ? 'readonly' : ''}>
        <c:if test="${not empty errors['email']}">
            <div class="invalid-feedback d-block">${errors['email']}</div>
        </c:if>
        <c:if test="${auth.isGoogleAccount}">
            <small class="text-muted">Tài khoản Google không thể đổi email.</small>
        </c:if>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Mã xác thực OTP</label>
        <div class="input-group has-validation">
            <input type="text" name="otp" class="form-control ${not empty errors['otp'] ? 'is-invalid' : ''}"
                   maxlength="6"
                   placeholder="Nhập mã 6 số" required>
            <button class="btn btn-outline-success" type="button" id="btnSendOTPProfile">Gửi mã</button>
            <c:if test="${not empty errors['otp']}">
                <div class="invalid-feedback">${errors['otp']}</div>
            </c:if>
        </div>
        <small id="otpTimerProfile" class="text-danger mt-1 d-block"></small>
    </div>
    <div class="d-flex justify-content-end mt-3">
        <button type="submit" class="btn btn-primary-custom fw-bold py-2">
            Cập nhật Email
        </button>
    </div>
</form>

<script src="${pageContext.request.contextPath}/js/otp.js"></script>
<script>
    setupOTPSender('btnSendOTPProfile', '#profileEmail', 'otpTimerProfile', '${pageContext.request.contextPath}');
</script>