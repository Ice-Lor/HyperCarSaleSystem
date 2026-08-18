<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Lịch Sử Hợp Đồng Đặt Cọc - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <h2 class="font-brand fw-bold text-white mb-4">
        <i class="bi bi-receipt gold-text me-2"></i> HỢP ĐỒNG ĐẶT CỌC CỦA ĐẠI CA
    </h2>

    <c:choose>
        <c:when test="${empty myOrders}">
            <div class="hyper-card p-5 text-center my-5">
                <i class="bi bi-folder-x fs-1 text-secondary mb-3 d-block"></i>
                <h4 class="text-white">Đại ca chưa có hợp đồng nào</h4>
                <p class="text-secondary small">Hãy chọn cho mình mẫu siêu xe yêu thích và đặt cọc ngay hôm nay.</p>
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-sm mt-2">Xem Bộ Sưu Tập Xe</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="hyper-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Mã Hợp Đồng</th>
                                <th>Ngày Đặt</th>
                                <th>Tổng Giá Trị</th>
                                <th>Tiền Đặt Cọc</th>
                                <th>Phương Thức</th>
                                <th>Trạng Thái</th>
                                <th>Chi Tiết</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${myOrders}">
                                <tr>
                                    <td class="fw-bold text-warning font-brand">${o.orderCode}</td>
                                    <td class="text-secondary small">
                                        <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td class="text-white font-brand">
                                        <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td class="text-warning fw-bold font-brand">
                                        <fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td>
                                        <span class="badge bg-dark border border-secondary text-secondary">${o.paymentMethod}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 'CONFIRMED'}"><span class="badge bg-success">ĐÃ XÁC NHẬN</span></c:when>
                                            <c:when test="${o.status == 'PROCESSING'}"><span class="badge bg-info text-dark">ĐANG XỬ LÝ</span></c:when>
                                            <c:when test="${o.status == 'COMPLETED'}"><span class="badge bg-primary">ĐÃ BÀN GIAO</span></c:when>
                                            <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-danger">ĐÃ HỦY</span></c:when>
                                            <c:otherwise><span class="badge bg-warning text-dark">CHỜ DUYỆT CỌC</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/orders?id=${o.orderId}" class="btn btn-outline-gold btn-sm">
                                            <i class="bi bi-eye"></i> Xem
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
