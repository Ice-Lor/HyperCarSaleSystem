<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Giỏ Xe Đặt Cọc - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="mb-4">
        <h2 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">GIỎ HÀNG SIÊU XE ĐẶT CỌC</h2>
        <p class="small" style="color: #b0b3c0;">Danh sách các siêu phẩm quý khách đã chọn để tiến hành giữ chỗ và ký kết hợp đồng</p>
    </div>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger py-2 small mb-4">${sessionScope.errorMessage}</div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty sessionScope.cart or empty sessionScope.cart.items}">
            <div class="card card-luxury p-5 text-center my-4" style="color: #b0b3c0;">
                <i class="bi bi-bag-x text-gold fs-1 mb-3"></i>
                <h5 class="text-light">Giỏ hàng siêu xe hiện đang trống</h5>
                <p class="small">Đại ca hãy khám phá bộ sưu tập và chọn những mẫu xe ưng ý nhất.</p>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold px-4">
                        <i class="bi bi-grid-fill me-1"></i> Khám Phá Showroom
                    </a>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="row g-4">
                <!-- Items Table -->
                <div class="col-lg-8">
                    <div class="card card-luxury p-3">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                                <thead>
                                    <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                                        <th>Siêu Xe</th>
                                        <th>Tùy Chọn</th>
                                        <th>Giá Bán</th>
                                        <th>Số Lượng</th>
                                        <th>Tiền Cọc (${sessionScope.cart.items[0].car.depositRate}%)</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${sessionScope.cart.items}" var="item">
                                        <tr class="border-bottom border-secondary">
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="${pageContext.request.contextPath}/${item.car.thumbnailUrl}" alt="${item.car.modelName}" 
                                                         style="width: 80px; height: 50px; object-fit: cover;" class="rounded me-3">
                                                    <div>
                                                        <a href="${pageContext.request.contextPath}/car-detail?id=${item.car.carId}" 
                                                           class="fw-bold text-light text-decoration-none hover-gold d-block">
                                                            ${item.car.modelName}
                                                        </a>
                                                        <small class="text-gold">${item.car.brandName}</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <small class="text-light d-block"><i class="bi bi-palette text-gold me-1"></i> ${item.selectedColor}</small>
                                                <c:if test="${not empty item.customOptions}">
                                                    <small class="d-block" style="font-size: 0.75rem; color: #b0b3c0;">Bespoke: ${item.customOptions}</small>
                                                </c:if>
                                            </td>
                                            <td class="text-nowrap text-light">
                                                <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="$"/>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST" class="d-flex align-items-center gap-1">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="carId" value="${item.car.carId}">
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.car.stockQuantity}" 
                                                           class="form-control form-control-sm bg-dark border-secondary text-light text-center" style="width: 60px;" 
                                                           onchange="this.form.submit()">
                                                </form>
                                            </td>
                                            <td class="fw-bold text-gold text-nowrap">
                                                <fmt:formatNumber value="${item.itemDeposit}" type="currency" currencySymbol="$"/>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="carId" value="${item.car.carId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm border-0">
                                                        <i class="bi bi-trash3-fill"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="d-flex justify-content-between mt-3 pt-3 border-top border-secondary">
                            <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-gold btn-sm">
                                <i class="bi bi-arrow-left me-1"></i> Tiếp Tục Chọn Xe
                            </a>
                            <form action="${pageContext.request.contextPath}/cart" method="POST">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-outline-secondary btn-sm text-light">
                                    <i class="bi bi-x-circle me-1"></i> Xóa Toàn Bộ Giỏ
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Summary Box -->
                <div class="col-lg-4">
                    <div class="card card-luxury p-4">
                        <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                            TỔNG KẾT ĐƠN ĐẶT CỌC
                        </h5>

                        <div class="d-flex justify-content-between mb-2 small" style="color: #b0b3c0;">
                            <span>Tổng giá trị hợp đồng:</span>
                            <span class="text-light fw-bold">
                                <fmt:formatNumber value="${sessionScope.cart.totalAmount}" type="currency" currencySymbol="$"/>
                            </span>
                        </div>
                        <div class="d-flex justify-content-between mb-3 small" style="color: #b0b3c0;">
                            <span>Tổng số lượng xe:</span>
                            <span class="text-light">${sessionScope.cart.totalQuantity} chiếc</span>
                        </div>

                        <hr class="border-secondary">

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="fw-bold text-light">Tiền Đặt Cọc Cần Thanh Toán:</span>
                            <span class="fs-4 fw-bold text-gold">
                                <fmt:formatNumber value="${sessionScope.cart.totalDeposit}" type="currency" currencySymbol="$"/>
                            </span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-gold w-100 py-3 fw-bold">
                            <i class="bi bi-shield-check me-1"></i> Tiến Hành Đặt Cọc
                        </a>

                        <div class="mt-3 small text-center" style="color: #b0b3c0;">
                            <i class="bi bi-lock-fill text-gold me-1"></i> Giao dịch bảo mật cấp cao bằng JDBC Transaction
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
