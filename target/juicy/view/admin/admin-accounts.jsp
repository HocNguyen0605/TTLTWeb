<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <title>Admin - Quản lý Tài khoản | Juicy</title>

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
               href="#">
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
    <h3 class="text-success mb-4">Danh sách Tài khoản</h3>

    <c:if test="${not empty sessionScope.message}">
      <div class="alert alert-success alert-dismissible fade show" role="alert">
          ${sessionScope.message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
      <c:remove var="message" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
          ${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
      <c:remove var="error" scope="session"/>
    </c:if>

    <div class="shadow-sm p-4 bg-white rounded">
      <table class="table table-hover align-middle">
        <thead class="table-light">
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Họ và Tên</th>
          <th>Email</th>
          <th>Số điện thoại</th>
          <th>Quyền</th>
          <th class="text-center">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>#${u.id}</td>
            <td>${u.username}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.phone}</td>
            <td>
              <form action="${pageContext.request.contextPath}/admin/accounts" method="post" class="d-flex align-items-center mb-0 gap-2">
                <input type="hidden" name="action" value="updateRole">
                <input type="hidden" name="id" value="${u.id}">
                <select name="role" class="form-select form-select-sm" style="width: auto;">
                  <option value="user" ${u.role == 0 ? 'selected' : ''}>User</option>
                  <option value="admin" ${u.role == 1 ? 'selected' : ''}>Admin</option>
                </select>
                <button type="submit" class="btn btn-sm btn-outline-primary">
                  <i class="bi bi-save"></i>
                </button>
              </form>
            </td>
            <td class="text-center">
              <form action="${pageContext.request.contextPath}/admin/accounts" method="post" class="d-inline" onsubmit="return confirm('Bạn có chắc chắn muốn xoá tài khoản này không?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${u.id}">
                <button type="submit" class="btn btn-sm btn-outline-danger">
                  <i class="bi bi-trash"></i> Xóa
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty users}">
          <tr>
            <td colspan="7" class="text-center text-muted py-4">Chưa có tài khoản nào.</td>
          </tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- FOOTER -->
<footer class="bg-dark text-white text-center py-3">
  © 2024 Juicy. All Rights Reserved.
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
