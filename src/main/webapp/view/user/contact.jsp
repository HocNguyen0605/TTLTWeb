<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Liên hệ với chúng tôi" />
    <jsp:param name="activePage" value="contact" />
</jsp:include>
<jsp:include page="/view/user/include/search-bar.jsp" />
<!-- NỘI DUNG CHÍNH: Form liên hệ -->
<section class="container my-5">
    <h2 class="text-center mb-4 text-success fw-bold">Liên Hệ Với Chúng Tôi</h2>
    <div class="row justify-content-center">
        <div class="col-md-8">
            <form id="contactForm" action="${pageContext.request.contextPath}/contact" method="post" class="p-4 shadow rounded bg-white">
                <div class="mb-3">
                    <label for="name" class="form-label fw-semibold">Họ và Tên</label>
                    <input type="text" class="form-control" id="name" name="fullName" placeholder="Nhập họ và tên" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label fw-semibold">Email</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="example@gmail.com"
                           required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label fw-semibold">Số điện thoại</label>
                    <input type="tel" class="form-control" id="phone" name="phone" placeholder="0123456789">
                </div>
                <div class="mb-3">
                    <label for="message" class="form-label fw-semibold">Nội dung</label>
                    <textarea class="form-control" id="message" name="message" rows="5" placeholder="Nhập nội dung"
                              required></textarea>
                </div>
                <button type="submit" class="btn btn-success rounded-pill fw-semibold">Gửi Liên Hệ</button>
                <c:if test="${not empty success}">
                    <p class="text-success mt-3 fw-semibold">✅ ${success}</p>
                </c:if>
                <c:if test="${not empty error}">
                    <p class="text-danger mt-3 fw-semibold">❌ ${error}</p>
                </c:if>
            </form>
        </div>
    </div>
</section>
<!-- Thông tin liên hệ -->
<%@include file="/view/user/include/footer.jsp" %>
