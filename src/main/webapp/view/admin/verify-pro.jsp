<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Xác minh Bảo mật - Pro Admin</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/verify-pro.css">
</head>
<body class="d-flex align-items-center justify-content-center vh-100">

<div class="card shadow-lg border-0" style="max-width: 450px; width: 100%; border-radius: 15px;">
  <div class="card-body p-5 text-center">
    <div class="mb-4">
      <i class="bi bi-shield-lock text-success" style="font-size: 3rem;"></i>
    </div>
    <h3 class="fw-bold mb-3 text-success">Xác minh Danh tính</h3>
    <p class="text-muted mb-4">Bạn đang truy cập vào khu vực Quản lý Tài khoản (cấp độ Pro-Admin). Vui lòng xác minh mã OTP gửi về Email của bạn để tiếp tục.</p>

    <div id="alertBox" class="alert d-none" role="alert"></div>

    <button id="btnSendOTP" class="btn btn-outline-success rounded-pill mb-4 px-4 w-100">
      <i class="bi bi-envelope"></i> Gửi mã OTP ngay
    </button>

    <div id="otpSection" class="d-none">
      <div class="otp-container">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
      </div>

      <button id="btnVerify" class="btn btn-success rounded-pill w-100 fw-bold py-2 mb-3">
        <span id="spinner" class="spinner-border spinner-border-sm d-none me-2" role="status"></span>
        Xác nhận & Tiếp tục
      </button>

      <div class="text-center">
        <small class="text-muted">Chưa nhận được mã? <a href="#" id="btnResend" class="text-success text-decoration-none fw-bold">Gửi lại (<span id="countdown">60</span>s)</a></small>
      </div>
    </div>

    <div class="mt-4">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-muted text-decoration-none">
        <i class="bi bi-arrow-left"></i> Quay lại Dashboard
      </a>
    </div>
  </div>
</div>

<script>
  const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/verify-pro.js"></script>

</body>
</html>
