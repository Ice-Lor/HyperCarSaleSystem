<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Đặt Cọc Thành Công - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5 text-center">
    <div class="hyper-card p-5 mx-auto" style="max-width: 650px;">
        <div class="rounded-circle bg-warning text-dark d-inline-flex align-items-center justify-content-center mb-3" style="width: 80px; height: 80px;">
            <i class="bi bi-check-lg fs-1"></i>
        </div>
        <h2 class="font-brand fw-bold text-white mb-2">ĐẶT CỌC THÀNH CÔNG!</h2>
        <p class="text-secondary mb-4">
            Hợp đồng đặt cọc của đại ca đã được ghi nhận vào hệ thống. Chuyên viên tư vấn cấp cao sẽ liên hệ trong ít phút để hoàn tất thủ tục bàn giao.
        </p>

        <c:if test="${not empty sessionScope.lastOrder}">
            <div class="p-3 rounded mb-4 text-start" style="background: #181822; border: 1px dashed var(--gold-primary);">
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-secondary small">Mã Hợp Đồng:</span>
                    <strong class="text-warning font-brand">${sessionScope.lastOrder.orderCode}</strong>
                </div>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-secondary small">Số Tiền Đặt Cọc:</span>
                    <strong class="text-white font-brand">
                        <fmt:formatNumber value="${sessionScope.lastOrder.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                    </strong>
                </div>
                <div class="d-flex justify-content-between">
                    <span class="text-secondary small">Phương Thức Thanh Toán:</span>
                    <strong class="text-white">${sessionScope.lastOrder.paymentMethod}</strong>
                </div>
            </div>
            <c:remove var="lastOrder" scope="session" />
        </c:if>

        <div class="d-flex justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-gold btn-sm px-4">
                <i class="bi bi-receipt me-1"></i> Quản Lý Hợp Đồng
            </a>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light btn-sm px-4">
                <i class="bi bi-house-door me-1"></i> Quay Lại Trang Chủ
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
