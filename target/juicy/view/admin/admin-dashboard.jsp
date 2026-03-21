<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Admin Dashboard | Juicy</title>

                <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            </head>

            <body>

                <!-- HEADER -->
                <header class="sticky-top shadow-sm">
                    <nav class="navbar navbar-expand-lg navbar-light bg-white py-3">
                        <div class="container">
                            <a class="navbar-brand fw-bold text-success fs-3"
                                href="${pageContext.request.contextPath}/admin/dashboard">
                                <img src="${pageContext.request.contextPath}/images/logo/logo-juicy.png" height="40"
                                    class="me-2">
                                JUICY
                            </a>

                            <div class="collapse navbar-collapse show">
                                <ul class="navbar-nav ms-auto">
                                    <li class="nav-item">
                                        <a class="nav-link active fw-semibold"
                                            href="${pageContext.request.contextPath}/admin/dashboard">
                                            Dashboard
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link fw-semibold"
                                            href="${pageContext.request.contextPath}/admin/products">
                                            Sản phẩm
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link fw-semibold"
                                            href="${pageContext.request.contextPath}/admin/manage-orders">
                                            Đơn hàng
                                        </a>
                                    </li>
                                </ul>

                                <a href="${pageContext.request.contextPath}/logout"
                                    class="btn btn-outline-success rounded-pill ms-3">
                                    Đăng xuất
                                </a>
                            </div>
                        </div>
                    </nav>
                </header>

                <!-- TỔNG QUAN -->
                <div class="container my-5">
                    <div class="row g-4 mb-4">
                        <div class="col-md-6">
                            <div class="p-4 shadow rounded bg-white text-center border-start border-5 border-success">
                                <h6 class="text-uppercase text-muted">Tổng doanh thu toàn thời gian</h6>
                                <h2 class="text-success fw-bold display-6">
                                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" />
                                </h2>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="p-4 shadow rounded bg-white text-center border-start border-5 border-primary">
                                <h6 class="text-uppercase text-muted">Tổng số đơn hàng</h6>
                                <h2 class="text-primary fw-bold display-6">
                                    ${totalOrders}
                                </h2>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4">

                        <div class="col-md-4">
                            <div class="p-4 shadow rounded bg-white text-center">
                                <h6>Đơn hôm nay</h6>
                                <h2 class="text-success fw-bold">
                                    ${todayOrders}
                                </h2>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="p-4 shadow rounded bg-white text-center">
                                <h6>Đơn tuần này</h6>
                                <h2 class="text-primary fw-bold">
                                    ${weekOrders}
                                </h2>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="p-4 shadow rounded bg-white text-center">
                                <h6>Đơn tháng này</h6>
                                <h2 class="text-warning fw-bold">
                                    ${monthOrders}
                                </h2>
                            </div>
                        </div>

                    </div>

                    <!-- BIỂU ĐỒ + TOP SP -->
                    <div class="row mt-5">
                        <div class="col-md-8">
                            <div class="shadow p-4 bg-white rounded">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5 class="mb-0">Biểu đồ doanh thu</h5>
                                    <div class="d-flex gap-2">
                                        <select id="chartFilter" class="form-select form-select-sm"
                                            style="width: auto;">
                                            <option value="1day">Hôm nay</option>
                                            <option value="7days" selected>7 ngày qua</option>
                                            <option value="30days">30 ngày qua</option>
                                            <option value="year">Năm nay</option>
                                            <option value="custom">Tùy chọn...</option>
                                        </select>
                                        <div id="customDateRange" class="d-flex gap-2"
                                            style="display: none !important;">
                                            <input type="date" id="startDate" class="form-control form-control-sm">
                                            <input type="date" id="endDate" class="form-control form-control-sm">
                                            <button id="btnFilter" class="btn btn-sm btn-primary">Lọc</button>
                                        </div>
                                    </div>
                                </div>
                                <div style="height: 350px;">
                                    <canvas id="revenueChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="shadow p-4 bg-white rounded">
                                <h5 class="mb-3">Sản phẩm bán chạy</h5>

                                <ul class="list-group">
                                    <c:forEach items="${topProducts}" var="p">
                                        <li class="list-group-item d-flex justify-content-between">
                                            <span>${p.name}</span>
                                            <span class="fw-bold text-success">
                                                ${p.sold} đã bán
                                            </span>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- FOOTER -->
                <footer class="bg-dark text-white text-center py-3">
                    © 2024 Juicy. All Rights Reserved.
                </footer>

                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <script>
                    const contextPath = '${pageContext.request.contextPath}';
                    // Dữ liệu từ Servlet được inject vào đây (fallback initial data)
                    window.revenueLabels = ${ revenueLabels };
                    window.revenueData = ${ revenueData };
                </script>
                <script src="${pageContext.request.contextPath}/js/admin-chart.js"></script>
            </body>

            </html>