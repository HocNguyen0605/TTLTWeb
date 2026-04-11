<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Hồ sơ cá nhân"/>
</jsp:include>

<section class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-10 col-md-12">
            <div class="profile-container shadow-sm p-4 bg-white rounded">

                <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i> ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="row mt-3">
                    <div class="col-md-auto border-end border-light-subtle d-flex flex-column mb-4 mb-md-0 pe-md-4">
                        <h4 class="fw-bold mb-4 px-2">Hồ sơ cá nhân</h4>
                        <div class="nav flex-column nav-tabs border-0 gap-2" id="profileTab" role="tablist"
                             aria-orientation="vertical">
                            <button
                                    class="nav-link text-decoration-none text-start rounded text-wrap ${activeTab != 'password' ? 'active' : ''}"
                                    id="info-tab" data-bs-toggle="tab" data-bs-target="#info-pane" type="button"
                                    role="tab">
                                <i class="bi bi-person-lines-fill me-2"></i>Thông tin cá nhân
                            </button>
                            <button
                                    class="nav-link text-decoration-none text-start rounded text-wrap ${activeTab == 'password' ? 'active' : ''}"
                                    id="password-tab" data-bs-toggle="tab" data-bs-target="#password-pane"
                                    type="button" role="tab">
                                <i class="bi bi-shield-lock-fill me-2"></i>Đổi mật khẩu
                            </button>
                            <a href="${pageContext.request.contextPath}/logout"
                               onclick="return confirm('Bạn có chắc chắn muốn đăng xuất không?')"
                               class="nav-link text-decoration-none text-start text-danger text-wrap mt-3 fw-bold border-0">
                                <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                            </a>
                        </div>
                    </div>

                    <div class="col-md ps-md-4">
                        <div class="tab-content" id="profileTabContent">
                            <div class="tab-pane fade ${activeTab != 'password' ? 'show active' : ''}"
                                 id="info-pane" role="tabpanel">
                                <h5 class="fw-bold mb-4">Thông tin cá nhân</h5>
                                <jsp:include page="/view/user/profile/info.jsp"/>
                            </div>

                            <div class="tab-pane fade ${activeTab == 'password' ? 'show active' : ''}"
                                 id="password-pane" role="tabpanel">
                                <h5 class="fw-bold mb-4">Đổi mật khẩu</h5>
                                <jsp:include page="/view/user/profile/changepass.jsp"/>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</section>

<%@include file="/view/user/include/footer.jsp" %>