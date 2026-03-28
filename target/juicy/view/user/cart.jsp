<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/view/user/include/header.jsp">
    <jsp:param name="title" value="Trang Chủ" />
</jsp:include>

<!-- GIỎ HÀNG -->
<section class="container my-5">
    <h2 class="text-center text-success fw-bold mb-4">Giỏ Hàng Của Bạn</h2>

    <div class="row g-4">
        <!-- Danh sách sản phẩm -->
        <div class="col-lg-8">
            <div class="table-responsive shadow rounded bg-white p-3">
                <table class="table align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>Sản Phẩm</th>
                        <th class="text-center">Số Lượng</th>
                        <th class="text-end">Đơn Giá</th>
                        <th class="text-end">Thành Tiền</th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>

                        <%-- Đưa comment vào trong hoặc xóa đi --%>
                        <c:when test="${empty sessionScope.cart || empty sessionScope.cart.allItems}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">
                                    🛒 Giỏ hàng đang trống
                                </td>
                            </tr>
                        </c:when>

                        <%-- Có sản phẩm --%>
                        <c:otherwise>
                            <c:forEach items="${sessionScope.cart.allItems}" var="item">
                                <tr>
                                    <%-- Sản phẩm --%>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${item.product.img}"
                                                 width="60"
                                                 class="rounded me-3"
                                                 alt="${item.product.name}">
                                            <div>
                                                <h6 class="fw-semibold mb-0">
                                                        ${item.product.name}
                                                </h6>
                                                <small class="text-muted">
                                                        ${item.product.volume} ml
                                                </small>
                                            </div>
                                        </div>
                                    </td>

                                    <%-- Số lượng --%>
                                    <td class="text-center">
                                        <form action="${pageContext.request.contextPath}/cart"
                                              method="post"
                                              class="d-inline">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="productId"
                                                   value="${item.product.id}">
                                            <input type="number"
                                                   name="quantity"
                                                   value="${item.quantity}"
                                                   min="1"
                                                   class="form-control text-center"
                                                   style="width:70px"
                                                   onchange="this.form.submit()">
                                        </form>
                                    </td>

                                    <%-- Đơn giá --%>
                                    <td class="text-end">
                                        <fmt:formatNumber value="${item.price}"
                                                          type="currency"
                                                          currencySymbol="đ"
                                                          maxFractionDigits="0"/>
                                    </td>

                                    <%-- Thành tiền --%>
                                    <td class="text-end fw-bold text-success">
                                        <fmt:formatNumber value="${item.totalPrice}"
                                                          type="currency"
                                                          currencySymbol="đ"
                                                          maxFractionDigits="0"/>
                                    </td>

                                    <%-- Xóa --%>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/cart"
                                              method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId"
                                                   value="${item.product.id}">
                                            <button class="btn btn-sm btn-outline-danger">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>

                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <c:set var="shippingFee" value="15000"/>

        <div class="col-lg-4">
            <div class="shadow rounded bg-white p-4">
                <h5 class="fw-bold mb-3 text-success">Tổng Đơn Hàng</h5>

                <!-- Tạm tính -->
                <div class="d-flex justify-content-between mb-2">
                    <span>Tạm tính:</span>
                    <span>
                <fmt:formatNumber value="${sessionScope.cart.totalPrice}"
                                  type="currency"
                                  currencySymbol="đ"
                                  maxFractionDigits="0"/>
            </span>
                </div>

                <!-- Phí ship -->
                <div class="d-flex justify-content-between mb-2">
                    <span>Phí giao hàng:</span>
                    <span>
                <fmt:formatNumber value="${shippingFee}"
                                  type="currency"
                                  currencySymbol="đ"
                                  maxFractionDigits="0"/>
            </span>
                </div>

                <div class="d-flex justify-content-between fw-bold border-top pt-2">
                    <span>Giảm Giá:</span>
                    <span class="text-danger"> <fmt:formatNumber
                            value="${not empty sessionScope.voucher ? sessionScope.voucher.discountValue : 0}"
                            type="currency"
                            currencySymbol="đ"
                            maxFractionDigits="0"/>
    </span>
                </div>

                <div class="d-flex justify-content-between fw-bold border-top pt-2">
                    <span>Tổng Đơn:</span>
                    <span class="text-success fs-5">
        <fmt:formatNumber
                value="${sessionScope.cart.totalPrice + shippingFee }"
                type="currency"
                currencySymbol="đ"
                maxFractionDigits="0"/>
    </span>
                </div>
                <div class="d-flex justify-content-between fw-bold border-top pt-2">
                    <span>TỔNG CỘNG:</span>
                    <span class="text-success">
                <fmt:formatNumber
                        value="${(sessionScope.cart.totalPrice + shippingFee - (not empty sessionScope.voucher ? sessionScope.voucher.discountValue : 0)) < 0 ? 0
                          : (sessionScope.cart.totalPrice + shippingFee - (not empty sessionScope.voucher ? sessionScope.voucher.discountValue : 0))}"                        type="currency"
                        currencySymbol="đ"
                        maxFractionDigits="0"/>
            </span>
                </div>
                <!-- Mã giảm giá voucher -->
                <div class="mt-3">
                    <label class="form-label fw-semibold">Mã giảm giá</label>
                    <form action="${pageContext.request.contextPath}/apply-voucher" method="post" class="input-group">
                        <input type="text" class="form-control" name="codeVoucher" placeholder="Nhập mã..." maxlength="15">
                        <button type="submit" class="btn btn-outline-success">
                            Áp dụng
                        </button>
                    </form>
                </div>

                <!-- Thanh toán -->
                <c:choose>
                    <c:when test="${sessionScope.cart == null || empty sessionScope.cart}">
                        <button class="btn btn-secondary w-100 mt-4 rounded-pill" disabled>
                            Giỏ hàng trống
                        </button>
                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.request.contextPath}/order" method="post">
                            <input type="hidden" name="action" value="prepare">
                            <button type="submit" class="btn btn-success w-100 mt-4 fw-semibold rounded-pill">
                                <i class="bi bi-credit-card me-1"></i> Thanh Toán Ngay
                            </button>
                        </form>

                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <%--Modal xác nhận thông tin đặt hàng; ẩn đi, khi nào orderFlag có giá trị show sẽ được js set lại --%>
        <div class="modal fade" id="checkoutModal" tabindex="-1" aria-labelledby="checkoutModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered"> <div class="modal-content shadow-lg border-0">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title fw-bold" id="checkoutModalLabel">
                        <i class="bi bi-cart-check me-2"></i>Thông Tin Đặt Hàng
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <form action="${pageContext.request.contextPath}/order" method="post">
                        <input type="hidden" name="action" value="confirm">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Họ và tên người nhận</label>
                            <input type="text" name="receiverName" class="form-control"
                                   value="${sessionScope.auth.fullName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Địa chỉ nhận hàng</label>
                            <input type="text" name="address" class="form-control" placeholder="Số nhà, tên đường..." required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Số điện thoại</label>
                            <input type="tel" name="phone" class="form-control" placeholder="0123456789" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Phương thức thanh toán</label>
                            <select name="paymentMethod" class="form-select" required>
                                <option value="COD">Thanh toán khi nhận hàng (COD)</option>
                                <option value="BANKING">Chuyển khoản ngân hàng</option>
                            </select>
                        </div>
                        <hr>
                        <button type="submit" class="btn btn-success w-100 rounded-pill fw-bold">
                            XÁC NHẬN ĐẶT HÀNG
                        </button>
                    </form>
                </div>
            </div>
            </div>
        </div>
        <c:if test="${sessionScope.orderFlag == 'show'}">
        <input type="hidden" id="triggerModalFlag" value="true">
            <%-- Xóa cờ ngay để không bị hiện lại khi F5 --%>
            <c:remove var="orderFlag" scope="session" />
        </c:if>
        <%@include file="/view/user/include/footer.jsp" %>l
