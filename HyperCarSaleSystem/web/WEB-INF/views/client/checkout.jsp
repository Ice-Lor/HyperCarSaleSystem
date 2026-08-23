<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Ký Hợp Đồng Đặt Cọc Siêu Xe" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">XÁC NHẬN <span class="text-gold">HỢP ĐỒNG ĐẶT CỌC</span></h1>
        <p class="page-subtitle">Hoàn tất thông tin bàn giao và phương thức thanh toán tiền cọc 10% giữ xe độc bản</p>
    </div>
</div>

<div class="container section">
    <c:if test="${not empty error}">
        <div class="alert alert-danger mb-4">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="POST" id="checkoutForm">
        <!-- Mã bảo mật CSRF Token -->
        <input type="hidden" name="csrf_token" value="${csrfToken}" />

        <div class="checkout-layout">
            <!-- CỘT TRÁI: THÔNG TIN BÀN GIAO & PHƯƠNG THỨC THANH TOÁN -->
            <div class="checkout-form-col">
                <!-- 1. Thông tin người nhận xe -->
                <div class="card p-4 mb-4">
                    <h3 class="card-title">1. THÔNG TIN KHÁCH HÀNG & BÀN GIAO XE</h3>
                    
                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Họ Và Tên Khách Hàng <span class="text-danger">*</span></label>
                            <input type="text" name="fullName" class="form-control" 
                                   value="${empty param.fullName ? user.fullName : param.fullName}" required>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Số Điện Thoại VIP <span class="text-danger">*</span></label>
                            <input type="tel" name="phone" class="form-control" 
                                   value="${empty param.phone ? user.phone : param.phone}" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Địa Chỉ Bàn Giao Xe Tận Nơi <span class="text-danger">*</span></label>
                        <input type="text" name="deliveryAddress" class="form-control" 
                               placeholder="vd: Biệt thự Chateau, Phú Mỹ Hưng, Quận 7, TP.HCM"
                               value="${empty param.deliveryAddress ? user.address : param.deliveryAddress}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Ghi Chú Yêu Cầu Bàn Giao Đặc Biệt</label>
                        <textarea name="note" class="form-control" rows="2" 
                                  placeholder="Yêu cầu về nghi thức bàn giao xe hoa, chuyên cơ vận chuyển bọc nhung kín...">${param.note}</textarea>
                    </div>
                </div>

                <!-- 2. Phương thức thanh toán tiền cọc -->
                <div class="card p-4">
                    <h3 class="card-title">2. HÌNH THỨC THANH TOÁN TIỀN CỌC</h3>
                    
                    <div class="payment-methods-grid">
                        <label class="payment-method-card active">
                            <input type="radio" name="paymentMethod" value="BANK_TRANSFER" checked>
                            <div class="pm-content">
                                <span class="pm-icon">🏦</span>
                                <div class="pm-text">
                                    <div class="pm-title font-bold">Chuyển Khoản Ngân Hàng VIP</div>
                                    <div class="font-sm text-muted">Vietcombank / Techcombank Private Banking</div>
                                </div>
                            </div>
                        </label>

                        <label class="payment-method-card">
                            <input type="radio" name="paymentMethod" value="DIRECT_SHOWROOM">
                            <div class="pm-content">
                                <span class="pm-icon">🏢</span>
                                <div class="pm-text">
                                    <div class="pm-title font-bold">Thanh Toán Trực Tiếp Showroom</div>
                                    <div class="font-sm text-muted">Tại Lounge VIP Landmark 81 hoặc Sala</div>
                                </div>
                            </div>
                        </label>

                        <label class="payment-method-card">
                            <input type="radio" name="paymentMethod" value="CRYPTO">
                            <div class="pm-content">
                                <span class="pm-icon">🪙</span>
                                <div class="pm-text">
                                    <div class="pm-title font-bold">Thanh Toán Crypto (USDT / BTC)</div>
                                    <div class="font-sm text-muted">Ví bảo mật phân tán đa chữ ký (Multi-Sig)</div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>
            </div>

            <!-- CỘT PHẢI: CHI TIẾT ĐƠN & MÃ GIẢM GIÁ AJAX -->
            <div class="checkout-summary-col">
                <div class="card p-4 sticky-top">
                    <h3 class="card-title">TỔNG KẾT ĐẶT CỌC</h3>

                    <!-- Danh sách xe trong đơn -->
                    <div class="checkout-car-list">
                        <c:forEach var="item" items="${cart.items}">
                            <div class="checkout-car-item">
                                <img src="${pageContext.request.contextPath}/${item.car.thumbnailUrl}" alt="${item.car.modelName}">
                                <div class="checkout-car-info">
                                    <div class="font-bold">${item.car.modelName}</div>
                                    <div class="font-sm text-muted">${item.selectedColor} x ${item.quantity}</div>
                                    <div class="text-gold font-bold">
                                        <fmt:formatNumber value="${item.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="summary-divider"></div>

                    <!-- Ô NHẬP VOUCHER / COUPON AJAX REALTIME -->
                    <div class="coupon-section">
                        <label class="form-label font-bold">Mã Ưu Đãi / Voucher VIP:</label>
                        <div class="coupon-input-group">
                            <input type="text" id="couponCodeInput" name="couponCode" class="form-control" 
                                   placeholder="vd: VIP50K, HYPER2026" autocomplete="off">
                            <button type="button" id="btnApplyCoupon" class="btn btn-gold" 
                                    data-total="${cart.totalAmount}">ÁP DỤNG</button>
                        </div>
                        <div id="couponMessage" class="coupon-msg mt-2 font-sm" style="display: none;"></div>
                    </div>

                    <div class="summary-divider"></div>

                    <!-- Bảng tính số tiền -->
                    <div class="summary-row">
                        <span>Tổng giá trị niêm yết:</span>
                        <span class="font-bold">
                            <fmt:formatNumber value="${cart.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </span>
                    </div>

                    <div class="summary-row">
                        <span>Tiền cọc gốc (10%):</span>
                        <span class="font-bold">
                            <fmt:formatNumber value="${cart.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </span>
                    </div>

                    <div class="summary-row discount-row text-success" id="discountRow" style="display: none;">
                        <span>Chiết khấu Voucher:</span>
                        <span class="font-bold" id="discountValue">-$0.00</span>
                    </div>

                    <div class="summary-divider"></div>

                    <div class="summary-row final-total-row">
                        <div>
                            <strong class="text-lg">TIỀN CỌC CẦN THANH TOÁN:</strong>
                        </div>
                        <div class="final-amount text-gold font-bold text-xl" id="finalDepositAmount">
                            <fmt:formatNumber value="${cart.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-gold btn-lg btn-block mt-4">
                        ✍️ XÁC NHẬN KÝ CỌC SIÊU XE
                    </button>

                    <p class="font-sm text-muted text-center mt-3">
                        Bằng việc bấm xác nhận, quý khách đồng ý với Quy chế giao dịch và Thỏa thuận bảo mật của HyperCar Showroom.
                    </p>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="../common/footer.jsp" />
