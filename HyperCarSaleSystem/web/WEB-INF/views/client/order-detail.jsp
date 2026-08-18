<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Chi Tiết Hợp Đồng #${order.orderCode} - HyperCar"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">CHI TIẾT HỢP ĐỒNG ĐẶT CỌC</h2>
            <p class="small mb-0" style="color: #b0b3c0;">Mã hợp đồng: <span class="text-gold fw-bold">${order.orderCode}</span></p>
        </div>
        <a href="${pageContext.request.contextPath}/order-history" class="btn btn-outline-secondary text-light btn-sm">
            <i class="bi bi-arrow-left me-1"></i> Quay Lại Danh Sách
        </a>
    </div>

    <div class="row g-4">
        <!-- Order Items -->
        <div class="col-lg-8">
            <div class="card card-luxury p-4 mb-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    DANH SÁCH SIÊU XE TRONG HỢP ĐỒNG
                </h5>

                <div class="d-flex flex-column gap-3">
                    <c:forEach items="${order.details}" var="d">
                        <div class="d-flex align-items-center justify-content-between p-3 rounded bg-surface" style="background-color: #1a1c2b;">
                            <div class="d-flex align-items-center">
                                <img src="${pageContext.request.contextPath}/${d.carThumbnailUrl}" alt="${d.carModelName}" style="width: 100px; height: 65px; object-fit: cover;" class="rounded me-3">
                                <div>
                                    <h6 class="fw-bold text-light mb-1">${d.carModelName}</h6>
                                    <div class="small" style="color: #b0b3c0;">Màu sơn: <span class="text-gold">${d.selectedColor}</span> • Số lượng: ${d.quantity} chiếc</div>
                                    <c:if test="${not empty d.customOptions}">
                                        <div class="small" style="font-size: 0.75rem; color: #b0b3c0;">Tùy biến Bespoke: ${d.customOptions}</div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="text-end">
                                <div class="fw-bold text-gold"><fmt:formatNumber value="${d.unitPrice}" type="currency" currencySymbol="$"/></div>
                                <small style="color: #b0b3c0;">Đơn giá niêm yết</small>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Order Information Summary -->
        <div class="col-lg-4">
            <div class="card card-luxury p-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    THÔNG TIN GIAO DỊCH
                </h5>

                <div class="small mb-3">
                    <div style="color: #b0b3c0;">Chủ sở hữu:</div>
                    <div class="fw-bold text-light">${order.userName} (${order.userEmail})</div>
                </div>

                <div class="small mb-3">
                    <div style="color: #b0b3c0;">Số điện thoại liên hệ:</div>
                    <div class="fw-bold text-light">${order.phone}</div>
                </div>

                <div class="small mb-3">
                    <div style="color: #b0b3c0;">Địa chỉ bàn giao siêu xe:</div>
                    <div class="text-light">${order.deliveryAddress}</div>
                </div>

                <div class="small mb-3">
                    <div style="color: #b0b3c0;">Phương thức thanh toán:</div>
                    <div class="badge bg-surface border border-secondary text-light">${order.paymentMethod}</div>
                </div>

                <hr class="border-secondary">

                <div class="d-flex justify-content-between small mb-2" style="color: #b0b3c0;">
                    <span>Tổng giá trị xe:</span>
                    <span class="text-light fw-bold"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/></span>
                </div>

                <c:if test="${order.discountAmount > 0}">
                    <div class="d-flex justify-content-between text-success small mb-2">
                        <span>Ưu đãi VIP (${order.couponCode}):</span>
                        <span>- <fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="$"/></span>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="fw-bold text-light">Tiền Đặt Cọc:</span>
                    <span class="fs-4 fw-bold text-gold"><fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="$"/></span>
                </div>

                <div class="d-flex justify-content-between align-items-center pt-2 border-top border-secondary">
                    <span class="small" style="color: #b0b3c0;">Trạng thái:</span>
                    <span class="badge ${order.status == 'COMPLETED' ? 'bg-success' : (order.status == 'CONFIRMED' ? 'bg-info text-dark' : 'bg-warning text-dark')}">
                        ${order.status}
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
