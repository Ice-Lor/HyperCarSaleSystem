<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Quản Lý Hợp Đồng Đặt Cọc" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="orders" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">QUẢN LÝ HỢP ĐỒNG ĐẶT CỌC SIÊU XE</h1>
            <div class="admin-topbar-actions">
                <a href="${pageContext.request.contextPath}/admin/export-report" class="btn btn-gold btn-sm" target="_blank">
                    📥 Xuất Báo Cáo Excel
                </a>
            </div>
        </div>

        <c:if test="${param.msg == 'status_updated'}">
            <div class="alert alert-success">✓ Cập nhật trạng thái hợp đồng đặt cọc thành công!</div>
        </c:if>

        <div class="card p-4 mt-3">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>Mã Hợp Đồng</th>
                        <th>Khách Hàng</th>
                        <th>Số Điện Thoại</th>
                        <th>Tiền Cọc (10%)</th>
                        <th>Tổng Giá Trị</th>
                        <th>Ngày Ký</th>
                        <th>Cập Nhật Trạng Thái</th>
                        <th>Chi Tiết</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="ord" items="${orders}">
                        <tr>
                            <td class="font-bold text-gold">${ord.orderCode}</td>
                            <td>
                                <div>${ord.userFullName}</div>
                                <span class="font-sm text-muted">@${ord.username}</span>
                            </td>
                            <td>${ord.phone}</td>
                            <td class="text-gold font-bold">
                                <fmt:formatNumber value="${ord.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${ord.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                            <td class="font-sm text-muted">
                                <fmt:formatDate value="${ord.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/orders" method="POST" class="d-flex align-items-center">
                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                    <input type="hidden" name="orderId" value="${ord.orderId}" />
                                    
                                    <select name="status" class="form-control form-control-sm mr-2" onchange="this.form.submit()">
                                        <option value="PENDING" ${ord.status == 'PENDING' ? 'selected' : ''}>⏳ Chờ Xác Nhận</option>
                                        <option value="CONFIRMED" ${ord.status == 'CONFIRMED' ? 'selected' : ''}>✓ Đã Nhận Cọc</option>
                                        <option value="PROCESSING" ${ord.status == 'PROCESSING' ? 'selected' : ''}>⚙️ Đang Chuẩn Bị Xe</option>
                                        <option value="COMPLETED" ${ord.status == 'COMPLETED' ? 'selected' : ''}>🏆 Đã Bàn Giao</option>
                                        <option value="CANCELLED" ${ord.status == 'CANCELLED' ? 'selected' : ''}>❌ Hủy Hợp Đồng</option>
                                    </select>
                                </form>
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
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/main.js" charset="UTF-8"></script>
</body>
</html>
