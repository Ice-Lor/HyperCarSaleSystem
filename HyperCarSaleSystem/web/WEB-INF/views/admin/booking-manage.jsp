<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Quản Lý Lịch Lái Thử F1" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="bookings" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">QUẢN LÝ LỊCH LÁI THỬ TRƯỜNG ĐUA F1</h1>
        </div>

        <c:if test="${param.msg == 'status_updated'}">
            <div class="alert alert-success">✓ Cập nhật trạng thái lịch hẹn lái thử thành công!</div>
        </c:if>

        <div class="card p-4 mt-3">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khách Hàng</th>
                        <th>Siêu Xe Trải Nghiệm</th>
                        <th>Ngày Hẹn</th>
                        <th>Khung Giờ</th>
                        <th>Trường Đua</th>
                        <th>Bằng Lái</th>
                        <th>Trạng Thái Duyệt</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="b" items="${bookings}">
                        <tr>
                            <td>${b.bookingId}</td>
                            <td>
                                <div class="font-bold">${b.userFullName}</div>
                                <span class="font-sm text-muted">@${b.username}</span>
                            </td>
                            <td class="text-gold font-bold">${b.carModelName}</td>
                            <td>
                                <fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td>${b.timeSlot}</td>
                            <td class="font-sm">${b.locationTrack}</td>
                            <td><code>${b.driverLicenseNumber}</code></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/bookings" method="POST" class="d-flex align-items-center">
                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                    <input type="hidden" name="action" value="update_status" />
                                    <input type="hidden" name="bookingId" value="${b.bookingId}" />

                                    <select name="status" class="form-control form-control-sm mr-2" onchange="this.form.submit()">
                                        <option value="PENDING" ${b.status == 'PENDING' ? 'selected' : ''}>⏳ Chờ Duyệt</option>
                                        <option value="CONFIRMED" ${b.status == 'CONFIRMED' ? 'selected' : ''}>✓ Đã Xác Nhận</option>
                                        <option value="COMPLETED" ${b.status == 'COMPLETED' ? 'selected' : ''}>🏆 Đã Hoàn Thành</option>
                                        <option value="CANCELLED" ${b.status == 'CANCELLED' ? 'selected' : ''}>❌ Hủy Lịch</option>
                                    </select>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
