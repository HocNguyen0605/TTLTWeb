<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<form action="${pageContext.request.contextPath}/changePassword" method="POST">
    <div class="mb-3">
        <label class="form-label fw-semibold">Mật khẩu hiện tại</label>
        <div class="input-group has-validation">
            <input type="password" name="oldPassword" class="form-control " required
                   placeholder="Nhập mật khẩu hiện tại">
            <button class="btn btn-outline-success btn-toggle-Password" type="button">
                <i class="bi bi-eye"></i>
            </button>
            <c:if test="${not empty errors.oldPassword}">
                <div class="invalid-feedback">${errors.oldPassword}</div>
            </c:if>
        </div>
    </div>
    <hr>
    <div class="mb-3">
        <label class="form-label fw-semibold">Mật khẩu mới</label>
        <div class="input-group has-validation">
            <input type="password" name="newPassword" class="form-control ${not empty errors.newPassword ? 'is-invalid' : ''}" required
                   maxlength="20"
                   placeholder="Tối thiểu 8 ký tự, kí tự viết hoa và kí tự đặt biệt">
            <button class="btn btn-outline-success btn-toggle-Password" type="button">
                <i class="bi bi-eye"></i>
            </button>
            <c:if test="${not empty errors.newPassword}">
                <div class="invalid-feedback">${errors.newPassword}</div>
            </c:if>
        </div>
    </div>
    <div class="mb-4">
        <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
        <div class="input-group has-validation">
            <input type="password" name="confirmPassword" class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" required
                   maxlength="20"
                   placeholder="Nhập lại mật khẩu mới">
            <button class="btn btn-outline-success btn-toggle-Password" type="button">
                <i class="bi bi-eye"></i>
            </button>
            <c:if test="${not empty errors.confirmPassword}">
                <div class="invalid-feedback">${errors.confirmPassword}</div>
            </c:if>
        </div>
    </div>
    <button type="submit" class="btn btn-primary-custom w-100 fw-bold py-2">
        Xác nhận đổi mật khẩu
    </button>
</form>
