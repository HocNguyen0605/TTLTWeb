<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<footer class="bg-dark text-white pt-5 pb-4">
    <div class="container text-center text-md-start">
        <div class="row text-center text-md-start">
            <div class="col-md-3 col-lg-3 col-xl-3 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">JUICY
                </h5>
                <p>Mang đến nguồn dinh dưỡng từ thiên nhiên, tốt cho sức khỏe.
                </p>
            </div>

            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">Danh Mục
                </h5>
                <p>
                    <a href="products.html" class="text-white text-decoration-none">Nước Ép</a>
                </p>
                <p>
                    <a href="products.html" class="text-white text-decoration-none">Trái Cây Văn Phòng</a>
                </p>
                <p>
                    <a href="404.html" class="text-white text-decoration-none">Khuyến Mãi</a>
                </p>
            </div>

            <div class="col-md-4 col-lg-3 col-xl-3 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 fw-bold text-success">Liên Hệ
                </h5>
                <p>
                    <i class="bi bi-geo-alt-fill me-2"></i>Đường số 7, Đông Hoà, Thủ Đức, Thành phố Hồ Chí Minh, Việt
                    Nam
                </p>
                <p><i class="bi bi-envelope-fill me-2"></i>
                    order@juicy.vn
                </p>
                <p><i class="bi bi-telephone-fill me-2"></i>0347 270 120
                </p>
            </div>
            <div class="col-md-3 mb-4">
                <h5 class="text-uppercase fw-bold text-success">
                    Theo Dõi Chúng Tôi
                </h5>
                <a href="#" class="text-white me-3"><i class="bi bi-facebook"></i></a>
                <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
                <a href="#" class="text-white me-3"><i class="bi bi-tiktok"></i></a>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-md-12 text-center pt-3 border-top border-secondary">
                <p>© 2024 Juicy. All Rights Reserved.
                </p>
            </div>
        </div>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/init.js"></script>
<script>window.contextPath = '${pageContext.request.contextPath}';</script>
<script type="module" src="${pageContext.request.contextPath}/js/navbar-tabs.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/auth.js"></script>
</body>

</html>