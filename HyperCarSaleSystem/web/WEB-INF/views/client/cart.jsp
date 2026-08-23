<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Giỏ Hàng Đặt Cọc Siêu Xe" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">GIỎ HÀNG <span class="text-gold">ĐẶT CỌC SIÊU XE</span></h1>
        <p class="page-subtitle">Kiểm tra các mẫu siêu xe đã chọn và tiến hành ký hợp đồng đặt cọc giữ xe</p>
    </div>
</div>

<div class="container section">
    <c:choose>
        <c:when test="${not empty cart && !cart.isEmpty()}">
            <div class="cart-layout">
                <!-- BẢNG DANH SÁCH XE TRONG GIỎ -->
                <div class="cart-table-col">
                    <div class="cart-table-card">
                        <table class="table cart-table">
                            <thead>
                                <tr>
                                    <th>Siêu Xe</th>
                                    <th>Tùy Chọn Bespoke</th>
                                    <th>Giá Niêm Yết</th>
                                    <th>Số Lượng</th>
                                    <th>Tiền Cọc (10%)</th>
                                    <th>Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${cart.items}">
                                    <tr>
                                        <!-- Ảnh & Tên xe -->
                                        <td class="cart-product-cell">
                                            <img src="${pageContext.request.contextPath}/${item.car.thumbnailUrl}" 
                                                 alt="${item.car.modelName}" class="cart-thumb">
                                            <div>
                                                <div class="font-bold">
                                                    <a href="${pageContext.request.contextPath}/MainController?action=CarDetail&id=${item.car.carId}">
                                                        ${item.car.modelName}
                                                    </a>
                                                </div>
                                                <span class="badge badge-brand">${item.car.brandName}</span>
                                            </div>
                                        </td>

                                        <!-- Màu sơn & Gói độ -->
                                        <td>
                                            <div class="font-sm">🎨 <strong>Màu:</strong> ${empty item.selectedColor ? 'Tiêu chuẩn' : item.selectedColor}</div>
                                            <c:if test="${not empty item.customOptions}">
                                                <div class="font-sm text-muted">✨ ${item.customOptions}</div>
                                            </c:if>
                                        </td>

                                        <!-- Giá niêm yết -->
                                        <td class="font-bold">
                                            <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                        </td>

                                        <!-- Cập nhật số lượng qua MainController -->
                                        <td>
                                            <form action="${pageContext.request.contextPath}/MainController" method="POST" class="qty-form">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                <input type="hidden" name="action" value="UpdateCart" />
                                                <input type="hidden" name="carId" value="${item.car.carId}" />
                                                <input type="number" name="quantity" value="${item.quantity}" min="1" max="10" 
                                                       class="form-control qty-input" onchange="this.form.submit()">
                                            </form>
                                        </td>

                                        <!-- Tiền cọc 10% -->
                                        <td class="text-gold font-bold">
                                            <fmt:formatNumber value="${item.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                        </td>

                                        <!-- Nút xóa xe qua MainController -->
                                        <td>
                                            <form action="${pageContext.request.contextPath}/MainController" method="POST" onsubmit="return confirm('Quý khách có chắc chắn muốn xóa siêu xe này khỏi giỏ?')">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                <input type="hidden" name="action" value="RemoveCart" />
                                                <input type="hidden" name="carId" value="${item.car.carId}" />
                                                <button type="submit" class="btn btn-icon text-danger" title="Xóa">🗑️</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Các nút thao tác phụ -->
                        <div class="cart-actions-row">
                            <a href="${pageContext.request.contextPath}/MainController?action=Cars" class="btn btn-outline">
                                ← TIẾP TỤC CHỌN XE
                            </a>
                            <form action="${pageContext.request.contextPath}/MainController" method="POST" onsubmit="return confirm('Quý khách có chắc chắn muốn xóa toàn bộ giỏ hàng?')">
                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                <input type="hidden" name="action" value="ClearCart" />
                                <button type="submit" class="btn btn-outline text-danger">XÓA SẠCH GIỎ HÀNG</button>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- TỔNG KẾT & TIẾN HÀNH ĐẶT CỌC -->
                <div class="cart-summary-col">
                    <div class="cart-summary-card">
                        <h3 class="summary-title">TỔNG GIÁ TRỊ HỢP ĐỒNG</h3>

                        <div class="summary-row">
                            <span>Tổng số lượng xe:</span>
                            <span class="font-bold">${cart.totalQuantity} chiếc</span>
                        </div>

                        <div class="summary-row">
                            <span>Tổng giá trị niêm yết:</span>
                            <span class="font-bold">
                                <fmt:formatNumber value="${cart.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </span>
                        </div>

                        <div class="summary-divider"></div>

                        <div class="summary-row total-deposit-row">
                            <div>
                                <strong>TỔNG TIỀN CỌC (10%):</strong>
                                <div class="font-sm text-muted">Thanh toán để xác nhận giữ xe độc bản</div>
                            </div>
                            <div class="total-deposit-val text-gold font-bold">
                                <fmt:formatNumber value="${cart.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </div>
                        </div>

                        <a href="${pageContext.request.contextPath}/MainController?action=Checkout" class="btn btn-gold btn-lg btn-block mt-4">
                            TIẾN HÀNH KÝ CỌC ONLINE →
                        </a>

                        <div class="cart-security-badge mt-3 text-center">
                            🔒 Giao dịch bảo mật bằng chuẩn mã hóa SSL 256-bit
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state p-5">
                <div class="empty-icon">🛒</div>
                <h2>Giỏ Hàng Đang Trống</h2>
                <p>Quý khách chưa lựa chọn mẫu siêu xe nào vào danh sách đặt cọc.</p>
                <a href="${pageContext.request.contextPath}/MainController?action=Cars" class="btn btn-gold btn-lg mt-3">
                    KHÁM PHÁ BỘ SƯU TẬP SIÊU XE
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
