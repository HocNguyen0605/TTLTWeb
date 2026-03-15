<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="contact" />
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
<!-- NỘI DUNG CHÍNH: Form liên hệ -->
<section class="container my-5">
    <h2 class="text-center mb-4 text-success fw-bold">Liên Hệ Với Chúng Tôi</h2>
    <div class="row justify-content-center">
        <div class="col-md-8">
            <form id="contactForm" class="p-4 shadow rounded bg-white">
                <div class="mb-3">
                    <label for="name" class="form-label fw-semibold">Họ và Tên</label>
                    <input type="text" class="form-control" id="name" placeholder="Nhập họ và tên" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label fw-semibold">Email</label>
                    <input type="email" class="form-control" id="email" placeholder="example@gmail.com"
                           required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label fw-semibold">Số điện thoại</label>
                    <input type="tel" class="form-control" id="phone" placeholder="0123456789">
                </div>
                <div class="mb-3">
                    <label for="message" class="form-label fw-semibold">Nội dung</label>
                    <textarea class="form-control" id="message" rows="5" placeholder="Nhập nội dung"
                              required></textarea>
                </div>
                <button type="submit" class="btn btn-success rounded-pill fw-semibold">Gửi Liên Hệ</button>
                <p id="successMessage" class="text-success mt-3 fw-semibold" style="display:none;">
                    ✅ Cảm ơn bạn! Chúng tôi sẽ phản hồi sớm nhất.
                </p>
            </form>
        </div>
    </div>
</section>
<!-- Thông tin liên hệ -->
<%@include file="/view/user/include/footer.jsp" %>
