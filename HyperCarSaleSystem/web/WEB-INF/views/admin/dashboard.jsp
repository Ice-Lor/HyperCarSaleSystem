<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Tổng Quan Hệ Thống (Dashboard)" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="dashboard" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">BÀN QUẢN TRỊ TRUNG TÂM</h1>
            <div class="admin-topbar-actions">
                <a href="${pageContext.request.contextPath}/admin/export-report" class="btn btn-gold btn-sm" target="_blank">
                    📥 Xuất Báo Cáo Doanh Thu
                </a>
            </div>
        </div>

        <!-- 1. THẺ THỐNG KÊ KPI CARDS -->
        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-icon">💰</div>
                <div class="kpi-data">
                    <div class="kpi-num text-gold">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                    </div>
                    <div class="kpi-label">Tổng Doanh Thu Tiền Cọc</div>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">📜</div>
                <div class="kpi-data">
                    <div class="kpi-num">${totalOrders}</div>
                    <div class="kpi-label">Hợp Đồng Đã Ký</div>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">🏎️</div>
                <div class="kpi-data">
                    <div class="kpi-num">${totalCars}</div>
                    <div class="kpi-label">Siêu Xe Đang Mở Bán</div>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">🏁</div>
                <div class="kpi-data">
                    <div class="kpi-num text-warning">${pendingBookings}</div>
                    <div class="kpi-label">Lịch Lái Thử Chờ Duyệt</div>
                </div>
            </div>
        </div>

        <!-- 2. BẢNG HỢP ĐỒNG ĐẶT CỌC GẦN ĐÂY NHẤT -->
        <div class="admin-section card p-4 mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="card-title mb-0">HỢP ĐỒNG ĐẶT CỌC MỚI NHẤT</h3>
                <a href="${pageContext.request.contextPath}/admin/orders" class="font-sm text-gold">Xem Tất Cả →</a>
            </div>

            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>Mã Hợp Đồng</th>
                        <th>Khách Hàng VIP</th>
                        <th>Số Điện Thoại</th>
                        <th>Tiền Cọc (10%)</th>
                        <th>Phương Thức</th>
                        <th>Trạng Thái</th>
                        <th>Chi Tiết</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="ord" items="${recentOrders}">
                        <tr>
                            <td class="font-bold text-gold">${ord.orderCode}</td>
                            <td>${ord.userFullName}</td>
                            <td>${ord.phone}</td>
                            <td class="text-gold font-bold">
                                <fmt:formatNumber value="${ord.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                            <td><span class="badge badge-outline">${ord.paymentMethod}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${ord.status == 'PENDING'}"><span class="badge badge-warning">Chờ Duyệt</span></c:when>
                                    <c:when test="${ord.status == 'CONFIRMED'}"><span class="badge badge-info">Đã Nhận Cọc</span></c:when>
                                    <c:when test="${ord.status == 'PROCESSING'}"><span class="badge badge-primary">Chuẩn Bị Xe</span></c:when>
                                    <c:when test="${ord.status == 'COMPLETED'}"><span class="badge badge-success">Đã Giao Xe</span></c:when>
                                    <c:otherwise><span class="badge badge-danger">Đã Hủy</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/order-detail?id=${ord.orderId}" 
                                   class="btn btn-outline btn-sm" target="_blank">
                                    Hóa Đơn
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 3. NHẬT KÝ HOẠT ĐỘNG HỆ THỐNG (AUDIT TRAIL) -->
        <div class="admin-section card p-4 mt-4">
            <h3 class="card-title">NHẬT KÝ HOẠT ĐỘNG HỆ THỐNG GẦN ĐÂY</h3>
            <div class="audit-log-list">
                <c:forEach var="log" items="${recentLogs}">
                    <div class="audit-log-item">
                        <span class="log-time text-muted font-sm">
                            <fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                        <span class="badge badge-primary">${log.action}</span>
                        <span class="log-desc">${log.details}</span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
