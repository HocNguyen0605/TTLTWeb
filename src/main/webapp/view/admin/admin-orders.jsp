<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
                <title>Juicy - Trái Cây Tươi Ngon &amp; Nước Ép</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            </head>

            <body>
                <!-- HEADER -->
                <header class="sticky-top shadow-sm">
                    <nav class="navbar navbar-expand-lg navbar-light bg-white py-3">
                        <div class="container">
                            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                data-bs-target="#navbarNav">
                                <span class="navbar-toggler-icon"></span>
                            </button>
                            <a class="navbar-brand fw-bold text-success fs-3"
                                href="${pageContext.request.contextPath}/admin/dashboard">
                                <img src="${pageContext.request.contextPath}/images/logo/logo-juicy.png"
                                    alt="Juicy Logo" height="40" class="me-2">
                                JUICY <span class="text-warning"></span>
                            </a>

                            <div class="navbar-collapse show" id="navbarNav">
                                <ul class="navbar-nav ms-auto align-items-lg-center">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">Sản
                                            Phẩm</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link active"
                                            href="${pageContext.request.contextPath}/admin/manage-orders">Đơn hàng</a>
                                    </li>
                                </ul>
                                <a href="${pageContext.request.contextPath}/logout"
                                    class="btn btn-outline-success rounded-pill fw-semibold ms-lg-3 my-2 my-lg-0 me-lg-2">
                                    <i class="bi bi-person-circle me-1"></i> Đăng Xuất
                                </a>
                            </div>
                        </div>
                    </nav>
                </header>
                <div class="container my-4">

                    <h3 class="mb-4 text-success fw-bold">Danh sách đơn hàng</h3>

                    <div class="card shadow-sm">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Ngày đặt</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach items="${orders}" var="o">
                                            <tr>
                                                <td>
                                                    <a href="javascript:void(0)" onclick="viewOrderDetail(${o.id})"
                                                        class="text-decoration-none fw-bold text-success">
                                                        #DH${o.id}
                                                    </a>
                                                </td>
                                                <td>
                                                    ${o.customerName}
                                                    <c:if test="${o.userId > 0}">
                                                        <br><small class="text-muted" style="font-size: 0.85em;">ID:
                                                            ${o.userId}</small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                                                </td>
                                                <td class="text-success fw-bold">
                                                    <fmt:formatNumber value="${o.totalPrice}" type="currency"
                                                        currencySymbol="₫" />
                                                </td>
                                                <td>
                                                    <form method="post" action="manage-orders" class="m-0">
                                                        <input type="hidden" name="action" value="updateStatus" />
                                                        <input type="hidden" name="orderId" value="${o.id}" />

                                                        <select name="status" onchange="this.form.submit()" class="form-select form-select-sm border-0 fw-bold rounded-3
                                                    ${o.status == 'Đã giao hàng' ? 'text-success bg-success-subtle' :
                                                      o.status == 'Đang giao hàng' ? 'text-primary bg-primary-subtle' :
                                                      o.status == 'Đã hủy' ? 'text-danger bg-danger-subtle' :
                                                      o.status == 'Bị hoàn' ? 'text-danger bg-danger-subtle' :
                                                      'text-warning bg-warning-subtle'}" style="width: 160px;">
                                                            <option value="Chờ xác nhận" ${o.status=='Chờ xác nhận'
                                                                ? 'selected' : '' }>Chờ xác nhận</option>
                                                            <option value="Đang xác nhận" ${o.status=='Đang xác nhận'
                                                                ? 'selected' : '' }>Đang xác nhận</option>
                                                            <option value="Đang giao hàng" ${o.status=='Đang giao hàng'
                                                                ? 'selected' : '' }>Đang giao hàng</option>
                                                            <option value="Đã giao hàng" ${o.status=='Đã giao hàng'
                                                                ? 'selected' : '' }>Đã giao hàng</option>
                                                            <option value="Bị hoàn" ${o.status=='Bị hoàn' ? 'selected'
                                                                : '' }>Bị hoàn</option>
                                                            <option value="Đã hủy" ${o.status=='Đã hủy' ? 'selected'
                                                                : '' }>Đã hủy</option>
                                                        </select>
                                                    </form>
                                                </td>

                                                <td>
                                                    <form method="post" action="manage-orders"
                                                        onsubmit="return confirm('Xóa đơn hàng này?')" class="m-0">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="orderId" value="${o.id}" />
                                                        <button
                                                            class="btn btn-outline-danger btn-sm rounded-circle d-flex align-items-center justify-content-center"
                                                            style="width: 32px; height: 32px;" title="Xóa">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <c:if test="${empty orders}">
                        <div class="text-center py-5">
                            <i class="bi bi-inbox fs-1 text-muted"></i>
                            <p class="text-muted mt-2">Chưa có đơn hàng nào.</p>
                        </div>
                    </c:if>

                </div>
                </div>

                <!-- Modal Chi Tiết Đơn Hàng -->
                <div class="modal fade" id="orderDetailModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title fw-bold text-success">Thông tin nhận hàng</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div id="modalLoading" class="text-center py-3">
                                    <div class="spinner-border text-success" role="status">
                                        <span class="visually-hidden">Loading...</span>
                                    </div>
                                </div>
                                <div id="modalContent" style="display: none;">
                                    <div class="mb-3">
                                        <label class="fw-bold">Người nhận:</label>
                                        <div id="dName" class="text-dark"></div>
                                    </div>
                                    <div class="mb-3">
                                        <label class="fw-bold">Số điện thoại:</label>
                                        <div id="dPhone" class="text-dark"></div>
                                    </div>
                                    <div class="mb-3">
                                        <label class="fw-bold">Địa chỉ:</label>
                                        <div id="dAddress" class="text-dark"></div>
                                    </div>
                                    <div class="mb-3">
                                        <label class="fw-bold">Ghi chú:</label>
                                        <div id="dNote" class="text-dark fst-italic"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script type="module" src="${pageContext.request.contextPath}/js/init.js"></script>

                <script>
                    function viewOrderDetail(orderId) {
                        const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
                        const loading = document.getElementById('modalLoading');
                        const content = document.getElementById('modalContent');

                        // Show modal & loading
                        modal.show();
                        loading.style.display = 'block';
                        content.style.display = 'none';

                        // Fetch API
                        fetch('${pageContext.request.contextPath}/api/order-detail?id=' + orderId)
                            .then(response => response.json())
                            .then(data => {
                                if (data.error) {
                                    alert(data.error);
                                    return;
                                }
                                document.getElementById('dName').innerText = data.receiverName || '---';
                                document.getElementById('dPhone').innerText = data.receiverPhone || '---';
                                document.getElementById('dAddress').innerText = data.address || '---';
                                document.getElementById('dNote').innerText = data.note || 'Không có ghi chú';

                                loading.style.display = 'none';
                                content.style.display = 'block';
                            })
                            .catch(err => {
                                console.error(err);
                                alert('Lỗi khi tải thông tin đơn hàng');
                                loading.style.display = 'none';
                            });
                    }
                </script>
            </body>

            </html>