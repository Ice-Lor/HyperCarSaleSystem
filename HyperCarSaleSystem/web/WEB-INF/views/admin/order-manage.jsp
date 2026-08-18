<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Hợp Đồng Đặt Cọc - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="orders" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">QUẢN LÝ HỢP ĐỒNG ĐẶT CỌC</h3>
                    <p class="text-secondary small mb-0">Theo dõi tiến trình thanh toán cọc và bàn giao xe cho khách VIP</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/export-orders" class="btn btn-outline-gold btn-sm">
                    <i class="bi bi-file-earmark-spreadsheet me-1"></i> Xuất File CSV
                </a>
            </div>

            <div class="hyper-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Mã Hợp Đồng</th>
                                <th>Khách Hàng</th>
                                <th>Tổng Giá Trị</th>
                                <th>Tiền Đặt Cọc</th>
                                <th>Phương Thức</th>
                                <th>Trạng Thái</th>
                                <th>Cập Nhật</th>
                                <th>Chi Tiết</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${orders}">
                                <tr>
                                    <td class="fw-bold text-warning font-brand">${o.orderCode}</td>
                                    <td>
                                        <div class="text-white fw-bold">${o.customerName}</div>
                                        <div class="small text-secondary">${o.phone}</div>
                                    </td>
                                    <td class="text-white font-brand">
                                        <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td class="text-warning fw-bold font-brand">
                                        <fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td><span class="badge bg-dark border border-secondary text-secondary">${o.paymentMethod}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 'CONFIRMED'}"><span class="badge bg-success">ĐÃ XÁC NHẬN</span></c:when>
                                            <c:when test="${o.status == 'PROCESSING'}"><span class="badge bg-info text-dark">ĐANG XỬ LÝ</span></c:when>
                                            <c:when test="${o.status == 'COMPLETED'}"><span class="badge bg-primary">ĐÃ BÀN GIAO</span></c:when>
                                            <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-danger">ĐÃ HỦY</span></c:when>
                                            <c:otherwise><span class="badge bg-warning text-dark">CHỜ DUYỆT</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="POST" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                                            <input type="hidden" name="orderId" value="${o.orderId}">
                                            <select name="status" class="form-select form-select-dark form-select-sm" style="width: 140px;">
                                                <option value="PENDING" ${o.status == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                                                <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>Xác nhận cọc</option>
                                                <option value="PROCESSING" ${o.status == 'PROCESSING' ? 'selected' : ''}>Đang chuẩn bị xe</option>
                                                <option value="COMPLETED" ${o.status == 'COMPLETED' ? 'selected' : ''}>Đã giao xe</option>
                                                <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>Hủy đơn</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-gold" title="Cập nhật">
                                                <i class="bi bi-check-lg"></i>
                                            </button>
                                        </form>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/orders?id=${o.orderId}" class="btn btn-sm btn-outline-light" title="Xem chi tiết">
                                            <i class="bi bi-eye"></i>
                                        </a>
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
