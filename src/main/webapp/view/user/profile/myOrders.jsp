<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="orders-toolbar mb-3">
    <div class="d-flex align-items-center gap-2">
        <i class="bi bi-receipt-cutoff text-success"></i>
        <span class="fw-bold">Danh sách đơn hàng</span>
    </div>
</div>

<c:set var="hasOrders" value="${not empty orders}"/>

<c:choose>
    <c:when test="${not empty userOrders}">
        <c:forEach items="${userOrders}" var="o">
            <c:set var="st" value="${o.status}"/>

            <c:set var="statusClass"
                   value="${st == 'confirmed' ? 'status-confirmed' :
                           st == 'processing' ? 'status-processing' :
                           st == 'shipping' ? 'status-shipping' :
                           st == 'delivered' ? 'status-delivered' :
                           st == 'cancelled' ? 'status-cancelled' :
                           st == 'refunded' ? 'status-refunded' : 'status-processing'}"/>

            <div class="card order-card shadow-sm mb-3">
                <div class="card-header d-flex flex-wrap justify-content-between align-items-center gap-2 py-3">
                    <div class="d-flex align-items-center gap-2">
                        <span class="fw-bold">Mã đơn #${o.id}</span>
                        <span class="status-pill ${statusClass}">${st}</span>
                    </div>
                </div>

                <div class="card-body">
                    <!-- Products List -->
                    <div class="order-items-preview mb-3 pb-3 border-bottom">
                        <c:if test="${not empty o.items}">
                            <c:set var="firstItem" value="${o.items[0]}" />
                            <div class="d-flex align-items-center mb-2">
                                <img src="${firstItem.productImg}" alt="${firstItem.productName}" class="img-thumbnail me-2" style="width: 60px; height: 60px; object-fit: cover;">
                                <div class="flex-grow-1">
                                    <span class="fw-bold">${firstItem.productName}</span>
                                    <span class="text-muted ms-2">${firstItem.volume}ml</span>
                                    <span class="text-muted ms-2">x${firstItem.quantity}</span>
                                </div>
                            </div>

                            <c:if test="${fn:length(o.items) > 1}">
                                <button class="btn btn-link btn-sm p-0 text-decoration-none mt-1 text-dark" type="button" data-bs-toggle="collapse" data-bs-target="#collapseItems${o.id}" aria-expanded="false" aria-controls="collapseItems${o.id}">
                                    Xem thêm ${fn:length(o.items) - 1} sản phẩm <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="collapse mt-2" id="collapseItems${o.id}">
                                    <c:forEach items="${o.items}" var="item" begin="1">
                                        <div class="d-flex align-items-center mt-2 pt-2 border-top">
                                            <img src="${item.productImg}" alt="${item.productName}" class="img-thumbnail me-2" style="width: 50px; height: 50px; object-fit: cover;">
                                            <div class="flex-grow-1">
                                                <span class="fw-bold">${item.productName}</span>
                                                <span class="text-muted ms-2">${item.volume}ml</span>
                                                <span class="text-muted ms-2">x${item.quantity}</span>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </c:if>
                    </div>

                    <div class="order-kv">

                        <div>
                            <div class="kv-label">Ngày đặt</div>
                            <div class="kv-value">
                                <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        <div>
                            <div class="kv-label">Ngày giao</div>
                            <div class="kv-value">
                                <c:choose>
                                    <c:when test="${not empty o.deliveredDate}">
                                        <fmt:formatDate value="${o.deliveredDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="text-muted" style="font-size: 0.9em;">Chưa có</i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div>
                            <div class="kv-label">Tổng tiền</div>
                            <div class="kv-value text-success">
                                <fmt:formatNumber value="${o.totalPrice}" pattern="#,###đ"/>
                            </div>
                        </div>
                        <div class="text-md-end">
                            <div class="order-actions mt-2 mt-md-0">

                                <c:if test="${st == 'confirmed' or st == 'pending'}">
                                    <button class="btn btn-outline-danger btn-sm btn-cancel-order" type="button" data-order-id="${o.id}">
                                        <i class="bi bi-x-circle me-1"></i>Hủy đơn
                                    </button>
                                </c:if>

                                <c:if test="${st == 'delivered' or st == 'cancelled' or st == 'refunded'}">
                                    <button class="btn btn-warning btn-sm" type="button" disabled>
                                        <i class="bi bi-arrow-repeat me-1"></i>Mua lại
                                    </button>
                                </c:if>

                                <c:if test="${st == 'delivered'}">
                                    <button class="btn btn-outline-primary btn-sm" type="button" disabled>
                                        <i class="bi bi-star me-1"></i>Đánh giá
                                    </button>
                                </c:if>

                                <c:if test="${st == 'refunded'}">
                                    <button class="btn btn-outline-secondary btn-sm" type="button" disabled>
                                        <i class="bi bi-cash-coin me-1"></i>Chi tiết hoàn tiền
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div class="text-center py-5">
            <p class="text-muted">Hiện chưa có đơn hàng nào.</p>
        </div>
    </c:otherwise>
</c:choose>

<!-- Cancel Order -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" aria-labelledby="cancelOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cancelOrderModalLabel">Hủy Đơn Hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="cancelOrderForm">
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn hủy đơn hàng <strong id="cancelOrderIdDisplay"></strong> không?</p>
                    <div class="mb-3">
                        <label for="cancelReason" class="form-label">Lý do hủy đơn (tối thiểu 10 ký tự) <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="cancelReason" name="reason" rows="3" required minlength="10" placeholder="Vui lòng cho chúng tôi biết lý do bạn hủy đơn..."></textarea>
                        <div class="invalid-feedback">
                            Vui lòng nhập lý do hủy đơn (ít nhất 10 ký tự).
                        </div>
                    </div>
                    <input type="hidden" id="cancelOrderIdInput" name="orderId">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-danger" id="btnSubmitCancel">Xác nhận hủy</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/myorders.js"></script>