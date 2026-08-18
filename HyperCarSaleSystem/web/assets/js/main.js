/**
 * HYPERCAR SALE SYSTEM - CLIENT-SIDE JAVASCRIPT
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Live Search Autocomplete
    const searchInput = document.getElementById('globalSearchInput');
    const searchResults = document.getElementById('searchResults');

    if (searchInput && searchResults) {
        let debounceTimer;

        searchInput.addEventListener('input', (e) => {
            clearTimeout(debounceTimer);
            const query = e.target.value.trim();

            if (query.length < 2) {
                searchResults.style.display = 'none';
                searchResults.innerHTML = '';
                return;
            }

            debounceTimer = setTimeout(() => {
                const contextPath = document.body.dataset.context || '';
                fetch(`${contextPath}/api/cars/search?q=${encodeURIComponent(query)}`)
                    .then(res => res.json())
                    .then(cars => {
                        if (!cars || cars.length === 0) {
                            searchResults.innerHTML = '<div class="p-3 text-muted text-center">Không tìm thấy siêu xe phù hợp</div>';
                            searchResults.style.display = 'block';
                            return;
                        }

                        let html = '';
                        cars.forEach(car => {
                            const formattedPrice = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(car.price);
                            html += `
                                <a href="${contextPath}/car-detail?id=${car.carId}" class="search-item">
                                    <img src="${car.thumbnailUrl}" alt="${car.modelName}">
                                    <div>
                                        <div class="fw-bold">${car.modelName}</div>
                                        <div class="small text-muted">${car.brandName} • <span class="text-warning">${formattedPrice}</span></div>
                                    </div>
                                </a>
                            `;
                        });
                        searchResults.innerHTML = html;
                        searchResults.style.display = 'block';
                    })
                    .catch(err => console.error('Search error:', err));
            }, 300);
        });

        // Close search results when clicking outside
        document.addEventListener('click', (e) => {
            if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
                searchResults.style.display = 'none';
            }
        });
    }

    // 2. Coupon Validation in Checkout
    const btnApplyCoupon = document.getElementById('btnApplyCoupon');
    const couponInput = document.getElementById('couponCodeInput');
    const couponMessage = document.getElementById('couponMessage');

    if (btnApplyCoupon && couponInput) {
        btnApplyCoupon.addEventListener('click', () => {
            const code = couponInput.value.trim();
            if (!code) return;

            const contextPath = document.body.dataset.context || '';
            const formData = new URLSearchParams();
            formData.append('couponCode', code);

            fetch(`${contextPath}/api/coupon/check`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    if (couponMessage) {
                        couponMessage.className = 'text-success small mt-1';
                        couponMessage.innerText = data.message + ` (Giảm -$${data.discountAmount.toLocaleString()})`;
                    }
                    const totalElem = document.getElementById('summaryFinalTotal');
                    const depositElem = document.getElementById('summaryFinalDeposit');
                    if (totalElem) totalElem.innerText = '$' + data.finalTotal.toLocaleString();
                    if (depositElem) depositElem.innerText = '$' + data.finalDeposit.toLocaleString();
                } else {
                    if (couponMessage) {
                        couponMessage.className = 'text-danger small mt-1';
                        couponMessage.innerText = data.message;
                    }
                }
            })
            .catch(err => console.error('Coupon error:', err));
        });
    }
});
