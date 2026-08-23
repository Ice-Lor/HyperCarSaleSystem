<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Lịch Sử Hợp Đồng Đặt Cọc" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">LỊCH SỬ <span class="text-gold">HỢP ĐỒNG ĐẶT CỌC</span></h1>
        <p class="page-subtitle">Theo dõi tiến độ xử lý và chi tiết các hợp đồng đặt cọc siêu xe độc bản của quý khách</p>
    </div>
</div>

<div class="container section">
    <c:choose>
        <c:when test="${not empty orders}">
            <div class="card p-4">
                <table class="table order-table">
                    <thead>
                        <tr>
                            <th>Mã Hợp Đồng</th>
                            <th>Ngày Ký Cọc</th>
                            <th>Tổng Giá Trị</th>
                            <th>Tiền Cọc Đã Ký</th>
                            <th>Hình Thức</th>
                            <th>Trạng Thái</th>
                            <th>Chi Tiết</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ord" items="${orders}">
                            <tr>
                                <td class="font-bold text-gold">
                                    <a href="${pageContext.request.contextPath}/order-detail?id=${ord.orderId}">
                                        ${ord.orderCode}
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatDate value="${ord.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td class="font-bold">
                                    <fmt:formatNumber value="${ord.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </td>
                                <td class="text-gold font-bold">
                                    <fmt:formatNumber value="${ord.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </td>
                                <td>
                                    <span class="badge badge-outline">${ord.paymentMethod}</span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ord.status == 'PENDING'}">
                                            <span class="badge badge-warning">⏳ Chờ Xác Nhận Cọc</span>
                                        </c:when>
                                        <c:when test="${ord.status == 'CONFIRMED'}">
                                            <span class="badge badge-info">✓ Đã Xác Nhận Tiền Cọc</span>
                                        </c:when>
                                        <c:when test="${ord.status == 'PROCESSING'}">
                                            <span class="badge badge-primary">⚙️ Đang Chuẩn Bị Xe</span>
                                        </c:when>
                                        <c:when test="${ord.status == 'COMPLETED'}">
                                            <span class="badge badge-success">🏆 Đã Bàn Giao Xe</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">❌ Đã Hủy</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order-detail?id=${ord.orderId}" 
                                       class="btn btn-outline btn-sm">
                                        Xem Hóa Đơn
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state p-5">
                <div class="empty-icon">📜</div>
                <h2>Chưa Có Hợp Đồng Đặt Cọc</h2>
                <p>Quý khách chưa thực hiện hợp đồng đặt cọc siêu xe nào tại hệ thống Showroom.</p>
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-lg mt-3">
                    KHÁM PHÁ BỘ SƯU TẬP SIÊU XE
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
