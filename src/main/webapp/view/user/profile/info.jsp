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
            <input type="text" maxlength="50" name="fullName" class="form-control"
                   value="${auth.fullName}" required>
        </div>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Email</label>
        <input type="email" maxlength="50" name="email" class="form-control"
               value="${auth.email}" required>
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Số điện thoại</label>
        <input type="text" maxlength="15" name="phone" class="form-control"
               value="${auth.phone}" placeholder="">
    </div>
    <div class="mb-3">
        <label class="form-label fw-semibold">Địa chỉ</label>
        <textarea name="address" class="form-control" rows="2"
                  placeholder="">${auth.address}</textarea>
    </div>

    <div class="d-flex justify-content-between mt-4">
        <button type="submit" class="btn btn-primary-custom w-100 fw-bold py-2">
            Cập nhật thông tin
        </button>
    </div>
</form>
