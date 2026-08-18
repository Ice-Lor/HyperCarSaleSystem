<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Danh Sách Đặt Cọc - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <h2 class="font-brand fw-bold text-white mb-4">
        <i class="bi bi-cart3 gold-text me-2"></i> DANH SÁCH SIÊU XE ĐẶT CỌC
    </h2>

    <c:choose>
        <c:when test="${empty sessionScope.cart or empty sessionScope.cart.items}">
            <div class="hyper-card p-5 text-center my-5">
                <i class="bi bi-cart-x fs-1 text-secondary mb-3 d-block"></i>
                <h4 class="text-white">Danh sách đặt cọc đang trống</h4>
                <p class="text-secondary small">Đại ca chưa chọn mẫu siêu xe nào vào danh sách.</p>
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-sm mt-2">
                    <i class="bi bi-grid-fill me-1"></i> Khám Phá Bộ Sưu Tập
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <!-- Cart Items Table -->
                <div class="col-lg-8">
                    <div class="hyper-card p-4">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th>Siêu Xe</th>
                                        <th>Màu Sơn</th>
                                        <th>Giá Niêm Yết</th>
                                        <th>Số Lượng</th>
                                        <th>Tiền Cọc (10-20%)</th>
                                        <th>Thao Tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${sessionScope.cart.items}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="${item.car.thumbnailUrl}" alt="${item.car.modelName}" class="rounded me-3" style="width: 70px; height: 45px; object-fit: cover;">
                                                    <div>
                                                        <div class="fw-bold text-white">${item.car.modelName}</div>
                                                        <div class="small text-secondary">${item.car.brandName}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge bg-secondary">${not empty item.selectedColor ? item.selectedColor : 'Tiêu chuẩn'}</span>
                                            </td>
                                            <td class="text-warning fw-bold font-brand">
                                                <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST" class="d-flex align-items-center gap-1">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="carId" value="${item.car.carId}">
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.car.stockQuantity}" class="form-control form-control-dark form-control-sm text-center" style="width: 60px;">
                                                    <button type="submit" class="btn btn-sm btn-outline-secondary" title="Cập nhật">
                                                        <i class="bi bi-arrow-clockwise"></i>
                                                    </button>
                                                </form>
                                            </td>
                                            <td class="text-white fw-bold font-brand">
                                                <fmt:formatNumber value="${item.totalDeposit}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="carId" value="${item.car.carId}">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top border-secondary border-opacity-25">
                            <a href="${pageContext.request.contextPath}/cars" class="text-secondary small text-decoration-none">
                                <i class="bi bi-arrow-left me-1"></i> Tiếp tục chọn xe
                            </a>
                            <form action="${pageContext.request.contextPath}/cart" method="POST">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                    <i class="bi bi-trash3 me-1"></i> Xóa tất cả
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Summary Box -->
                <div class="col-lg-4">
                    <div class="hyper-card p-4">
                        <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                            TỔNG HỢP HỢP ĐỒNG CỌC
                        </h5>

                        <div class="d-flex justify-content-between text-secondary mb-2">
                            <span>Tổng giá trị xe:</span>
                            <span class="text-white fw-bold">
                                <fmt:formatNumber value="${sessionScope.cart.subTotal}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                            </span>
                        </div>

                        <div class="d-flex justify-content-between text-secondary mb-3">
                            <span>Tổng tiền cọc cần thanh toán:</span>
                            <span class="text-warning fw-bold fs-5 font-brand">
                                <fmt:formatNumber value="${sessionScope.cart.totalDeposit}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                            </span>
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-gold btn-lg">
                                <i class="bi bi-credit-card-2-front-fill me-2"></i> TIẾN HÀNH ĐẶT CỌC
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
