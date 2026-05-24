<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myOrders.css">



<c:choose>
    <c:when test="${not empty userOrders}">
        <div class="order-tabs-container mb-3">
            <ul class="nav order-tabs border-bottom" id="orderStatusTab" role="tablist">
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link active" id="all-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#all-orders" type="button" role="tab" aria-controls="all-orders"
                            aria-selected="true">
                        Tất cả
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="pending-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#pending-orders" type="button" role="tab"
                            aria-controls="pending-orders" aria-selected="false">
                        Chờ xử lý
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="confirmed-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#confirmed-orders" type="button" role="tab"
                            aria-controls="confirmed-orders" aria-selected="false">
                        Chờ giao hàng
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="shipping-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#shipping-orders" type="button" role="tab"
                            aria-controls="shipping-orders" aria-selected="false">
                        Vận chuyển
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="delivered-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#delivered-orders" type="button" role="tab"
                            aria-controls="delivered-orders" aria-selected="false">
                        Hoàn thành
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="cancelled-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#cancelled-orders" type="button" role="tab"
                            aria-controls="cancelled-orders" aria-selected="false">
                        Đã hủy
                    </button>
                </li>
                <li class="nav-item order-tab-item" role="presentation">
                    <button class="order-tab-link" id="refunded-orders-tab" data-bs-toggle="tab"
                            data-bs-target="#refunded-orders" type="button" role="tab"
                            aria-controls="refunded-orders" aria-selected="false">
                        Trả hàng/Hoàn tiền
                    </button>
                </li>
            </ul>
        </div>

        <!-- Search bar -->
        <div class="search-orders-container mb-4 position-relative">
            <i
                    class="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
            <input type="text" id="orderSearchInput" class="form-control ps-5 py-2.5"
                   placeholder="Bạn có thể tìm kiếm theo ID đơn hàng hoặc Tên sản phẩm..."
                   style="border-radius: 4px; border: 1px solid #e0e0e0; font-size: 0.95rem;">
        </div>

        <!-- Tab contents -->
        <div class="tab-content" id="orderStatusTabContent">
            <!-- TẤT CẢ -->
            <div class="tab-pane fade show active" id="all-orders" role="tabpanel"
                 aria-labelledby="all-orders-tab">
                <c:set var="countAll" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:set var="countAll" value="${countAll + 1}" />
                    <c:set var="st" value="${o.status}" />
                    <%@include file="/view/user/profile/orderCard.jsp" %>
                </c:forEach>
                <c:if test="${countAll == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-receipt text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Chưa có đơn hàng nào</p>
                    </div>
                </c:if>
            </div>

            <!-- CHỜ XỬ LÝ -->
            <div class="tab-pane fade" id="pending-orders" role="tabpanel"
                 aria-labelledby="pending-orders-tab">
                <c:set var="countPending" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'pending'}">
                        <c:set var="countPending" value="${countPending + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countPending == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-wallet2 text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Không có đơn hàng nào đang chờ xử lý</p>
                    </div>
                </c:if>
            </div>

            <!-- CHỜ GIAO HÀNG -->
            <div class="tab-pane fade" id="confirmed-orders" role="tabpanel"
                 aria-labelledby="confirmed-orders-tab">
                <c:set var="countConfirmed" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'confirmed' or o.status == 'processing'}">
                        <c:set var="countConfirmed" value="${countConfirmed + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countConfirmed == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-box-seam text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Không có đơn hàng nào đang chờ giao hàng</p>
                    </div>
                </c:if>
            </div>

            <!-- VẬN CHUYỂN -->
            <div class="tab-pane fade" id="shipping-orders" role="tabpanel"
                 aria-labelledby="shipping-orders-tab">
                <c:set var="countShipping" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'shipping'}">
                        <c:set var="countShipping" value="${countShipping + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countShipping == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-truck text-muted" style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Không có đơn hàng nào đang vận chuyển</p>
                    </div>
                </c:if>
            </div>

            <!-- HOÀN THÀNH -->
            <div class="tab-pane fade" id="delivered-orders" role="tabpanel"
                 aria-labelledby="delivered-orders-tab">
                <c:set var="countDelivered" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'delivered'}">
                        <c:set var="countDelivered" value="${countDelivered + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countDelivered == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-check2-circle text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Chưa có đơn hàng nào hoàn thành</p>
                    </div>
                </c:if>
            </div>

            <!-- ĐÃ HỦY -->
            <div class="tab-pane fade" id="cancelled-orders" role="tabpanel"
                 aria-labelledby="cancelled-orders-tab">
                <c:set var="countCancelled" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'cancelled'}">
                        <c:set var="countCancelled" value="${countCancelled + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countCancelled == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-x-octagon text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Không có đơn hàng nào bị hủy</p>
                    </div>
                </c:if>
            </div>

            <!-- TRẢ HÀNG/HOÀN TIỀN -->
            <div class="tab-pane fade" id="refunded-orders" role="tabpanel"
                 aria-labelledby="refunded-orders-tab">
                <c:set var="countRefunded" value="0" />
                <c:forEach items="${userOrders}" var="o">
                    <c:if test="${o.status == 'refunded'}">
                        <c:set var="countRefunded" value="${countRefunded + 1}" />
                        <c:set var="st" value="${o.status}" />
                        <%@include file="/view/user/profile/orderCard.jsp" %>
                    </c:if>
                </c:forEach>
                <c:if test="${countRefunded == 0}">
                    <div class="text-center py-5 empty-tab-state">
                        <i class="bi bi-arrow-counterclockwise text-muted"
                           style="font-size: 3.5rem; opacity: 0.4;"></i>
                        <p class="text-muted mt-2 mb-0">Không có đơn hàng nào trả hàng hoặc hoàn tiền
                        </p>
                    </div>
                </c:if>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="text-center py-5 shadow-sm bg-white rounded">
            <i class="bi bi-inbox text-muted" style="font-size: 4rem; opacity: 0.3;"></i>
            <p class="text-muted mt-3 fs-5">Hiện chưa có đơn hàng nào của bạn.</p>
            <a href="${pageContext.request.contextPath}/product" class="btn btn-success px-4 mt-2">
                <i class="bi bi-cart3 me-2"></i>Mua sắm ngay
            </a>
        </div>
    </c:otherwise>
</c:choose>

<!-- Review Modal -->
<div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 8px;">
            <div class="modal-header border-bottom-0 py-3">
                <h5 class="modal-title fw-bold" id="reviewModalLabel">Đánh giá sản phẩm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <form id="reviewForm">
                <div class="modal-body py-2">
                    <p class="mb-3 text-muted">Sản phẩm: <strong id="reviewProductName"
                                                                 class="text-dark"></strong></p>
                    <div class="mb-3">
                        <label class="form-label d-block fw-bold text-dark mb-2">Chất lượng sản
                            phẩm</label>
                        <div class="star-rating">
                            <input type="radio" id="star5" name="rating" value="5" checked /><label
                                for="star5" title="5 stars"><i class="bi bi-star-fill"></i></label>
                            <input type="radio" id="star4" name="rating" value="4" /><label for="star4"
                                                                                            title="4 stars"><i class="bi bi-star-fill"></i></label>
                            <input type="radio" id="star3" name="rating" value="3" /><label for="star3"
                                                                                            title="3 stars"><i class="bi bi-star-fill"></i></label>
                            <input type="radio" id="star2" name="rating" value="2" /><label for="star2"
                                                                                            title="2 stars"><i class="bi bi-star-fill"></i></label>
                            <input type="radio" id="star1" name="rating" value="1" /><label for="star1"
                                                                                            title="1 star"><i class="bi bi-star-fill"></i></label>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="reviewContent" class="form-label fw-bold text-dark">Cảm nhận của bạn
                            <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="reviewContent" name="content" rows="4"
                                  required placeholder="Sản phẩm tuyệt vời, giao hàng nhanh..."></textarea>
                    </div>
                    <input type="hidden" id="reviewProductId" name="productId">
                </div>
                <div class="modal-footer border-top-0 py-3">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-success px-4" id="btnSubmitReview">Gửi đánh
                        giá</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Cancel Order Modal -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" aria-labelledby="cancelOrderModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 8px;">
            <div class="modal-header border-bottom-0 py-3">
                <h5 class="modal-title fw-bold" id="cancelOrderModalLabel">Hủy đơn hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <form id="cancelOrderForm">
                <div class="modal-body py-2">
                    <p class="mb-3 text-muted">Đơn hàng: <strong id="cancelOrderIdDisplay"
                                                                 class="text-dark"></strong></p>
                    <div class="mb-3">
                        <label for="cancelReason" class="form-label fw-bold text-dark">Lý do hủy đơn
                            <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="cancelReason" name="reason" rows="4" required
                                  minlength="5"
                                  placeholder="Vui lòng cho chúng tôi biết lý do bạn hủy đơn..."></textarea>
                        <div class="invalid-feedback">
                            Vui lòng nhập lý do hủy đơn (ít nhất 5 ký tự).
                        </div>
                    </div>
                    <input type="hidden" id="cancelOrderIdInput" name="orderId">
                </div>
                <div class="modal-footer border-top-0 py-3">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-danger px-4" id="btnSubmitCancel">Xác nhận
                        hủy</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Refund Request Modal -->
<div class="modal fade" id="refundOrderModal" tabindex="-1" aria-labelledby="refundOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 8px;">
            <div class="modal-header border-bottom-0 py-3">
                <h5 class="modal-title fw-bold" id="refundOrderModalLabel">Yêu cầu trả hàng/hoàn tiền</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="refundForm">
                <div class="modal-body py-2">
                    <p class="mb-3 text-muted">Đơn hàng: <strong id="refundOrderIdDisplay" class="text-dark"></strong></p>
                    <div class="mb-3">
                        <label for="refundReason" class="form-label fw-bold text-dark">Lý do trả hàng/hoàn tiền
                            <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="refundReason" name="reason" rows="4" required minlength="5"
                                  placeholder="Vui lòng cung cấp lý do..."></textarea>
                        <div class="invalid-feedback">Vui lòng nhập lý do (ít nhất 5 ký tự).</div>
                    </div>
                    <input type="hidden" id="refundOrderIdInput" name="orderId">
                </div>
                <div class="modal-footer border-top-0 py-3">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-juicy-refund px-4" id="btnSubmitRefund">Gửi yêu cầu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/myorders.js"></script>