<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Thủ Tục Đặt Cọc Hợp Đồng - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <h2 class="font-brand fw-bold text-white mb-4">
        <i class="bi bi-shield-check gold-text me-2"></i> THỦ TỤC ĐẶT CỌC & BÀN GIAO SIÊU XE
    </h2>

    <form action="${pageContext.request.contextPath}/checkout" method="POST">
        <input type="hidden" name="csrf_token" value="${csrfToken}">

        <div class="row g-4">
            <!-- Left Info Form -->
            <div class="col-lg-7">
                <div class="hyper-card p-4 mb-4">
                    <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                        <i class="bi bi-person-lines-fill me-2"></i> THÔNG TIN CHỦ SỞ HỮU
                    </h5>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">HỌ VÀ TÊN KHÁCH HÀNG VIP</label>
                        <input type="text" class="form-control form-control-dark" value="${sessionScope.currentUser.fullName}" readonly>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">EMAIL LIÊN HỆ</label>
                            <input type="email" class="form-control form-control-dark" value="${sessionScope.currentUser.email}" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">SỐ ĐIỆN THOẠI NHẬN XE (*)</label>
                            <input type="text" name="phone" class="form-control form-control-dark" value="${sessionScope.currentUser.phone}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">ĐỊA CHỈ BÀN GIAO TẬN NƠI (BIỆT THỰ / SHOWROOM) (*)</label>
                        <input type="text" name="deliveryAddress" class="form-control form-control-dark" value="${sessionScope.currentUser.address}" placeholder="VD: Biệt thự số 10 Thảo Điền, TP. Thủ Đức" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">GHI CHÚ / YÊU CẦU ĐẶC BIỆT</label>
                        <textarea name="note" rows="3" class="form-control form-control-dark" placeholder="Yêu cầu giao bằng xe thùng kín chuyên dụng, bạt phủ bảo vệ..."></textarea>
                    </div>
                </div>

                <!-- Payment Method -->
                <div class="hyper-card p-4">
                    <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                        <i class="bi bi-wallet2 me-2"></i> HÌNH THỨC THANH TOÁN TIỀN CỌC
                    </h5>

                    <div class="form-check p-3 rounded mb-2 border border-secondary border-opacity-25" style="background: #181822;">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payBank" value="BANK_TRANSFER" checked>
                        <label class="form-check-label text-white fw-bold d-block" for="payBank">
                            <i class="bi bi-bank me-2 text-warning"></i> Chuyển Khoản Ngân Hàng Doanh Nghiệp VIP (Swift / Vietcombank Priority)
                        </label>
                        <span class="text-secondary small">Nhận viên kiểm toán sẽ gửi thông tin ủy nhiệm chi trong 15 phút.</span>
                    </div>

                    <div class="form-check p-3 rounded mb-2 border border-secondary border-opacity-25" style="background: #181822;">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payCrypto" value="CRYPTO_USDT">
                        <label class="form-check-label text-white fw-bold d-block" for="payCrypto">
                            <i class="bi bi-currency-bitcoin me-2 text-warning"></i> Thanh Toán Tiền Kỹ Thuật Số (USDT / Bitcoin)
                        </label>
                        <span class="text-secondary small">Ví đa chữ ký bảo mật mạng lưới ERC20 / TRC20.</span>
                    </div>

                    <div class="form-check p-3 rounded border border-secondary border-opacity-25" style="background: #181822;">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payDirect" value="SHOWROOM_DIRECT">
                        <label class="form-check-label text-white fw-bold d-block" for="payDirect">
                            <i class="bi bi-building-check me-2 text-warning"></i> Ký Hợp Đồng & Thanh Toán Trực Tiếp Tại Showroom
                        </label>
                        <span class="text-secondary small">Showroom chuẩn bị sẵn phòng tiếp khách VIP và tiệc rượu vang.</span>
                    </div>
                </div>
            </div>

            <!-- Right Summary -->
            <div class="col-lg-5">
                <div class="hyper-card p-4 sticky-top" style="top: 90px;">
                    <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                        CHI TIẾT ĐƠN ĐẶT CỌC
                    </h5>

                    <!-- Items List -->
                    <div class="mb-3">
                        <c:forEach var="item" items="${sessionScope.cart.items}">
                            <div class="d-flex justify-content-between align-items-center mb-2 pb-2 border-bottom border-secondary border-opacity-10">
                                <div>
                                    <div class="fw-bold text-white small">${item.car.modelName} (x${item.quantity})</div>
                                    <div class="text-secondary small">Màu: ${item.selectedColor}</div>
                                </div>
                                <div class="text-warning fw-bold small font-brand">
                                    <fmt:formatNumber value="${item.totalDeposit}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Voucher Input -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">MÃ ƯU ĐÃI VIP (VOUCHER)</label>
                        <div class="input-group">
                            <input type="text" id="couponCodeInput" name="couponCode" class="form-control form-control-dark form-control-sm" placeholder="VD: VIP50K, HYPER2026">
                            <button type="button" id="btnApplyCoupon" class="btn btn-outline-gold btn-sm">Áp Dụng</button>
                        </div>
                        <div id="couponMessage"></div>
                    </div>

                    <!-- Price Calculations -->
                    <div class="pt-2 border-top border-secondary border-opacity-25">
                        <div class="d-flex justify-content-between text-secondary small mb-2">
                            <span>Tổng giá trị xe:</span>
                            <span id="summaryFinalTotal" class="text-white fw-bold">
                                <fmt:formatNumber value="${sessionScope.cart.finalTotal}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                            </span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="fw-bold text-white">TIỀN CỌC CẦN THANH TOÁN:</span>
                            <span id="summaryFinalDeposit" class="text-warning fw-bold fs-4 font-brand">
                                <fmt:formatNumber value="${sessionScope.cart.finalDeposit}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                            </span>
                        </div>
                    </div>

                    <div class="d-grid gap-2 mt-4">
                        <button type="submit" class="btn btn-gold btn-lg">
                            <i class="bi bi-check2-circle me-2"></i> XÁC NHẬN KÝ HỢP ĐỒNG CỌC
                        </button>
                    </div>

                    <p class="text-center text-secondary small mt-3 mb-0">
                        <i class="bi bi-lock-fill me-1"></i> Giao dịch được bảo hộ pháp lý & mã hóa bảo mật 256-bit
                    </p>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
