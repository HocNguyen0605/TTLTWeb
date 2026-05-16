<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="icon" type="images/png" href="images/logo-juicy.png" sizes="32x32"/>
  <link rel="shortcut icon" href="images/logo-juicy.png" type="image/png"/>
  <title>404 - Trang Không Tìm Thấy - Juicy</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/error.css">

</head>
<body>

<main class="error-container">
  <a class="mb-4 d-inline-block" href="/">
    <img src="images/logo/logo-juicy.png" alt="Juicy Logo" height="80">
  </a>
  <h1 class="error-code">404</h1>
  <p class="error-message">Ôi không! Trang bạn tìm không tồn tại.</p>
  <p class="error-description">
    Rất tiếc, đường dẫn bạn truy cập có thể đã bị thay đổi hoặc không còn tồn tại.
    <br>Hãy quay lại trang chủ để khám phá các sản phẩm tươi ngon của chúng tôi!
  </p>
  <a href="${pageContext.request.contextPath}/home" class="btn btn-primary-custom btn-home-404 shadow-lg">
    <i class="bi bi-house-door-fill me-2"></i> Quay Về Trang Chủ
  </a>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstra  p@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>