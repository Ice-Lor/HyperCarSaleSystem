<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Ký Kết Đặt Cọc - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="mb-4">
        <h2 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">XÁC NHẬN HỢP ĐỒNG ĐẶT CỌC XE VIP</h2>
        <p class="small" style="color: #b0b3c0;">Quý khách vui lòng kiểm tra thông tin giao nhận và hoàn tất thanh toán cọc giữ xe</p>
    </div>

    <form action="${pageContext.request.contextPath}/checkout" method="POST">
        <input type="hidden" name="csrf_token" value="${csrfToken}">

        <div class="row g-4">
            <!-- Customer & Delivery Form -->
            <div class="col-lg-7">
                <div class="card card-luxury p-4 mb-4">
                    <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                        <i class="bi bi-person-vcard-fill me-2"></i> THÔNG TIN KHÁCH HÀNG VIP
                    </h5>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Họ và tên chủ sở hữu</label>
                            <input type="text" class="form-control bg-dark border-secondary text-light" 
                                   value="${sessionScope.currentUser.fullName != null ? sessionScope.currentUser.fullName : sessionScope.currentUser.username}" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Email liên hệ VIP</label>
                            <input type="email" class="form-control bg-dark border-secondary text-light" 
                                   value="${sessionScope.currentUser.email}" readonly>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Số điện thoại nhận bàn giao *</label>
                            <input type="tel" name="phone" class="form-control bg-dark border-secondary text-light" 
                                   value="${sessionScope.currentUser.phone}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Phương thức thanh toán cọc *</label>
                            <select name="paymentMethod" class="form-select bg-dark border-secondary text-light">
                                <option value="BANK_TRANSFER">Chuyển Khoản Ngân Hàng VIP (Ưu tiên)</option>
                                <option value="CRYPTO">Thanh Toán Crypto (USDT / BTC)</option>
                                <option value="DIRECT_CASH">Thanh Toán Tiền Mặt Tại Showroom</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Địa chỉ bàn giao siêu xe tận nơi *</label>
                        <input type="text" name="deliveryAddress" class="form-control bg-dark border-secondary text-light" 
                               value="${sessionScope.currentUser.address}" placeholder="Biệt thự, dinh thự hoặc cảng hàng không VIP" required>
                    </div>

                    <div class="mb-2">
                        <label class="form-label small" style="color: #b0b3c0;">Yêu cầu đặc biệt trong lễ bàn giao (Tùy chọn)</label>
                        <textarea name="note" class="form-control bg-dark border-secondary text-light" rows="2" 
                                  placeholder="Ví dụ: Rượu vang Champagne Dom Pérignon, Pháo hoa, Thảm đỏ..."></textarea>
                    </div>
                </div>

                <!-- Voucher Code Section -->
                <div class="card card-luxury p-4">
                    <h5 class="fw-bold text-gold mb-3" style="font-family: 'Cinzel', serif;">
                        <i class="bi bi-ticket-perforated-fill me-2"></i> MÃ ƯU ĐÃI VIP (VOUCHER)
                    </h5>
                    <div class="input-group mb-2">
                        <input type="text" id="couponCodeInput" name="couponCode" class="form-control bg-dark border-secondary text-light text-uppercase" 
                               placeholder="Nhập mã ưu đãi (VIP50K, HYPER2026...)">
                        <button type="button" id="btnApplyCoupon" class="btn btn-outline-gold" data-total-amount="${sessionScope.cart.totalAmount}">
                            Áp Dụng
                        </button>
                    </div>
                    <div id="couponMessage" class="small"></div>
                </div>
            </div>

            <!-- Order Review Box -->
            <div class="col-lg-5">
                <div class="card card-luxury p-4 sticky-top" style="top: 90px;">
                    <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                        CHI TIẾT ĐƠN ĐẶT CỌC
                    </h5>

                    <div class="d-flex flex-column gap-3 mb-4">
                        <c:forEach items="${sessionScope.cart.items}" var="item">
                            <div class="d-flex align-items-center justify-content-between">
                                <div class="d-flex align-items-center">
                                    <img src="${pageContext.request.contextPath}/${item.car.thumbnailUrl}" style="width: 50px; height: 35px; object-fit: cover;" class="rounded me-2">
                                    <div>
                                        <div class="fw-bold text-light small">${item.car.modelName} (x${item.quantity})</div>
                                        <small style="font-size: 0.75rem; color: #b0b3c0;">Màu: ${item.selectedColor}</small>
                                    </div>
                                </div>
                                <span class="fw-bold text-gold small">
                                    <fmt:formatNumber value="${item.itemDeposit}" type="currency" currencySymbol="$"/>
                                </span>
                            </div>
                        </c:forEach>
                    </div>

                    <hr class="border-secondary">

                    <div class="d-flex justify-content-between small mb-2" style="color: #b0b3c0;">
                        <span>Tổng giá trị đơn hàng:</span>
                        <span class="text-light fw-bold">
                            <fmt:formatNumber value="${sessionScope.cart.totalAmount}" type="currency" currencySymbol="$"/>
                        </span>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <span class="fw-bold text-light">Số tiền đặt cọc cần trả:</span>
                        <span class="fs-4 fw-bold text-gold" id="checkoutDepositAmount" data-raw-deposit="${sessionScope.cart.totalDeposit}">
                            <fmt:formatNumber value="${sessionScope.cart.totalDeposit}" type="currency" currencySymbol="$"/>
                        </span>
                    </div>

                    <button type="submit" class="btn btn-gold w-100 py-3 fw-bold">
                        <i class="bi bi-shield-lock-fill me-2"></i> Xác Nhận Đặt Cọc & Tạo Hợp Đồng
                    </button>

                    <div class="mt-3 small text-center" style="color: #b0b3c0;">
                        <i class="bi bi-file-earmark-lock-fill text-gold me-1"></i> Ràng buộc giao dịch qua JDBC Transaction tự động
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
