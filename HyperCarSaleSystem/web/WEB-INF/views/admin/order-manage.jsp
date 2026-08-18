<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Quản Lý Đơn Cọc Xe - Admin HyperCar"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="orders"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">QUẢN LÝ HỢP ĐỒNG ĐẶT CỌC</h3>
                    <p class="small mb-0" style="color: #b0b3c0;">Duyệt, xác nhận thanh toán cọc và quản lý tiến độ bàn giao</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/export-orders" class="btn btn-outline-gold btn-sm">
                    <i class="bi bi-file-earmark-spreadsheet me-1"></i> Xuất Báo Cáo CSV
                </a>
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
                                <th>Mã Hợp Đồng</th>
                                <th>Khách Hàng VIP</th>
                                <th>Tổng Giá Trị</th>
                                <th>Tiền Đặt Cọc</th>
                                <th>Voucher</th>
                                <th>Phương Thức</th>
                                <th>Trạng Thái</th>
                                <th>Cập Nhật Trạng Thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="o">
                                <tr class="border-bottom border-secondary">
                                    <td class="fw-bold text-gold">${o.orderCode}</td>
                                    <td>
                                        <div class="fw-bold text-light">${o.userName}</div>
                                        <small style="color: #b0b3c0;">${o.userEmail}</small>
                                    </td>
                                    <td class="text-light"><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$"/></td>
                                    <td class="text-gold fw-bold"><fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$"/></td>
                                    <td><span class="badge bg-dark border border-secondary">${o.couponCode != null ? o.couponCode : 'Không'}</span></td>
                                    <td><span class="badge bg-dark border border-secondary">${o.paymentMethod}</span></td>
                                    <td>
                                        <span class="badge ${o.status == 'COMPLETED' ? 'bg-success' : (o.status == 'CONFIRMED' ? 'bg-info text-dark' : (o.status == 'CANCELLED' ? 'bg-danger' : 'bg-warning text-dark'))}">
                                            ${o.status}
                                        </span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="POST" class="d-flex gap-1">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                                            <input type="hidden" name="orderId" value="${o.orderId}">
                                            <select name="status" class="form-select form-select-sm bg-dark border-secondary text-light" style="width: 140px;">
                                                <option value="PENDING" ${o.status == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                                                <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                                                <option value="COMPLETED" ${o.status == 'COMPLETED' ? 'selected' : ''}>Đã bàn giao</option>
                                                <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>Hủy đơn</option>
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
