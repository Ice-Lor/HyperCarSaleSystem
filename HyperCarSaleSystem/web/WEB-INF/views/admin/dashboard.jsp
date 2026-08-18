<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Tổng Quan Quản Trị - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="dashboard" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Dashboard Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">BẢNG ĐIỀU KHIỂN QUẢN TRỊ (DASHBOARD)</h3>
                    <p class="text-secondary small mb-0">Hệ thống phân tích và giám sát vận hành Showroom Siêu Xe</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/export-orders" class="btn btn-outline-gold btn-sm">
                        <i class="bi bi-file-earmark-spreadsheet me-1"></i> Xuất Báo Cáo CSV
                    </a>
                </div>
            </div>

            <!-- Stats Metric Cards -->
            <div class="row g-3 mb-4">
                <div class="col-xl-3 col-sm-6">
                    <div class="hyper-card p-3 border-start border-warning border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-secondary small fw-bold text-uppercase d-block">Tổng Doanh Thu Cọc</span>
                                <span class="fs-4 fw-bold text-warning font-brand">
                                    <fmt:formatNumber value="${stats.totalRevenue}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                </span>
                            </div>
                            <div class="fs-2 text-warning"><i class="bi bi-currency-dollar"></i></div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="hyper-card p-3 border-start border-info border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-secondary small fw-bold text-uppercase d-block">Hợp Đồng Đặt Cọc</span>
                                <span class="fs-4 fw-bold text-white font-brand">${stats.totalOrders}</span>
                            </div>
                            <div class="fs-2 text-info"><i class="bi bi-receipt"></i></div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="hyper-card p-3 border-start border-danger border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-secondary small fw-bold text-uppercase d-block">Lịch Lái Thử Chờ Duyệt</span>
                                <span class="fs-4 fw-bold text-danger font-brand">${stats.pendingBookings}</span>
                            </div>
                            <div class="fs-2 text-danger"><i class="bi bi-flag"></i></div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="hyper-card p-3 border-start border-success border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-secondary small fw-bold text-uppercase d-block">Khách Hàng VIP</span>
                                <span class="fs-4 fw-bold text-success font-brand">${stats.totalCustomers}</span>
                            </div>
                            <div class="fs-2 text-success"><i class="bi bi-people"></i></div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Charts & Analytics (Chart.js) -->
            <div class="row g-4 mb-4">
                <div class="col-lg-8">
                    <div class="hyper-card p-4">
                        <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                            <i class="bi bi-graph-up-arrow me-2"></i> BIỂU ĐỒ DOANH THU ĐẶT CỌC THEO THÁNG
                        </h5>
                        <canvas id="revenueChart" style="max-height: 280px;"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="hyper-card p-4">
                        <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                            <i class="bi bi-pie-chart-fill me-2"></i> TỶ TRỌNG SIÊU XE THEO HÃNG
                        </h5>
                        <canvas id="brandChart" style="max-height: 280px;"></canvas>
                    </div>
                </div>
            </div>

            <!-- Recent Orders & Audit Logs -->
            <div class="row g-4">
                <!-- Recent Orders -->
                <div class="col-lg-7">
                    <div class="hyper-card p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold gold-text font-brand mb-0">HỢP ĐỒNG CỌC MỚI NHẤT</h5>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="small text-secondary text-decoration-none">Xem tất cả</a>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-dark table-sm table-dark-custom align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th>Mã Hợp Đồng</th>
                                        <th>Khách Hàng</th>
                                        <th>Tiền Cọc</th>
                                        <th>Trạng Thái</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="o" items="${recentOrders}">
                                        <tr>
                                            <td class="fw-bold text-warning small font-brand">${o.orderCode}</td>
                                            <td class="small text-white">${o.customerName}</td>
                                            <td class="small text-white font-brand">
                                                <fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                            </td>
                                            <td>
                                                <span class="badge ${o.status == 'CONFIRMED' ? 'bg-success' : 'bg-warning text-dark'} small">
                                                    ${o.status}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- System Audit Logs -->
                <div class="col-lg-5">
                    <div class="hyper-card p-4">
                        <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                            <i class="bi bi-activity me-2"></i> NHẬT KÝ HOẠT ĐỘNG (AUDIT LOG)
                        </h5>
                        <ul class="list-unstyled small mb-0 lh-lg" style="max-height: 250px; overflow-y: auto;">
                            <c:forEach var="log" items="${recentLogs}">
                                <li class="pb-2 mb-2 border-bottom border-secondary border-opacity-10">
                                    <div class="d-flex justify-content-between text-secondary">
                                        <span class="badge bg-dark border border-secondary text-warning">${log.action}</span>
                                        <span><fmt:formatDate value="${log.createdAt}" pattern="HH:mm dd/MM" /></span>
                                    </div>
                                    <div class="text-white mt-1">${log.details}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Chart.js CDN (Phía Client, không dùng lib Java ngoài) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', () => {
    // 1. Revenue Bar Chart
    const ctxRevenue = document.getElementById('revenueChart');
    if (ctxRevenue) {
        new Chart(ctxRevenue, {
            type: 'bar',
            data: {
                labels: ['Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8'],
                datasets: [{
                    label: 'Tiền Cọc (Triệu USD)',
                    data: [1.2, 1.8, 2.5, 3.1, 2.9, 4.4],
                    backgroundColor: 'rgba(212, 175, 55, 0.7)',
                    borderColor: '#d4af37',
                    borderWidth: 1,
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { labels: { color: '#ccc' } }
                },
                scales: {
                    x: { ticks: { color: '#888' }, grid: { color: 'rgba(255,255,255,0.05)' } },
                    y: { ticks: { color: '#888' }, grid: { color: 'rgba(255,255,255,0.05)' } }
                }
            }
        });
    }

    // 2. Brand Doughnut Chart
    const ctxBrand = document.getElementById('brandChart');
    if (ctxBrand) {
        new Chart(ctxBrand, {
            type: 'doughnut',
            data: {
                labels: ['Bugatti', 'Ferrari', 'Lamborghini', 'Koenigsegg', 'Khác'],
                datasets: [{
                    data: [35, 25, 20, 15, 5],
                    backgroundColor: ['#d4af37', '#e63946', '#2a9d8f', '#e76f51', '#4a4e69'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom', labels: { color: '#ccc', boxWidth: 12 } }
                }
            }
        });
    }
});
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
