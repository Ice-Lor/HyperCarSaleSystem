<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="${car.modelName} - Chi Tiết Siêu Xe" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<!-- BREADCRUMB ĐIỀU HƯỚNG -->
<div class="breadcrumb-container">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="${pageContext.request.contextPath}/home">Trang Chủ</a></li>
            <li><a href="${pageContext.request.contextPath}/cars">Bộ Sưu Tập Xe</a></li>
            <li><a href="${pageContext.request.contextPath}/cars?brandId=${car.brandId}">${car.brandName}</a></li>
            <li class="active">${car.modelName}</li>
        </ul>
    </div>
</div>

<div class="container section">
    <!-- KHỐI CHI TIẾT CHÍNH (GALLERY & SPECS) -->
    <div class="car-detail-layout">
        <!-- CỘT TRÁI: BỘ SƯU TẬP HÌNH ẢNH GALLERY -->
        <div class="car-gallery-col">
            <div class="main-image-wrap">
                <img id="mainCarImage" src="${pageContext.request.contextPath}/${car.thumbnailUrl}" 
                     alt="${car.modelName}" class="main-car-img">
            </div>

            <!-- Danh sách ảnh Thumbnail nhỏ chuyển đổi -->
            <div class="gallery-thumbs-row">
                <div class="thumb-item active" onclick="changeMainImage('${pageContext.request.contextPath}/${car.thumbnailUrl}', this)">
                    <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" alt="${car.modelName}">
                </div>
                <c:forEach var="img" items="${images}">
                    <div class="thumb-item" onclick="changeMainImage('${pageContext.request.contextPath}/${img.imageUrl}', this)">
                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="${img.caption}">
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- CỘT PHẢI: THÔNG SỐ KỸ THUẬT & ĐẶT CỌC -->
        <div class="car-info-col">
            <div class="brand-badge-row">
                <span class="badge badge-brand">${car.brandName}</span>
                <span class="brand-country-tag">Xuất xứ: ${car.brandCountry}</span>
            </div>

            <h1 class="detail-car-title">${car.modelName}</h1>

            <!-- Đánh giá sao trung bình -->
            <div class="rating-summary">
                <div class="stars text-gold">
                    <c:forEach begin="1" end="5" var="s">
                        <c:choose>
                            <c:when test="${s <= averageRating}">★</c:when>
                            <c:otherwise>☆</c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <span class="rating-score font-bold">${averageRating} / 5.0</span>
                <span class="rating-count">(${reviews.size()} đánh giá của khách VIP)</span>
            </div>

            <!-- Bảng giá niêm yết & Số tiền đặt cọc -->
            <div class="detail-price-box">
                <div class="price-item">
                    <span class="price-label">Giá Niêm Yết Chính Hãng</span>
                    <div class="main-price">
                        <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                    </div>
                </div>
                <div class="price-item">
                    <span class="price-label">Mức Tiền Đặt Cọc (10%)</span>
                    <div class="deposit-price text-gold">
                        <fmt:formatNumber value="${car.price * 0.10}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                    </div>
                </div>
            </div>

            <!-- Thông số kỹ thuật F1 nổi bật -->
            <div class="specs-grid-box">
                <div class="spec-box">
                    <span class="spec-icon">⚡</span>
                    <div class="spec-data">
                        <span class="spec-num">${car.horsepower} HP</span>
                        <span class="spec-name">Công Suất Cực Đại</span>
                    </div>
                </div>
                <div class="spec-box">
                    <span class="spec-icon">⏱️</span>
                    <div class="spec-data">
                        <span class="spec-num">${car.acceleration0100} giây</span>
                        <span class="spec-name">Tăng Tốc (0-100 km/h)</span>
                    </div>
                </div>
                <div class="spec-box">
                    <span class="spec-icon">🚀</span>
                    <div class="spec-data">
                        <span class="spec-num">${car.topSpeed} km/h</span>
                        <span class="spec-name">Vận Tốc Tối Đa</span>
                    </div>
                </div>
                <div class="spec-box">
                    <span class="spec-icon">📅</span>
                    <div class="spec-data">
                        <span class="spec-num">${car.year}</span>
                        <span class="spec-name">Năm Sản Xuất</span>
                    </div>
                </div>
            </div>

            <!-- Chi tiết động cơ & Tồn kho -->
            <div class="extra-specs-list">
                <p>⚙️ <strong>Cấu hình Động cơ:</strong> ${car.engineSpec}</p>
                <p>📦 <strong>Tình trạng Showroom:</strong> 
                    <c:choose>
                        <c:when test="${car.stockQuantity > 0}">
                            <span class="text-success font-bold">Sẵn sàng bàn giao (${car.stockQuantity} xe trong kho)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger font-bold">Tạm hết hàng (Đặt riêng theo diện Bespoke)</span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <!-- FORM ĐẶT CỌC & TÙY CHỌN MÀU SẮC BESPOKE -->
            <form action="${pageContext.request.contextPath}/cart" method="POST" class="order-action-form">
                <!-- Mã bảo mật CSRF Token -->
                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                <input type="hidden" name="action" value="add" />
                <input type="hidden" name="carId" value="${car.carId}" />
                <input type="hidden" name="quantity" value="1" />

                <div class="form-group">
                    <label class="form-label">Chọn Màu Sơn Ngoại Thất (Bespoke):</label>
                    <select name="selectedColor" class="form-control">
                        <option value="Tuxedo Black (Đen Quyền Lực)">Tuxedo Black (Đen Quyền Lực)</option>
                        <option value="Rosso Corsa (Đỏ Trường Đua)">Rosso Corsa (Đỏ Trường Đua)</option>
                        <option value="French Racing Blue (Xanh Bugatti)">French Racing Blue (Xanh Bugatti)</option>
                        <option value="Giallo Modena (Vàng Hoàng Gia)">Giallo Modena (Vàng Hoàng Gia)</option>
                        <option value="Stealth Matte Grey (Xám Mờ Tàng Hình)">Stealth Matte Grey (Xám Mờ Tàng Hình)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Gói Trang Bị Đặc Biệt:</label>
                    <input type="text" name="customOptions" class="form-control" 
                           placeholder="vd: Gói Mâm Carbon nguyên khối, Khắc tên VIP lên bệ cửa...">
                </div>

                <div class="action-buttons-group">
                    <button type="submit" class="btn btn-gold btn-lg btn-block" ${car.stockQuantity <= 0 ? 'disabled' : ''}>
                        🛒 TIẾN HÀNH ĐẶT CỌC XE (10%)
                    </button>
                    <a href="${pageContext.request.contextPath}/test-drive?carId=${car.carId}" class="btn btn-outline btn-lg btn-block">
                        🏁 ĐẶT LỊCH LÁI THỬ TRƯỜNG ĐUA F1
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- MÔ TẢ CHI TIẾT & LỊCH SỬ DÒNG XE -->
    <div class="detail-description-section mt-5">
        <h2 class="section-title">KIỆT TÁC <span class="text-gold">THIẾT KẾ & DI SẢN</span></h2>
        <div class="description-content">
            <p>${car.description}</p>
        </div>
    </div>

    <!-- KHU VỰC ĐÁNH GIÁ CỦA KHÁCH HÀNG VIP -->
    <div id="reviews" class="reviews-section mt-5">
        <h2 class="section-title">ĐÁNH GIÁ TỪ <span class="text-gold">CHỦ NHÂN SIÊU XE</span></h2>

        <!-- Thông báo sau khi gửi review -->
        <c:if test="${param.reviewSuccess == '1'}">
            <div class="alert alert-success">Cảm ơn quý khách đã chia sẻ đánh giá trải nghiệm siêu xe!</div>
        </c:if>
        <c:if test="${param.reviewError == 'already_reviewed'}">
            <div class="alert alert-warning">Quý khách đã từng gửi đánh giá cho mẫu siêu xe này trước đó.</div>
        </c:if>

        <div class="reviews-layout">
            <!-- Cột trái: Form gửi đánh giá -->
            <div class="review-form-col">
                <div class="card p-4">
                    <h3 class="card-title">Gửi Đánh Giá Trải Nghiệm</h3>
                    
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <p class="text-muted">Vui lòng đăng nhập tài khoản VIP để gửi đánh giá cảm nhận về siêu xe.</p>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-gold btn-block">Đăng Nhập Ngay</a>
                        </c:when>
                        <c:when test="${hasReviewed}">
                            <p class="text-success font-bold">✓ Quý khách đã hoàn tất đánh giá cho mẫu xe này.</p>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/submit-review" method="POST">
                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                <input type="hidden" name="carId" value="${car.carId}" />

                                <div class="form-group">
                                    <label class="form-label">Điểm Đánh Giá:</label>
                                    <select name="rating" class="form-control">
                                        <option value="5">★★★★★ - Tuyệt tác hoàn hảo (5 Sao)</option>
                                        <option value="4">★★★★☆ - Trải nghiệm xuất sắc (4 Sao)</option>
                                        <option value="3">★★★☆☆ - Hài lòng (3 Sao)</option>
                                        <option value="2">★★☆☆☆ - Bình thường (2 Sao)</option>
                                        <option value="1">★☆☆☆☆ - Cần cải thiện (1 Sao)</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label class="form-label">Cảm Nhận Trải Nghiệm:</label>
                                    <textarea name="comment" class="form-control" rows="4" 
                                              placeholder="Chia sẻ cảm giác cầm lái, âm thanh động cơ và độ hoàn thiện nội thất..." required></textarea>
                                </div>

                                <button type="submit" class="btn btn-gold btn-block">GỬI ĐÁNH GIÁ VIP</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Cột phải: Danh sách các đánh giá đã có -->
            <div class="review-list-col">
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <div class="review-items">
                            <c:forEach var="rev" items="${reviews}">
                                <div class="review-card">
                                    <div class="review-header">
                                        <div class="reviewer-info">
                                            <span class="reviewer-avatar">👤</span>
                                            <div>
                                                <div class="reviewer-name font-bold">${rev.userFullName}</div>
                                                <div class="review-date text-muted font-sm">
                                                    <fmt:formatDate value="${rev.reviewDate}" pattern="dd/MM/yyyy HH:mm" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="review-stars text-gold">
                                            <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                                            <c:forEach begin="${rev.rating + 1}" end="5">☆</c:forEach>
                                        </div>
                                    </div>
                                    <p class="review-comment">${rev.comment}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state p-4">
                            <p class="text-muted">Chưa có đánh giá nào cho siêu xe này. Hãy là chủ nhân đầu tiên chia sẻ cảm nhận!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- KHỐI SIÊU XE CÙNG THƯƠNG HIỆU (RELATED CARS) -->
    <c:if test="${not empty relatedCars}">
        <div class="related-cars-section mt-5">
            <h2 class="section-title">SIÊU XE CÙNG <span class="text-gold">THƯƠNG HIỆU ${car.brandName}</span></h2>
            <div class="car-grid">
                <c:forEach var="rcar" items="${relatedCars}">
                    <div class="car-card">
                        <div class="car-thumb-wrap">
                            <img src="${pageContext.request.contextPath}/${rcar.thumbnailUrl}" alt="${rcar.modelName}" class="car-thumb">
                        </div>
                        <div class="car-body">
                            <h3 class="car-title">
                                <a href="${pageContext.request.contextPath}/car-detail?id=${rcar.carId}">${rcar.modelName}</a>
                            </h3>
                            <div class="car-price-row">
                                <div class="price-val">
                                    <fmt:formatNumber value="${rcar.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                            </div>
                            <div class="car-actions">
                                <a href="${pageContext.request.contextPath}/car-detail?id=${rcar.carId}" class="btn btn-outline btn-block">
                                    XEM CHI TIẾT
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>

<script>
    // Hàm chuyển đổi ảnh chính trong Gallery
    function changeMainImage(imgSrc, element) {
        document.getElementById('mainCarImage').src = imgSrc;
        const thumbs = document.querySelectorAll('.thumb-item');
        thumbs.forEach(t => t.classList.remove('active'));
        element.classList.add('active');
    }
</script>

<jsp:include page="../common/footer.jsp" />
