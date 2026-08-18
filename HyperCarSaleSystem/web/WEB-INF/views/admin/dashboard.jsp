<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Bàn Quản Trị Hệ Thống - HyperCar Sale System"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <!-- Admin Sidebar -->
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="dashboard"/>
        </jsp:include>

        <!-- Main Content -->
        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">TỔNG QUAN DOANH THU & KINH DOANH</h3>
                    <p class="small mb-0" style="color: #b0b3c0;">Hệ thống phân phối và quản lý khách hàng siêu xe VIP</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/export-orders" class="btn btn-outline-gold btn-sm">
                    <i class="bi bi-download me-1"></i> Xuất Báo Cáo Excel (CSV)
                </a>
            </div>

            <!-- KPI Metric Cards -->
            <div class="row g-3 mb-4">
                <div class="col-md-3">
                    <div class="card card-luxury p-3 border-gold">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <small class="text-uppercase" style="color: #b0b3c0;">Tiền Cọc Đã Thu</small>
                                <h4 class="fw-bold text-gold mt-1 mb-0">
                                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="$"/>
                                </h4>
                            </div>
                            <i class="bi bi-wallet2 text-gold fs-2"></i>
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card card-luxury p-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <small class="text-uppercase" style="color: #b0b3c0;">Siêu Xe Sẵn Sàng</small>
                                <h4 class="fw-bold text-light mt-1 mb-0">${totalCars} chiếc</h4>
                            </div>
                            <i class="bi bi-car-front-fill text-info fs-2"></i>
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card card-luxury p-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <small class="text-uppercase" style="color: #b0b3c0;">Tổng Hợp Đồng Cọc</small>
                                <h4 class="fw-bold text-light mt-1 mb-0">${totalOrders} đơn</h4>
                            </div>
                            <i class="bi bi-receipt-cutoff text-warning fs-2"></i>
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card card-luxury p-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <small class="text-uppercase" style="color: #b0b3c0;">Khách Hàng VIP</small>
                                <h4 class="fw-bold text-light mt-1 mb-0">${totalUsers} người</h4>
                            </div>
                            <i class="bi bi-person-badge-fill text-success fs-2"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Chart.js Section -->
            <div class="row g-4 mb-4">
                <div class="col-lg-6">
                    <div class="card card-luxury p-4">
                        <h6 class="fw-bold text-gold mb-3"><i class="bi bi-pie-chart-fill me-2"></i> TỶ TRỌNG SIÊU XE THEO HÃNG</h6>
                        <canvas id="brandChart" style="max-height: 260px;"></canvas>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card card-luxury p-4">
                        <h6 class="fw-bold text-gold mb-3"><i class="bi bi-clock-history me-2"></i> NHẬT KÝ HOẠT ĐỘNG HỆ THỐNG GẦN ĐÂY</h6>
                        <div class="d-flex flex-column gap-2">
                            <c:forEach items="${recentLogs}" var="log">
                                <div class="p-2 rounded bg-surface d-flex justify-content-between align-items-center small" style="background-color: #1c1e2e;">
                                    <div>
                                        <span class="badge bg-gold text-dark me-2">${log.action}</span>
                                        <span class="text-light">${log.details}</span>
                                    </div>
                                    <span style="color: #b0b3c0; font-size: 0.75rem;"><fmt:formatDate value="${log.createdAt}" pattern="HH:mm dd/MM"/></span>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Orders Table -->
            <div class="card card-luxury p-4">
                <h6 class="fw-bold text-gold mb-3"><i class="bi bi-table me-2"></i> DANH SÁCH HỢP ĐỒNG ĐẶT CỌC MỚI NHẤT</h6>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                        <thead>
                            <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                                <th>Mã Hợp Đồng</th>
                                <th>Khách Hàng</th>
                                <th>Tổng Giá Trị</th>
                                <th>Tiền Đặt Cọc</th>
                                <th>Phương Thức</th>
                                <th>Trạng Thái</th>
                                <th>Ngày Đặt</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentOrders}" var="o">
                                <tr class="border-bottom border-secondary">
                                    <td class="fw-bold text-gold">${o.orderCode}</td>
                                    <td class="text-light">${o.userName}</td>
                                    <td class="text-light"><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$"/></td>
                                    <td class="text-gold fw-bold"><fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$"/></td>
                                    <td><span class="badge bg-dark border border-secondary">${o.paymentMethod}</span></td>
                                    <td>
                                        <span class="badge ${o.status == 'COMPLETED' ? 'bg-success' : (o.status == 'CONFIRMED' ? 'bg-info text-dark' : 'bg-warning text-dark')}">
                                            ${o.status}
                                        </span>
                                    </td>
                                    <td class="small" style="color: #b0b3c0;"><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Chart.js Library -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const brandData = {
            labels: [
                <c:forEach items="${carsByBrand}" var="entry" varStatus="loop">
                    '${entry.key}'${!loop.last ? ',' : ''}
                </c:forEach>
            ],
            datasets: [{
                data: [
                    <c:forEach items="${carsByBrand}" var="entry" varStatus="loop">
                        ${entry.value}${!loop.last ? ',' : ''}
                    </c:forEach>
                ],
                backgroundColor: ['#d4af37', '#e5c158', '#3b82f6', '#10b981', '#ef4444', '#8b5cf6'],
                borderColor: '#181924',
                borderWidth: 2
            }]
        };

        const ctx = document.getElementById('brandChart').getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: brandData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: { color: '#f3f4f6' }
                    }
                }
            }
        });
    });
</script>

<!-- Bootstrap Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
