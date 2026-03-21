<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="about" />
</jsp:include>
<!--Thêm thanh tìm kiếm vào dưới phần header của website-->
<section class="bg-light py-4 border-bottom">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <form class="d-flex" action="${pageContext.request.contextPath}/search" method="get">
                    <input class="form-control form-control-lg me-2 border-success" type="search" name="query"
                           placeholder="Tìm kiếm tên sản phẩm, loại trái cây..." aria-label="Search">
                    <button class="btn btn-primary-custom btn-lg fw-bold" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>
<!-- HERO BANNER -->
<section class="hero-section d-flex align-items-center justify-content-center text-center">
    <section><img src="images/banner/bannerabout.jpg" class="hero-section"></section>
    <!-- dành cho sửa đổi chỉnh banner -->
</section>

<!-- SỨ MỆNH & CAM KẾT -->
<section class="container py-5">
    <div class="row align-items-center gy-5">
        <div class="col-md-6">
            <img src="images/banner/about-fruit.jpg" class="img-fluid rounded-4 shadow" alt="Trái cây tươi">
        </div>
        <div class="col-md-6">
            <h2 class="fw-bold text-success mb-3">Sứ Mệnh &amp; Cam Kết</h2>
            <p>Chúng tôi mang đến nguồn dinh dưỡng từ thiên nhiên, giúp bạn duy trì lối sống lành mạnh. Mỗi sản
                phẩm
                Juicy được chọn lọc kỹ lưỡng, từ nguyên liệu tươi ngon đến quy trình ép lạnh giữ trọn dưỡng
                chất.</p>
            <ul class="list-unstyled">
                <li><i class="bi bi-check-circle-fill text-success me-2"></i>Nguyên liệu tươi đạt chuẩn
                    GlobalGAP
                </li>
                <li><i class="bi bi-check-circle-fill text-success me-2"></i>Cold-press giữ nguyên vitamin &amp;
                    enzyme
                </li>
                <li><i class="bi bi-check-circle-fill text-success me-2"></i>Không phẩm màu, không chất bảo quản
                </li>
            </ul>
        </div>
    </div>
    <!-- Đội ngũ -->
    <div class="row align-items-center gy-5">
        <div class="col-md-6 order-md-2">
            <img src="images/banner/about-team.jpg" class="img-fluid rounded-4 shadow" alt="Đội ngũ Juicy">
        </div>
        <div class="col-md-6 order-md-1">
            <h2 class="fw-bold text-success mb-3">Đội Ngũ Juicy</h2>
            <p>
                Chúng tôi là một tập thể trẻ, năng động, đam mê sức khỏe và yêu thiên nhiên. Mỗi thành viên của
                Juicy
                đều làm việc với tinh thần tận tâm và sáng tạo để mang lại trải nghiệm tốt nhất cho khách hàng.
            </p>
            <p>
                Cảm ơn bạn đã đồng hành cùng Juicy trên hành trình lan tỏa lối sống xanh, sạch và lành mạnh! 🌿
            </p>
        </div>
    </div>
</section>

<!-- LỊCH SỬ HÌNH THÀNH -->
<section class="bg-light py-5">
    <div class="container">
        <h2 class="fw-bold text-success mb-4 text-center">Lịch Sử Hình Thành</h2>
        <p class="text-center mb-5">Juicy ra đời với tầm nhìn mang trái cây tươi ngon &amp; nước ép chất lượng
            cao đến
            mọi gia đình Việt Nam. Bắt đầu từ một cửa hàng nhỏ, đến nay chúng tôi đã phục vụ hàng ngàn khách
            hàng mỗi
            ngày.</p>
        <div class="row g-4 text-center">
            <div class="col-md-4">
                <i class="bi bi-lightning-charge-fill fs-1 text-success mb-2"></i>
                <h5 class="fw-bold">2018</h5>
                <p>Khởi nghiệp với cửa hàng đầu tiên tại TP.HCM</p>
            </div>
            <div class="col-md-4">
                <i class="bi bi-people-fill fs-1 text-success mb-2"></i>
                <h5 class="fw-bold">2020</h5>
                <p>Mở rộng chuỗi cửa hàng &amp; xây dựng thương hiệu uy tín</p>
            </div>
            <div class="col-md-4">
                <i class="bi bi-award-fill fs-1 text-success mb-2"></i>
                <h5 class="fw-bold">2023</h5>
                <p>Đạt chứng nhận chất lượng &amp; phục vụ khách hàng toàn quốc</p>
            </div>
        </div>
    </div>
</section>

<!-- QUY TRÌNH -->
<section class="container py-5">
    <h2 class="fw-bold text-success mb-4 text-center">Quy Trình Chọn Lọc &amp; Ép Lạnh</h2>
    <div class="row g-4 text-center">
        <div class="col-md-3">
            <i class="bi bi-search fs-1 text-success mb-2"></i>
            <h6 class="fw-bold">Chọn Lựa</h6>
            <p>Nguyên liệu tươi ngon, đạt chuẩn chất lượng</p>
        </div>
        <div class="col-md-3">
            <i class="bi bi-droplet fs-1 text-success mb-2"></i>
            <h6 class="fw-bold">Ép Lạnh</h6>
            <p>Giữ nguyên dưỡng chất &amp; hương vị tự nhiên</p>
        </div>
        <div class="col-md-3">
            <i class="bi bi-thermometer-half fs-1 text-success mb-2"></i>
            <h6 class="fw-bold">Kiểm Tra</h6>
            <p>Đảm bảo an toàn và chất lượng cao nhất</p>
        </div>
        <div class="col-md-3">
            <i class="bi bi-hand-thumbs-up-fill fs-1 text-success mb-2"></i>
            <h6 class="fw-bold">Giao Hàng</h6>
            <p>Đến tay khách hàng nhanh chóng &amp; tươi ngon</p>
        </div>
    </div>
</section>

<!-- TESTIMONIAL -->
<section class="bg-light py-5">
    <div class="container text-center">
        <h2 class="fw-bold text-success mb-4">Khách Hàng Nói Gì?</h2>
        <div class="row g-4">
            <div class="col-md-4">
                <div class="card border-0 shadow-sm p-3">
                    <p>"Nước ép Juicy luôn tươi ngon, mỗi sáng là động lực tuyệt vời cho cả ngày!"</p>
                    <h6 class="fw-bold mb-0">Nguyễn Thị Lan</h6>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 shadow-sm p-3">
                    <p>"Mình rất thích cam kết không chất bảo quản, yên tâm dùng mỗi ngày."</p>
                    <h6 class="fw-bold mb-0">Trần Văn Hưng</h6>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 shadow-sm p-3">
                    <p>"Giao hàng nhanh, đóng gói cẩn thận. Juicy xứng đáng 5 sao!"</p>
                    <h6 class="fw-bold mb-0">Lê Thị Minh</h6>
                </div>
            </div>
        </div>
    </div>
</section>
<!--footer-->
<%@include file="/view/user/include/footer.jsp" %>
