<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="statusClass"
       value="${st == 'confirmed' ? 'status-confirmed' :
               st == 'processing' ? 'status-processing' :
               st == 'shipping' ? 'status-shipping' :
               st == 'delivered' ? 'status-delivered' :
               st == 'cancelled' ? 'status-cancelled' :
               st == 'refunded' ? 'status-refunded' : 'status-processing'}"/>

<c:choose>
    <c:when test="${st == 'pending'}">
        <c:set var="statusText" value="Chờ xử lý"/>
    </c:when>
    <c:when test="${st == 'confirmed'}">
        <c:set var="statusText" value="Chờ giao hàng"/>
    </c:when>
    <c:when test="${st == 'processing'}">
        <c:set var="statusText" value="Đang xử lý"/>
    </c:when>
    <c:when test="${st == 'shipping'}">
        <c:set var="statusText" value="Đang vận chuyển"/>
    </c:when>
    <c:when test="${st == 'delivered'}">
        <c:set var="statusText" value="Hoàn thành"/>
    </c:when>
    <c:when test="${st == 'cancelled'}">
        <c:set var="statusText" value="Đã hủy"/>
    </c:when>
    <c:when test="${st == 'refunded'}">
        <c:set var="statusText" value="Trả hàng/Hoàn tiền"/>
    </c:when>
    <c:otherwise>
        <c:set var="statusText" value="${st}"/>
    </c:otherwise>
</c:choose>

<div class="card order-card shadow-sm mb-3" data-order-id="${o.id}">
    <div class="card-header d-flex flex-wrap justify-content-between align-items-center gap-2 py-3">
        <div class="d-flex align-items-center gap-2">
            <span class="fw-bold">Mã đơn #${o.id}</span>
            <span class="status-pill ${statusClass}">${statusText}</span>
        </div>
    </div>

    <div class="card-body">
        <!-- Products List -->
        <div class="order-items-preview mb-3 pb-3 border-bottom">
            <c:if test="${not empty o.items}">
                <c:set var="firstItem" value="${o.items[0]}"/>
                <div class="d-flex align-items-center mb-2">
                    <img src="${firstItem.productImg}" alt="${firstItem.productName}"
                         class="img-thumbnail me-2" style="width: 60px; height: 60px; object-fit: cover;">
                    <div class="flex-grow-1">
                        <span class="fw-bold text-dark">${firstItem.productName}</span>
                        <span class="text-muted ms-2">${firstItem.volume}ml</span>
                        <span class="text-muted ms-2">x${firstItem.quantity}</span>
                    </div>
                    <c:if test="${st == 'delivered'}">
                        <button class="btn btn-success btn-sm review-btn ms-2"
                                data-bs-toggle="modal"
                                data-bs-target="#reviewModal"
                                data-product-id="${firstItem.productId}"
                                data-product-name="${firstItem.productName}">
                            <i class="bi bi-star me-1"></i>Đánh giá
                        </button>
                    </c:if>
                </div>

                <c:if test="${fn:length(o.items) > 1}">
                    <button class="btn btn-link btn-sm p-0 text-decoration-none mt-1 text-dark"
                            type="button" data-bs-toggle="collapse" data-bs-target="#collapseItems${o.id}"
                            aria-expanded="false" aria-controls="collapseItems${o.id}">
                        Xem thêm ${fn:length(o.items) - 1} sản phẩm <i class="bi bi-chevron-down"></i>
                    </button>
                    <div class="collapse mt-2" id="collapseItems${o.id}">
                        <c:forEach items="${o.items}" var="item" begin="1">
                            <div class="d-flex align-items-center mt-2 pt-2 border-top">
                                <img src="${item.productImg}" alt="${item.productName}"
                                     class="img-thumbnail me-2"
                                     style="width: 50px; height: 50px; object-fit: cover;">
                                <div class="flex-grow-1">
                                    <span class="fw-bold text-dark">${item.productName}</span>
                                    <span class="text-muted ms-2">${item.volume}ml</span>
                                    <span class="text-muted ms-2">x${item.quantity}</span>
                                </div>
                                <c:if test="${st == 'delivered'}">
                                    <button class="btn btn-primary btn-sm review-btn ms-2"
                                            data-bs-toggle="modal"
                                            data-bs-target="#reviewModal"
                                            data-product-id="${item.productId}"
                                            data-product-name="${item.productName}">
                                        <i class="bi bi-star me-1"></i>Đánh giá
                                    </button>
                                </c:if>
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
                        <button class="btn btn-outline-danger btn-sm btn-cancel-order" type="button"
                                data-order-id="${o.id}">
                            <i class="bi bi-x-circle me-1"></i>Hủy đơn
                        </button>
                    </c:if>
                    <c:if test="${st == 'delivered' or st == 'cancelled' or st == 'refunded'}">
                        <button class="btn btn-outline-success btn-sm btn-reorder" type="button"
                                data-order-id="${o.id}"><i class="bi bi-arrow-repeat me-1"></i>Mua lại</button>
                    </c:if>
                    <c:if test="${st == 'delivered'}">
                        <button class="btn btn-outline-danger btn-sm request-refund-btn ms-2" type="button"
                                data-order-id="${o.id}"><i class="bi bi-arrow-counterclockwise me-1"></i>Yêu cầu trả hàng/hoàn tiền</button>
                    </c:if>
                    <c:if test="${st == 'confirmed' or st == 'processing' or st == 'shipping' or st == 'delivered'}">
                        <button class="btn btn-outline-info btn-sm btn-track-order ms-2" type="button"
                                data-order-id="${o.id}"><i class="bi bi-geo-alt me-1"></i>Theo dõi đơn hàng</button>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
