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
                    <a href="order-detail?id=${o.id}" class="btn btn-outline-success btn-sm rounded-pill">
                        <i class="bi bi-eye me-1"></i>Xem chi tiết
                    </a>
                </div>

                <div class="card-body">
                    <div class="order-kv">
                        <div>
                            <div class="kv-label">Tên sản phẩm</div>
                            <div class="kv-value">${o.itemName}</div>
                        </div>
                        <div>
                            <div class="kv-label">Ngày đặt</div>
                            <div class="kv-value">
                                <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        <div>
                            <div class="kv-label">Tổng tiền</div>
                            <div class="kv-value text-success">
                                <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>
                        <div class="text-md-end">
                            <div class="order-actions mt-2 mt-md-0">

                                <c:if test="${st == 'confirmed' or st == 'processing'}">
                                    <button class="btn btn-outline-danger btn-sm" type="button" disabled>
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
                                        <i class="bi bi-cash-coin me-1"></i>Hoàn tiền
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