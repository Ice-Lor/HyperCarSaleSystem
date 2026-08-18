<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Chi Tiết Hợp Đồng #${order.orderCode} - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="font-brand fw-bold text-white mb-1">HỢP ĐỒNG #${order.orderCode}</h2>
            <p class="text-secondary small mb-0">Ngày tạo: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
        </div>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách
        </a>
    </div>

    <div class="row g-4">
        <!-- Order Items -->
        <div class="col-lg-8">
            <div class="hyper-card p-4 mb-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    DANH SÁCH SIÊU XE ĐẶT CỌC
                </h5>

                <div class="table-responsive">
                    <table class="table table-dark table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Siêu Xe</th>
                                <th>Màu Sơn</th>
                                <th>Số Lượng</th>
                                <th>Đơn Giá</th>
                                <th>Thành Tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="detail" items="${order.orderDetails}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${detail.thumbnailUrl}" alt="${detail.modelName}" class="rounded me-3" style="width: 70px; height: 45px; object-fit: cover;">
                                            <div>
                                                <div class="fw-bold text-white">${detail.modelName}</div>
                                                <div class="small text-secondary">${detail.brandName}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="badge bg-secondary">${not empty detail.selectedColor ? detail.selectedColor : 'Mặc định'}</span></td>
                                    <td class="text-white">${detail.quantity}</td>
                                    <td class="text-white font-brand">
                                        <fmt:formatNumber value="${detail.unitPrice}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td class="text-warning fw-bold font-brand">
                                        <fmt:formatNumber value="${detail.subTotal}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Delivery & Contract Info -->
            <div class="hyper-card p-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    ĐỊA CHỈ & THỦ TỤC BÀN GIAO
                </h5>
                <div class="row g-3 small text-secondary">
                    <div class="col-md-6">
                        <span class="d-block text-white fw-bold">Người nhận xe:</span>
                        ${order.customerName} (${order.phone})
                    </div>
                    <div class="col-md-6">
                        <span class="d-block text-white fw-bold">Địa chỉ giao:</span>
                        ${order.deliveryAddress}
                    </div>
                    <div class="col-12">
                        <span class="d-block text-white fw-bold">Ghi chú kèm theo:</span>
                        ${not empty order.note ? order.note : 'Không có yêu cầu đặc biệt.'}
                    </div>
                </div>
            </div>
        </div>

        <!-- Summary & Status -->
        <div class="col-lg-4">
            <div class="hyper-card p-4 sticky-top" style="top: 90px;">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    TÌNH TRẠNG HỢP ĐỒNG
                </h5>

                <div class="mb-3">
                    <span class="text-secondary small d-block">Trạng thái hiện tại:</span>
                    <c:choose>
                        <c:when test="${order.status == 'CONFIRMED'}"><span class="badge bg-success fs-6">ĐÃ XÁC NHẬN CỌC</span></c:when>
                        <c:when test="${order.status == 'PROCESSING'}"><span class="badge bg-info text-dark fs-6">ĐANG VẬN CHUYỂN</span></c:when>
                        <c:when test="${order.status == 'COMPLETED'}"><span class="badge bg-primary fs-6">ĐÃ BÀN GIAO XE</span></c:when>
                        <c:when test="${order.status == 'CANCELLED'}"><span class="badge bg-danger fs-6">ĐÃ HỦY HỢP ĐỒNG</span></c:when>
                        <c:otherwise><span class="badge bg-warning text-dark fs-6">CHỜ DUYỆT THANH TOÁN</span></c:otherwise>
                    </c:choose>
                </div>

                <div class="mb-3">
                    <span class="text-secondary small d-block">Phương thức thanh toán:</span>
                    <strong class="text-white">${order.paymentMethod}</strong>
                </div>

                <hr class="border-secondary border-opacity-25 my-3">

                <div class="d-flex justify-content-between text-secondary small mb-2">
                    <span>Tổng giá trị hợp đồng:</span>
                    <span class="text-white fw-bold">
                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                    </span>
                </div>

                <c:if test="${order.discountAmount > 0}">
                    <div class="d-flex justify-content-between text-success small mb-2">
                        <span>Chiết khấu VIP (${order.couponCode}):</span>
                        <span>-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" /></span>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between align-items-center pt-2 border-top border-secondary border-opacity-25">
                    <span class="text-white fw-bold">TIỀN CỌC:</span>
                    <span class="text-warning fw-bold fs-4 font-brand">
                        <fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
