/**
 * HYPERCAR SALE SYSTEM - JAVASCRIPT MASTER SCRIPT
 * Xử lý AJAX Live Search Realtime, AJAX Giỏ Hàng Nhảy Số & Kiểm Tra Voucher Đặt Cọc
 * 100% Vanilla JavaScript - KHÔNG THƯ VIỆN NGOÀI - CHẠY OFFLINE LOCAL HOÀN TOÀN
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Khởi tạo mã bảo mật CSRF
    const csrfToken = getCsrfToken();

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
    const meta = document.querySelector('meta[name="csrf-token"]');
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
    const searchInput = document.getElementById('liveSearchInput');
    const searchResults = document.getElementById('liveSearchResults');

    if (!searchInput || !searchResults) return;

    let debounceTimer;

    searchInput.addEventListener('input', function () {
        const query = this.value.trim();

        clearTimeout(debounceTimer);

        if (query.length < 2) {
            searchResults.innerHTML = '';
            searchResults.style.display = 'none';
            return;
        }

        debounceTimer = setTimeout(() => {
            const contextPath = getContextPath();
            fetch(`${contextPath}/api/search?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success' && data.data && data.data.length > 0) {
                        let html = '';
                        data.data.forEach(car => {
                            html += `
                                <a href="${contextPath}/car-detail?id=${car.carId}" class="search-item">
                                    <img src="${contextPath}/${car.thumbnailUrl}" alt="${car.modelName}" class="search-item-thumb">
                                    <div>
                                        <div class="search-item-title">${car.modelName}</div>
                                        <div class="search-item-brand">${car.brandName} • ${car.horsepower} HP</div>
                                    </div>
                                    <div class="search-item-price">${car.formattedPrice}</div>
                                </a>
                            `;
                        });
                        searchResults.innerHTML = html;
                        searchResults.style.display = 'block';
                    } else {
                        searchResults.innerHTML = '<div class="p-3 text-muted text-center font-sm">Không tìm thấy siêu xe phù hợp</div>';
                        searchResults.style.display = 'block';
                    }
                })
                .catch(err => {
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
    const addButtons = document.querySelectorAll('.btn-add-cart');
    const cartBadge = document.getElementById('cartBadge');

    addButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const carId = this.getAttribute('data-car-id');
            if (!carId) return;

            const contextPath = getContextPath();
            const csrf = getCsrfToken();

            const formData = new URLSearchParams();
            formData.append('action', 'add');
            formData.append('carId', carId);
            formData.append('quantity', '1');
            formData.append('csrf_token', csrf);

            fetch(`${contextPath}/api/cart`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-CSRF-Token': csrf
                },
                body: formData.toString()
            })
            .then(response => response.json())
            .then(res => {
                if (res.status === 'success') {
                    if (cartBadge) {
                        cartBadge.innerText = res.totalQuantity;
                        // Hiệu ứng nảy số
                        cartBadge.style.transform = 'scale(1.3)';
                        setTimeout(() => { cartBadge.style.transform = 'scale(1)'; }, 200);
                    }
                    showToast(`✓ ${res.message}`);
                } else {
                    showToast(`⚠️ ${res.message}`);
                }
            })
            .catch(err => {
                console.error('Lỗi thêm giỏ:', err);
                showToast('⚠️ Đã có lỗi xảy ra khi thêm giỏ hàng!');
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
    const btnApply = document.getElementById('btnApplyCoupon');
    const couponInput = document.getElementById('couponCodeInput');
    const couponMsg = document.getElementById('couponMessage');
    const discountRow = document.getElementById('discountRow');
    const discountValue = document.getElementById('discountValue');
    const finalDepositEl = document.getElementById('finalDepositAmount');

    if (!btnApply || !couponInput) return;

    btnApply.addEventListener('click', function () {
        const code = couponInput.value.trim();
        const totalAmount = this.getAttribute('data-total') || '0';

        if (!code) {
            if (couponMsg) {
                couponMsg.innerHTML = '<span class="text-danger">Vui lòng nhập mã ưu đãi!</span>';
                couponMsg.style.display = 'block';
            }
            return;
        }

        const contextPath = getContextPath();
        const csrf = getCsrfToken();

        const formData = new URLSearchParams();
        formData.append('couponCode', code);
        formData.append('totalAmount', totalAmount);
        formData.append('csrf_token', csrf);

        fetch(`${contextPath}/api/coupon/check`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-CSRF-Token': csrf
            },
            body: formData.toString()
        })
        .then(response => response.json())
        .then(res => {
            if (res.valid) {
                couponMsg.innerHTML = `<span class="text-success">${res.message}</span>`;
                couponMsg.style.display = 'block';

                if (discountRow) discountRow.style.display = 'flex';
                if (discountValue) discountValue.innerText = `-${res.formattedDiscount}`;

                // Tính toán tiền cọc mới (Tiền cọc gốc 10% trừ chiết khấu)
                const baseDeposit = parseFloat(totalAmount) * 0.10;
                const discount = parseFloat(res.discountAmount || 0);
                const finalDeposit = Math.max(0, baseDeposit - discount);

                if (finalDepositEl) {
                    finalDepositEl.innerText = '$' + finalDeposit.toLocaleString('en-US', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    });
                }
                showToast(`🎁 Áp dụng voucher ${res.code} thành công!`);
            } else {
                couponMsg.innerHTML = `<span class="text-danger">${res.message}</span>`;
                couponMsg.style.display = 'block';
                if (discountRow) discountRow.style.display = 'none';
            }
        })
        .catch(err => {
            console.error('Lỗi check coupon:', err);
        });
    });
}

/**
 * =========================================================================
 * 4. CHỌN PHƯƠNG THỨC THANH TOÁN (CHECKOUT PAGE)
 * =========================================================================
 */
function initPaymentMethodSelector() {
    const methodCards = document.querySelectorAll('.payment-method-card');
    methodCards.forEach(card => {
        card.addEventListener('click', function () {
            methodCards.forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            const radio = this.querySelector('input[type="radio"]');
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
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `<span>👑</span> <div>${message}</div>`;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => { toast.remove(); }, 400);
    }, 3500);
}
