<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Hợp Đồng Đặt Cọc Của Tôi - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="mb-4">
        <h2 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">HỢP ĐỒNG ĐẶT CỌC SIÊU XE</h2>
        <p class="small" style="color: #b0b3c0;">Quản lý và tra cứu tiến độ bàn giao các siêu phẩm quý khách đang sở hữu</p>
    </div>

    <div class="card card-luxury p-3">
        <div class="table-responsive">
            <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                <thead>
                    <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                        <th>Mã Hợp Đồng</th>
                        <th>Ngày Ký Kết</th>
                        <th>Tổng Giá Trị</th>
                        <th>Tiền Đặt Cọc</th>
                        <th>Mã Ưu Đãi</th>
                        <th>Trạng Thái</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="7" class="text-center py-5" style="color: #b0b3c0;">
                                <i class="bi bi-inbox fs-2 d-block mb-2 text-gold"></i>
                                Quý khách chưa có hợp đồng đặt cọc nào.
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${orders}" var="o">
                        <tr class="border-bottom border-secondary">
                            <td class="fw-bold text-gold">${o.orderCode}</td>
                            <td class="text-light"><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td class="fw-bold text-light"><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$"/></td>
                            <td class="text-gold fw-bold"><fmt:formatNumber value="${o.depositAmount}" type="currency" currencySymbol="$"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty o.couponCode}">
                                        <span class="badge bg-surface border border-gold text-gold">${o.couponCode}</span>
                                    </c:when>
                                    <c:otherwise><span class="small" style="color: #b0b3c0;">Không</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${o.status == 'COMPLETED'}"><span class="badge bg-success">Đã Bàn Giao</span></c:when>
                                    <c:when test="${o.status == 'CONFIRMED'}"><span class="badge bg-info text-dark">Đã Xác Nhận Cọc</span></c:when>
                                    <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-danger">Đã Hủy</span></c:when>
                                    <c:otherwise><span class="badge bg-warning text-dark">Đang Chờ Xử Lý</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/order-history?id=${o.orderId}" class="btn btn-outline-gold btn-sm px-3">
                                    Chi Tiết <i class="bi bi-eye-fill ms-1"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
