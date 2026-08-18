document.addEventListener('DOMContentLoaded', function () {
    // 1. AJAX Live Search
    const searchInput = document.getElementById('globalSearchInput');
    const searchDropdown = document.getElementById('searchResultsDropdown');

    if (searchInput && searchDropdown) {
        let debounceTimeout;
        searchInput.addEventListener('input', function () {
            clearTimeout(debounceTimeout);
            const query = this.value.trim();

            if (query.length < 2) {
                searchDropdown.style.display = 'none';
                searchDropdown.innerHTML = '';
                return;
            }

            debounceTimeout = setTimeout(() => {
                const contextPath = document.body.getAttribute('data-context-path') || '';
                fetch(contextPath + '/api/search?q=' + encodeURIComponent(query))
                    .then(response => response.json())
                    .then(cars => {
                        if (!cars || cars.length === 0) {
                            searchDropdown.innerHTML = '<div class="p-3 text-muted text-center">Không tìm thấy siêu xe phù hợp</div>';
                            searchDropdown.style.display = 'block';
                            return;
                        }

                        let html = '';
                        cars.forEach(car => {
                            const priceFormatted = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(car.price);
                            html += `
                                <a href="${contextPath}/car-detail?id=${car.carId}" class="search-item">
                                    <img src="${car.thumbnailUrl}" alt="${car.modelName}">
                                    <div>
                                        <div class="fw-bold">${car.modelName}</div>
                                        <small class="text-gold">${car.brandName} • ${priceFormatted}</small>
                                    </div>
                                </a>
                            `;
                        });
                        searchDropdown.innerHTML = html;
                        searchDropdown.style.display = 'block';
                    })
                    .catch(err => console.error('Live search error:', err));
            }, 300);
        });

        document.addEventListener('click', function (e) {
            if (!searchInput.contains(e.target) && !searchDropdown.contains(e.target)) {
                searchDropdown.style.display = 'none';
            }
        });
    }

    // 2. AJAX Coupon Validator
    const applyCouponBtn = document.getElementById('btnApplyCoupon');
    const couponInput = document.getElementById('couponCodeInput');
    const couponMsg = document.getElementById('couponMessage');
    const totalDepositEl = document.getElementById('checkoutDepositAmount');

    if (applyCouponBtn && couponInput) {
        applyCouponBtn.addEventListener('click', function () {
            const code = couponInput.value.trim();
            const rawAmount = this.getAttribute('data-total-amount');
            const contextPath = document.body.getAttribute('data-context-path') || '';

            if (!code) {
                couponMsg.innerHTML = '<span class="text-danger">Vui lòng nhập mã ưu đãi VIP!</span>';
                return;
            }

            fetch(`${contextPath}/api/coupon?code=${encodeURIComponent(code)}&amount=${rawAmount}`)
                .then(r => r.json())
                .then(data => {
                    if (data.valid) {
                        couponMsg.innerHTML = `<span class="text-success"><i class="bi bi-check-circle-fill"></i> ${data.message}</span>`;
                        if (totalDepositEl) {
                            const currentDeposit = parseFloat(totalDepositEl.getAttribute('data-raw-deposit'));
                            const discount = (currentDeposit * data.discountPercent) / 100;
                            const maxDisc = parseFloat(data.maxDiscount);
                            const actualDiscount = Math.min(discount, maxDisc);
                            const newDeposit = currentDeposit - actualDiscount;
                            totalDepositEl.innerText = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(newDeposit);
                        }
                    } else {
                        couponMsg.innerHTML = `<span class="text-danger"><i class="bi bi-exclamation-triangle-fill"></i> ${data.message}</span>`;
                    }
                })
                .catch(err => console.error('Coupon check error:', err));
        });
    }
});
