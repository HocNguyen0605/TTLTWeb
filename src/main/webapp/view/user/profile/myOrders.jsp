<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="orders-toolbar mb-3">
    <div class="d-flex align-items-center gap-2">
        <i class="bi bi-receipt-cutoff text-success"></i>
        <span class="fw-bold">Danh sách đơn hàng</span>
    </div>
    <div class="text-muted small">
        * UI demo: nút chức năng hiển thị theo trạng thái (không xử lý backend).
    </div>
</div>

<c:set var="hasOrders" value="${not empty orders}"/>

<c:choose>
    <c:when test="${hasOrders}">
        <c:forEach items="${orders}" var="o">
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
                        <span class="fw-bold">Order #${o.id}</span>
                        <span class="status-pill ${statusClass}">${st}</span>
                    </div>
                    <button class="btn btn-outline-success btn-sm rounded-pill" type="button" disabled>
                        <i class="bi bi-eye me-1"></i>Xem chi tiết
                    </button>
                </div>

                <div class="card-body">
                    <div class="order-kv">
                        <div>
                            <div class="kv-label">Item name</div>
                            <div class="kv-value">${o.itemName}</div>
                        </div>
                        <div>
                            <div class="kv-label">Order date</div>
                            <div class="kv-value">
                                <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        <div>
                            <div class="kv-label">Total price</div>
                            <div class="kv-value text-success">
                                <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>
                        <div class="text-md-end">
                            <div class="kv-label">Actions</div>
                            <div class="order-actions mt-2 mt-md-0">

                                <c:if test="${st == 'Confirmed' or st == 'Processing'}">
                                    <button class="btn btn-outline-danger btn-sm" type="button" disabled>
                                        <i class="bi bi-x-circle me-1"></i>Hủy đơn
                                    </button>
                                </c:if>

                                <c:if test="${st == 'Delivered' or st == 'Cancelled' or st == 'Refunded'}">
                                    <button class="btn btn-warning btn-sm" type="button" disabled>
                                        <i class="bi bi-arrow-repeat me-1"></i>Mua lại
                                    </button>
                                </c:if>

                                <c:if test="${st == 'Delivered'}">
                                    <button class="btn btn-outline-primary btn-sm" type="button" disabled>
                                        <i class="bi bi-star me-1"></i>Đánh giá
                                    </button>
                                </c:if>

                                <c:if test="${st == 'Refunded'}">
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
        <c:set var="demoStatusList" value="Confirmed,Processing,Shipping,Delivered,Cancelled,Refunded"/>

        <c:forEach var="st" items="${fn:split(demoStatusList, ',')}" varStatus="loop">
            <c:set var="statusClass"
                   value="${st == 'Confirmed' ? 'status-confirmed' :
                           st == 'Processing' ? 'status-processing' :
                           st == 'Shipping' ? 'status-shipping' :
                           st == 'Delivered' ? 'status-delivered' :
                           st == 'Cancelled' ? 'status-cancelled' :
                           st == 'Refunded' ? 'status-refunded' : 'status-processing'}"/>

            <div class="card order-card shadow-sm mb-3">
                <div class="card-header d-flex flex-wrap justify-content-between align-items-center gap-2 py-3">
                    <div class="d-flex align-items-center gap-2">
                        <span class="fw-bold">Order #${1001 + loop.index}</span>
                        <span class="status-pill ${statusClass}">${st}</span>
                    </div>
                    <button class="btn btn-outline-success btn-sm rounded-pill" type="button" disabled>
                        <i class="bi bi-eye me-1"></i>Xem chi tiết
                    </button>
                </div>

                <div class="card-body">
                    <div class="order-kv">
                        <div>
                            <div class="kv-label">Item name</div>
                            <div class="kv-value">Mixed Fruit Box</div>
                        </div>
                        <div>
                            <div class="kv-label">Order date</div>
                            <div class="kv-value">08/05/2026</div>
                        </div>
                        <div>
                            <div class="kv-label">Total price</div>
                            <div class="kv-value text-success">250.000₫</div>
                        </div>
                        <div class="text-md-end">
                            <div class="kv-label">Actions</div>
                            <div class="order-actions mt-2 mt-md-0">
                                <c:if test="${st == 'Confirmed' or st == 'Processing'}">
                                    <button class="btn btn-outline-danger btn-sm" type="button" disabled>
                                        <i class="bi bi-x-circle me-1"></i>Hủy đơn
                                    </button>
                                </c:if>

                                <c:if test="${st == 'Delivered' or st == 'Cancelled' or st == 'Refunded'}">
                                    <button class="btn btn-warning btn-sm" type="button" disabled>
                                        <i class="bi bi-arrow-repeat me-1"></i>Mua lại
                                    </button>
                                </c:if>

                                <c:if test="${st == 'Delivered'}">
                                    <button class="btn btn-outline-primary btn-sm" type="button" disabled>
                                        <i class="bi bi-star me-1"></i>Đánh giá
                                    </button>
                                </c:if>
                                <c:if test="${st == 'Refunded'}">
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
    </c:otherwise>
</c:choose>