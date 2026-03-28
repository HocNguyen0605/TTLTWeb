<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ"/>
</jsp:include>
<section class="container py-5">
    <div class="auth-container">
        <h2 class="text-center fw-bold text-success mb-4">Quản Lý Tài Khoản</h2>

        <ul class="nav nav-tabs nav-fill mb-4" id="authTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link ${activeTab == 'register' ? '' : 'active'}" id="login-tab"
                        data-bs-toggle="tab" data-bs-target="#login" type="button" role="tab"
                        aria-selected="${activeTab == 'register' ? 'false' : 'true'}">ĐĂNG NHẬP
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link ${activeTab == 'register' ? 'active' : ''}" id="register-tab"
                        data-bs-toggle="tab" data-bs-target="#register" type="button" role="tab"
                        aria-selected="${activeTab == 'register' ? 'true' : 'false'}">ĐĂNG KÝ
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="forgot-tab" data-bs-toggle="tab" data-bs-target="#forgot"
                        type="button" role="tab">QUÊN MẬT KHẨU
                </button>
            </li>
        </ul>

        <div class="tab-content" id="authTabsContent">

            <div class="tab-pane fade ${activeTab == 'register' ? '' : 'show active'}" id="login"
                 role="tabpanel" aria-labelledby="login-tab">
                <form action="${pageContext.request.contextPath}/login" id="loginForm" method="post">

                    <div class="mb-3">
                        <label for="loginEmail" class="form-label fw-semibold">Email hoặc Tên đăng
                            nhập</label>
                        <input type="text" class="form-control" id="loginEmail" name="email" required
                               placeholder="Nhập email của bạn"/>
                    </div>
                    <div class="mb-3">
                        <label for="loginPassword" class="form-label fw-semibold">Mật khẩu</label>
                        <input type="password" class="form-control" id="loginPassword" name="password" required
                               placeholder="Nhập mật khẩu"/>
                    </div>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="rememberMe" name="remember">
                            <label class="form-check-label" for="rememberMe">
                                Ghi nhớ đăng nhập
                            </label>
                        </div>
                        <a href="#" class="text-success fw-semibold text-decoration-none"
                           onclick="document.getElementById('forgot-tab').click(); return false;">Quên Mật khẩu?</a>
                    </div>
                    <button type="submit" class="btn btn-primary-custom w-100 fw-bold py-2">Đăng Nhập</button>
                </form>
                <div class="text-center mt-3">
                    <p>Chưa có tài khoản?
                        <a href="#" class="text-success fw-bold text-decoration-none"
                           onclick="document.getElementById('register-tab').click(); return false;">Đăng ký ngay</a>
                    </p>
                </div>
            </div>

            <div class="tab-pane fade ${activeTab == 'register' ? 'show active' : ''}" id="register"
                 role="tabpanel" aria-labelledby="register-tab">
                <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post">

                    <div class="mb-3">
                        <label for="regUserame" class="form-label fw-semibold">Tên người dùng *</label>
                        <input type="text" class="form-control" maxlength="20" id="regUserame" name="username" required
                               placeholder="Nhập tên của bạn" value="${oldUsername}"/>
                        <c:if test="${errors.username != null}">
                            <div class="text-danger small">
                                    ${errors.username}
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="regFullname" class="form-label fw-semibold">Họ và tên</label>
                        <input type="text" class="form-control" maxlength="50" id="regFullname" name="fullname"
                               placeholder="Nhập họ và tên của bạn" value="${oldFullName}">
                        <c:if test="${errors.fullname != null}">
                           <div class="text-danger small">
                                    ${errors.fullname}
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="regEmail" class="form-label fw-semibold">Email *</label>
                        <input type="email" class="form-control" id="regEmail" name="email" required
                               placeholder="Nhập địa chỉ email" value="${oldEmail}"/>
                        <c:if test="${errors.email != null}">
                            <div class="text-danger small">
                                    ${errors.email}
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="regPassword" class="form-label fw-semibold">Mật khẩu *</label>
                        <input type="password" class="form-control" maxlength="20" id="regPassword" name="password" required
                               placeholder="Tối thiểu 8 ký tự, kí tự viết hoa và kí tự đặt biệt"/>
                        <c:if test="${errors.password != null}">
                            <div class="text-danger small">
                                    ${errors.password}
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-4">
                        <label for="regConfirmPassword" class="form-label fw-semibold">Xác nhận Mật khẩu *</label>
                        <input type="password" class="form-control" maxlength="20" id="regConfirmPassword" name="confirmPassword"
                               required placeholder="Nhập lại mật khẩu"/>
                        <c:if test="${errors.confirmPassword != null}">
                            <div class="text-danger small">
                                    ${errors.confirmPassword}
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mã xác thực OTP *</label>
                        <div class="input-group">
                            <input type="text" name="otp" class="form-control" maxlength="6" placeholder="Nhập mã 6 số" required>
                            <button class="btn btn-outline-success" type="button" id="btnSendOTP">Gửi mã</button>
                        </div>
                        <small id="otpTimer" class="text-danger"></small>
                    </div>

                    <button type="submit" class="btn btn-primary-custom w-100 fw-bold py-2">Đăng Ký</button>
                </form>
                <div class="text-center mt-3">
                    <p>Đã có tài khoản? <a href="#" class="text-success fw-bold text-decoration-none"
                                           onclick="document.getElementById('login-tab').click(); return false;">Đăng
                        nhập</a></p>
                </div>
            </div>

            <div class="tab-pane fade" id="forgot" role="tabpanel" aria-labelledby="forgot-tab">
                <p class="text-center mb-4 text-muted">Vui lòng nhập email đã đăng ký của bạn. Chúng tôi sẽ gửi một liên
                    kết để đặt lại mật khẩu.</p>
                <form id="forgotPasswordForm">
                    <div class="mb-3">
                        <label class="form-label">Nhập Email đã đăng ký</label>
                        <input type="email" name="email" id="forgotEmail" class="form-control" required
                               placeholder="name@example.com">
                        <div id="forgotMsg" class="mt-2 small"></div>
                    </div>
                    <button type="submit" class="btn btn-success w-100" id="btnForgot">Gửi mật khẩu mới</button>
                </form>
                <div class="text-center mt-3">
                    <p><a href="#" class="text-success fw-bold text-decoration-none"
                          onclick="document.getElementById('login-tab').click(); return false;">Quay lại Đăng nhập</a>
                    </p>
                </div>
            </div>

        </div>
    </div>
</section>

<%@include file="/view/user/include/footer.jsp" %>

<script type="module" src="${pageContext.request.contextPath}/js/init.js"></script>
<script>
    //login otp
    document.getElementById('btnSendOTP').addEventListener('click', function () {
        const email = document.querySelector('#registerForm input[name="email"]').value;
        const notice = document.getElementById('otpTimer');
        const btn = this;
        if (!email) {
            notice.innerHTML = "Vui lòng nhập email trước";
            return;
        }
        let interval;
        let timeLeft = 60;

        btn.disabled = true;
        notice.innerHTML = "Đang gửi mã... (60s)";

        const Updtimer = () => {
            if(timeLeft <= 0){
                clearInterval(interval);
                btn.disabled = false;
                notice.innerHTML = "Mã đã hết hạn, vui lòng gửi lại";
            } else {
                notice.innerHTML = "Mã hiệu lực trong: " + timeLeft + "s";
                timeLeft -= 1;
            }
        }

        Updtimer();
        interval = setInterval(Updtimer, 1000);

        fetch('${pageContext.request.contextPath}/send-otp?email=' + email)
            .catch(err => {
                notice.innerHTML = "Lỗi gửi mã, thử lại sau.";
                btn.disabled = false;
                clearInterval(interval);
            });
    });
</script>
<script>
    //logic quên mk
    document.getElementById('forgotPasswordForm').addEventListener('submit', function (e) {
        e.preventDefault(); // Ngăn load lại trang

        const email = document.getElementById('forgotEmail').value;
        const msgDiv = document.getElementById('forgotMsg');
        const btn = document.getElementById('btnForgot');

        msgDiv.innerHTML = "Đang xử lý...";
        btn.disabled = true;

        fetch('${pageContext.request.contextPath}/forgot-password', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'email=' + encodeURIComponent(email)
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    msgDiv.className = "mt-2 small text-success";
                    msgDiv.innerHTML = "Mật khẩu mới đã được gửi vào Email của bạn!";
                } else if (data === "not_found") {
                    msgDiv.className = "mt-2 small text-danger";
                    msgDiv.innerHTML = "Email này không tồn tại trong hệ thống!";
                } else {
                    msgDiv.className = "mt-2 small text-danger";
                    msgDiv.innerHTML = "Có lỗi xảy ra, vui lòng thử lại!";
                }
                btn.disabled = false;
            });
    });
</script>
