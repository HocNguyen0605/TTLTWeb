<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
    <jsp:param name="activePage" value="home" />
</jsp:include>
<body>

<div class="container my-5">
    <h2 class="text-center text-success fw-bold mb-4">
        Thông Tin Thanh Toán
    </h2>

    <div class="row g-4">

        <!-- FORM THÔNG TIN KHÁCH -->
        <div class="col-md-7">
            <form action="${pageContext.request.contextPath}/order"
                  method="post"
                  class="p-4 shadow rounded bg-white">

            <div class="mb-3">
                    <label class="form-label fw-semibold">Họ và tên</label>
                    <input type="text" name="fullName"
                           class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <input type="tel" name="phone"
                           class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Địa chỉ giao hàng</label>
                    <input type="text" name="address"
                           class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Phương thức thanh toán</label>
                    <select name="paymentMethod" class="form-select" required>
                        <option value="">-- Chọn --</option>
                        <option value="COD">Thanh toán khi nhận hàng (COD)</option>
                        <option value="BANK">Chuyển khoản ngân hàng</option>
                        <option value="EWALLET">Ví điện tử</option>
                    </select>
                </div>

                <button class="btn btn-success w-100 rounded-pill fw-semibold">
                    Xác Nhận Đặt Hàng
                </button>
            </form>
        </div>

        <!-- TÓM TẮT ĐƠN HÀNG -->
        <div class="col-md-5">
            <div class="p-4 shadow rounded bg-white">
                <h5 class="fw-bold mb-3 text-success">Đơn Hàng</h5>

                <c:forEach var="item" items="${sessionScope.cart.items}">
                    <div class="d-flex justify-content-between mb-2">
                        <span>
                            ${item.product.name}
                            (${item.product.volume}ml) x ${item.quantity}
                        </span>
                        <span>
                            <fmt:formatNumber value="${item.totalPrice}"
                                              type="currency"
                                              currencySymbol="đ"
                                              maxFractionDigits="0"/>
                        </span>
                    </div>
                </c:forEach>

                <hr>

                <c:set var="shippingFee" value="15000"/>

                <div class="d-flex justify-content-between">
                    <span>Tạm tính</span>
                    <span>
                        <fmt:formatNumber value="${sessionScope.cart.totalPrice}"
                                          type="currency"
                                          currencySymbol="đ"
                                          maxFractionDigits="0"/>
                    </span>
                </div>

                <div class="d-flex justify-content-between">
                    <span>Phí giao hàng</span>
                    <span>
                        <fmt:formatNumber value="${shippingFee}"
                                          type="currency"
                                          currencySymbol="đ"
                                          maxFractionDigits="0"/>
                    </span>
                </div>

                <div class="d-flex justify-content-between fw-bold text-success mt-2">
                    <span>Tổng cộng</span>
                    <span>
                        <fmt:formatNumber
                                value="${sessionScope.cart.totalPrice + shippingFee}"
                                type="currency"
                                currencySymbol="đ"
                                maxFractionDigits="0"/>
                    </span>
                </div>
            </div>
        </div>

    </div>
</div>
<%@include file="/view/user/include/footer.jsp" %>
