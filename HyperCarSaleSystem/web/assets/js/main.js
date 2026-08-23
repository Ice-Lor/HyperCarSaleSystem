/**
 * HYPERCAR SALE SYSTEM - JAVASCRIPT MASTER SCRIPT
 * Xử lý AJAX Live Search Realtime, AJAX Giỏ Hàng Nhảy Số & Kiểm Tra Voucher Đặt Cọc
 * 100% Vanilla JavaScript - KHÔNG THƯ VIỆN NGOÀI - CHẠY OFFLINE LOCAL HOÀN TOÀN
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Khởi tạo mã bảo mật CSRF
    var csrfToken = getCsrfToken();

    // 2. Khởi tạo tính năng AJAX Live Search
    initLiveSearch();

    // 3. Khởi tạo tính năng AJAX Thêm Xe Vào Giỏ
    initAddToCart();

    // 4. Khởi tạo tính năng AJAX Kiểm Tra Mã Giảm Giá
    initCouponChecker();

    // 5. Khởi tạo chọn thẻ thanh toán trên trang Checkout
    initPaymentMethodSelector();
});

/**
 * Lấy mã CSRF Token từ thẻ meta trong header
 */
function getCsrfToken() {
    var meta = document.querySelector('meta[name="csrf-token"]');
    return meta ? meta.getAttribute('content') : '';
}

/**
 * Lấy contextPath của ứng dụng
 */
function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2)) || '';
}

/**
 * =========================================================================
 * 1. AJAX LIVE SEARCH REALTIME DROPDOWN
 * =========================================================================
 */
function initLiveSearch() {
    var searchInput = document.getElementById('liveSearchInput');
    var searchResults = document.getElementById('liveSearchResults');

    if (!searchInput || !searchResults) return;

    var debounceTimer;

    searchInput.addEventListener('input', function () {
        var query = this.value.trim();

        clearTimeout(debounceTimer);

        if (query.length < 2) {
            searchResults.innerHTML = '';
            searchResults.style.display = 'none';
            return;
        }

        debounceTimer = setTimeout(function () {
            var contextPath = getContextPath();
            fetch(contextPath + '/api/search?q=' + encodeURIComponent(query))
                .then(function (response) { return response.json(); })
                .then(function (data) {
                    if (data.status === 'success' && data.data && data.data.length > 0) {
                        var html = '';
                        data.data.forEach(function (car) {
                            html += '<a href="' + contextPath + '/car-detail?id=' + car.carId + '" class="search-item">' +
                                    '<img src="' + contextPath + '/' + car.thumbnailUrl + '" alt="' + car.modelName + '" class="search-item-thumb">' +
                                    '<div>' +
                                        '<div class="search-item-title">' + car.modelName + '</div>' +
                                        '<div class="search-item-brand">' + car.brandName + ' &bull; ' + car.horsepower + ' HP</div>' +
                                    '</div>' +
                                    '<div class="search-item-price">' + car.formattedPrice + '</div>' +
                                '</a>';
                        });
                        searchResults.innerHTML = html;
                        searchResults.style.display = 'block';
                    } else {
                        searchResults.innerHTML = '<div class="p-3 text-muted text-center font-sm">Kh&ocirc;ng t&igrave;m th&#7845;y si&ecirc;u xe ph&ugrave; h&#7907;p</div>';
                        searchResults.style.display = 'block';
                    }
                })
                .catch(function (err) {
                    console.error('Lỗi tìm kiếm live:', err);
                });
        }, 250);
    });

    // Đóng dropdown khi click ra ngoài
    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.style.display = 'none';
        }
    });

    // Mở lại dropdown khi focus nếu có chữ
    searchInput.addEventListener('focus', function () {
        if (this.value.trim().length >= 2 && searchResults.children.length > 0) {
            searchResults.style.display = 'block';
        }
    });
}

/**
 * =========================================================================
 * 2. AJAX THÊM SIÊU XE VÀO GIỎ CỌC
 * =========================================================================
 */
function initAddToCart() {
    var addButtons = document.querySelectorAll('.btn-add-cart');
    var cartBadge = document.getElementById('cartBadge');

    addButtons.forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            var carId = this.getAttribute('data-car-id');
            if (!carId) return;

            var contextPath = getContextPath();
            var csrf = getCsrfToken();

            var formData = new URLSearchParams();
            formData.append('action', 'add');
            formData.append('carId', carId);
            formData.append('quantity', '1');
            formData.append('csrf_token', csrf);

            fetch(contextPath + '/api/cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-CSRF-Token': csrf
                },
                body: formData.toString()
            })
            .then(function (response) { return response.json(); })
            .then(function (res) {
                if (res.status === 'success') {
                    if (cartBadge) {
                        cartBadge.innerText = res.totalQuantity;
                        // Hiệu ứng nảy số
                        cartBadge.style.transform = 'scale(1.3)';
                        setTimeout(function () { cartBadge.style.transform = 'scale(1)'; }, 200);
                    }
                    showToast(res.message);
                } else {
                    showToast(res.message);
                }
            })
            .catch(function (err) {
                console.error('Lỗi thêm giỏ:', err);
                showToast('Đã có lỗi xảy ra khi thêm giỏ hàng!');
            });
        });
    });
}

/**
 * =========================================================================
 * 3. AJAX KIỂM TRA MÃ GIẢM GIÁ VOUCHER
 * =========================================================================
 */
function initCouponChecker() {
    var btnApply = document.getElementById('btnApplyCoupon');
    var couponInput = document.getElementById('couponCodeInput');
    var couponMsg = document.getElementById('couponMessage');
    var discountRow = document.getElementById('discountRow');
    var discountValue = document.getElementById('discountValue');
    var finalDepositEl = document.getElementById('finalDepositAmount');

    if (!btnApply || !couponInput) return;

    couponInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            btnApply.click();
        }
    });

    btnApply.addEventListener('click', function () {
        var code = couponInput.value.trim();
        var totalAmount = this.getAttribute('data-total') || '0';

        if (!code) {
            if (couponMsg) {
                couponMsg.innerHTML = '<span class="text-danger">Vui lòng nhập mã ưu đãi!</span>';
                couponMsg.style.display = 'block';
            }
            return;
        }

        var contextPath = getContextPath();
        var csrf = getCsrfToken();

        var formData = new URLSearchParams();
        formData.append('couponCode', code);
        formData.append('totalAmount', totalAmount);
        formData.append('csrf_token', csrf);

        fetch(contextPath + '/api/coupon/check', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-CSRF-Token': csrf
            },
            body: formData.toString()
        })
        .then(function (response) { return response.json(); })
        .then(function (res) {
            if (res.valid) {
                couponMsg.innerHTML = '<span class="text-success">' + res.message + '</span>';
                couponMsg.style.display = 'block';

                if (discountRow) discountRow.style.display = 'flex';
                if (discountValue) discountValue.innerText = '-' + res.formattedDiscount;

                // Tính toán tiền cọc mới (Tiền cọc gốc 10% trừ chiết khấu)
                var baseDeposit = parseFloat(totalAmount) * 0.10;
                var discount = parseFloat(res.discountAmount || 0);
                var finalDeposit = Math.max(0, baseDeposit - discount);

                if (finalDepositEl) {
                    finalDepositEl.innerText = '$' + finalDeposit.toLocaleString('en-US', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    });
                }
                showToast('Áp dụng voucher ' + res.code + ' thành công!');
            } else {
                couponMsg.innerHTML = '<span class="text-danger">' + res.message + '</span>';
                couponMsg.style.display = 'block';
                if (discountRow) discountRow.style.display = 'none';
                showToast(res.message);
            }
        })
        .catch(function (err) {
            console.error('Lỗi check coupon:', err);
            if (couponMsg) {
                couponMsg.innerHTML = '<span class="text-danger">Đã có lỗi xảy ra khi kiểm tra mã voucher!</span>';
                couponMsg.style.display = 'block';
            }
        });
    });
}

/**
 * =========================================================================
 * 4. CHỌN PHƯƠNG THỨC THANH TOÁN (CHECKOUT PAGE)
 * =========================================================================
 */
function initPaymentMethodSelector() {
    var methodCards = document.querySelectorAll('.payment-method-card');
    methodCards.forEach(function (card) {
        card.addEventListener('click', function () {
            methodCards.forEach(function (c) { c.classList.remove('active'); });
            this.classList.add('active');
            var radio = this.querySelector('input[type="radio"]');
            if (radio) radio.checked = true;
        });
    });
}

/**
 * =========================================================================
 * 5. HIỂN THỊ TOAST THÔNG BÁO THỜI GIAN THỰC
 * =========================================================================
 */
function showToast(message) {
    var container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    var toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = '<span class="text-gold" style="font-size: 1.1rem; margin-right: 6px;">&#10003;</span> <div>' + message + '</div>';

    container.appendChild(toast);

    setTimeout(function () {
        toast.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(function () { toast.remove(); }, 400);
    }, 3500);
}
