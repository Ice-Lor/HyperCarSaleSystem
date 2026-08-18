<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Lịch Lái Thử VIP Track - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="bookings" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">QUẢN LÝ LỊCH LÁI THỬ TRACK VIP</h3>
                    <p class="text-secondary small mb-0">Xác nhận và điều phối xe phục vụ buổi chạy thử của khách VIP</p>
                </div>
            </div>

            <div class="hyper-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Khách Hàng</th>
                                <th>Siêu Xe Trải Nghiệm</th>
                                <th>Ngày & Khung Giờ</th>
                                <th>Địa Điểm Trường Đua</th>
                                <th>Bằng Lái</th>
                                <th>Trạng Thái</th>
                                <th>Cập Nhật</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="b" items="${bookings}">
                                <tr>
                                    <td>
                                        <div class="fw-bold text-white">${b.customerName}</div>
                                        <div class="small text-secondary">${b.customerPhone}</div>
                                    </td>
                                    <td>
                                        <div class="fw-bold text-warning">${b.carModelName}</div>
                                        <div class="small text-secondary">${b.brandName}</div>
                                    </td>
                                    <td>
                                        <div class="text-white font-brand"><fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy" /></div>
                                        <div class="small text-secondary">${b.timeSlot}</div>
                                    </td>
                                    <td class="small text-secondary" style="max-width: 200px;">${b.locationTrack}</td>
                                    <td><span class="badge bg-dark border border-secondary text-white">${b.driverLicenseNumber}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${b.status == 'CONFIRMED'}"><span class="badge bg-success">ĐÃ DUYỆT</span></c:when>
                                            <c:when test="${b.status == 'COMPLETED'}"><span class="badge bg-primary">HOÀN TẤT</span></c:when>
                                            <c:when test="${b.status == 'CANCELLED'}"><span class="badge bg-danger">ĐÃ HỦY</span></c:when>
                                            <c:otherwise><span class="badge bg-warning text-dark">CHỜ DUYỆT</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/bookings" method="POST" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                                            <input type="hidden" name="bookingId" value="${b.bookingId}">
                                            <select name="status" class="form-select form-select-dark form-select-sm" style="width: 130px;">
                                                <option value="PENDING" ${b.status == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                                                <option value="CONFIRMED" ${b.status == 'CONFIRMED' ? 'selected' : ''}>Xác nhận</option>
                                                <option value="COMPLETED" ${b.status == 'COMPLETED' ? 'selected' : ''}>Hoàn tất</option>
                                                <option value="CANCELLED" ${b.status == 'CANCELLED' ? 'selected' : ''}>Từ chối/Hủy</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-gold">
                                                <i class="bi bi-check-lg"></i>
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
