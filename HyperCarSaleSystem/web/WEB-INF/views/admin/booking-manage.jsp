<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Quản Lý Lịch Lái Thử - Admin HyperCar"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="bookings"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">QUẢN LÝ LỊCH TRẢI NGHIỆM LÁI THỬ F1</h3>
            </div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success py-2 small mb-4">${sessionScope.successMessage}</div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <div class="card card-luxury p-3">
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                        <thead>
                            <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                                <th>ID</th>
                                <th>Khách Hàng VIP</th>
                                <th>Siêu Xe</th>
                                <th>Ngày & Khung Giờ</th>
                                <th>Trường Đua</th>
                                <th>Bằng Lái</th>
                                <th>Trạng Thái</th>
                                <th>Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${bookings}" var="b">
                                <tr class="border-bottom border-secondary">
                                    <td class="text-light">#${b.bookingId}</td>
                                    <td>
                                        <div class="fw-bold text-light">${b.userName}</div>
                                        <small style="color: #b0b3c0;">${b.userPhone}</small>
                                    </td>
                                    <td class="text-gold fw-bold">${b.carModelName}</td>
                                    <td>
                                        <div class="text-light"><fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy"/></div>
                                        <small style="color: #b0b3c0;">${b.timeSlot}</small>
                                    </td>
                                    <td><small class="text-light">${b.locationTrack}</small></td>
                                    <td><span class="badge bg-dark border border-secondary">${b.driverLicenseNumber}</span></td>
                                    <td>
                                        <span class="badge ${b.status == 'CONFIRMED' ? 'bg-success' : (b.status == 'CANCELLED' ? 'bg-danger' : 'bg-warning text-dark')}">
                                            ${b.status}
                                        </span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/bookings" method="POST" class="d-flex gap-1">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                                            <input type="hidden" name="bookingId" value="${b.bookingId}">
                                            <select name="status" class="form-select form-select-sm bg-dark border-secondary text-light" style="width: 130px;">
                                                <option value="PENDING" ${b.status == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                                                <option value="CONFIRMED" ${b.status == 'CONFIRMED' ? 'selected' : ''}>Xác nhận</option>
                                                <option value="CANCELLED" ${b.status == 'CANCELLED' ? 'selected' : ''}>Hủy lịch</option>
                                            </select>
                                            <button type="submit" class="btn btn-gold btn-sm px-2">
                                                <i class="bi bi-check2"></i>
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
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
