<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Showroom Siêu Xe Độc Bản Hàng Đầu" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<!-- 1. HERO BANNER THƯỢNG LƯU -->
<section class="hero-section">
    <div class="hero-overlay"></div>
    <div class="container hero-content">
        <span class="hero-tag">THE PINNACLE OF AUTOMOTIVE LUXURY</span>
        <h1 class="hero-title">HỘI TỤ KIỆT TÁC <span class="text-gold">SIÊU XE ĐỘC BẢN</span></h1>
        <p class="hero-desc">
            Trải nghiệm cảm giác làm chủ những cỗ máy tốc độ đắt giá nhất hành tinh từ Bugatti, Ferrari, Lamborghini, Koenigsegg, Pagani, McLaren, Porsche và Rimac.
        </p>
        <div class="hero-cta">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-lg">KHÁM PHÁ BỘ SƯU TẬP</a>
            <a href="${pageContext.request.contextPath}/test-drive" class="btn btn-outline btn-lg">ĐẶT LỊCH LÁI THỬ F1</a>
        </div>
    </div>
</section>

<!-- 2. THƯƠNG HIỆU ĐỘC QUYỀN (8 BRANDS SHOWCASE) -->
<section class="section brands-section">
    <div class="container">
        <div class="section-header">
            <h2 class="section-title">8 ĐẠI DIỆN <span class="text-gold">DANH TIẾNG TOÀN CẦU</span></h2>
            <p class="section-subtitle">Phân phối chính thức các dòng siêu xe và megacar độc bản</p>
        </div>

        <div class="brands-grid">
            <c:forEach var="brand" items="${brands}">
                <a href="${pageContext.request.contextPath}/cars?brandId=${brand.brandId}" class="brand-card">
                    <div class="brand-logo-wrap">
                        <img src="${pageContext.request.contextPath}/${brand.logoUrl}" alt="${brand.brandName}" class="brand-logo-img">
                    </div>
                    <h3 class="brand-name">${brand.brandName}</h3>
                    <span class="brand-country">📍 ${brand.country}</span>
                </a>
            </c:forEach>
        </div>
    </div>
</section>

<!-- 3. TOP SIÊU XE NỔI BẬT NHẤT (FEATURED HYPERCARS) -->
<section class="section featured-section bg-darker">
    <div class="container">
        <div class="section-header">
            <h2 class="section-title">KIỆT TÁC <span class="text-gold">NỔI BẬT NHẤT</span></h2>
            <p class="section-subtitle">Những mẫu xe có mã lực khủng nhất và giá trị sưu tầm vô giá</p>
        </div>

        <div class="car-grid">
            <c:forEach var="car" items="${featuredCars}">
                <div class="car-card">
                    <!-- Ảnh đại diện siêu xe -->
                    <div class="car-thumb-wrap">
                        <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" alt="${car.modelName}" class="car-thumb">
                        <span class="badge badge-brand">${car.brandName}</span>
                    </div>

                    <!-- Nội dung thông tin xe -->
                    <div class="car-body">
                        <h3 class="car-title">
                            <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}">${car.modelName}</a>
                        </h3>
                        
                        <!-- Thông số hiệu năng F1 -->
                        <div class="car-specs">
                            <div class="spec-item">
                                <span class="spec-label">Mã Lực</span>
                                <span class="spec-val">${car.horsepower} HP</span>
                            </div>
                            <div class="spec-item">
                                <span class="spec-label">0-100 km/h</span>
                                <span class="spec-val">${car.acceleration0100}s</span>
                            </div>
                            <div class="spec-item">
                                <span class="spec-label">Tốc Độ Tối Đa</span>
                                <span class="spec-val">${car.topSpeed} km/h</span>
                            </div>
                        </div>

                        <div class="car-divider"></div>

                        <!-- Giá niêm yết và Tiền đặt cọc -->
                        <div class="car-price-row">
                            <div>
                                <span class="price-label">Giá Niêm Yết</span>
                                <div class="price-val">
                                    <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                            </div>
                            <div class="text-right">
                                <span class="price-label">Tiền Cọc (10%)</span>
                                <div class="deposit-val">
                                    <fmt:formatNumber value="${car.price * 0.10}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                            </div>
                        </div>

                        <!-- Nút thao tác -->
                        <div class="car-actions">
                            <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}" class="btn btn-outline btn-block">
                                XEM CHI TIẾT
                            </a>
                            <button type="button" class="btn btn-gold btn-block btn-add-cart" data-car-id="${car.carId}">
                                + ĐẶT CỌC XE
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="text-center mt-5">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline btn-lg">
                XEM TOÀN BỘ BỘ SƯU TẬP XE →
            </a>
        </div>
    </div>
</section>

<!-- 4. SIÊU XE MỚI VỀ SHOWROOM (LATEST ARRIVALS) -->
<section class="section latest-section">
    <div class="container">
        <div class="section-header">
            <h2 class="section-title">SIÊU XE <span class="text-gold">MỚI VỀ SHOWROOM</span></h2>
            <p class="section-subtitle">Vừa hoàn tất thủ tục nhập khẩu và kiểm định chất lượng nghiêm ngặt</p>
        </div>

        <div class="car-grid">
            <c:forEach var="car" items="${latestCars}">
                <div class="car-card">
                    <div class="car-thumb-wrap">
                        <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" alt="${car.modelName}" class="car-thumb">
                        <span class="badge badge-gold">NEW ARRIVAL</span>
                    </div>

                    <div class="car-body">
                        <h3 class="car-title">
                            <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}">${car.modelName}</a>
                        </h3>
                        
                        <div class="car-specs">
                            <div class="spec-item">
                                <span class="spec-label">Hãng</span>
                                <span class="spec-val">${car.brandName}</span>
                            </div>
                            <div class="spec-item">
                                <span class="spec-label">Năm SX</span>
                                <span class="spec-val">${car.year}</span>
                            </div>
                            <div class="spec-item">
                                <span class="spec-label">Mã Lực</span>
                                <span class="spec-val">${car.horsepower} HP</span>
                            </div>
                        </div>

                        <div class="car-divider"></div>

                        <div class="car-price-row">
                            <div>
                                <span class="price-label">Giá Bán</span>
                                <div class="price-val">
                                    <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                            </div>
                            <div class="text-right">
                                <span class="price-label">Tồn Kho</span>
                                <span class="badge badge-stock">${car.stockQuantity} xe</span>
                            </div>
                        </div>

                        <div class="car-actions">
                            <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}" class="btn btn-outline btn-block">
                                CHI TIẾT & BẢNG GIÁ
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- 5. 4 ĐẶC QUYỀN KHÁCH HÀNG THƯỢNG LƯU (VIP CONCIERGE) -->
<section class="section concierge-section bg-darker">
    <div class="container">
        <div class="section-header">
            <h2 class="section-title">ĐẶC QUYỀN <span class="text-gold">DÀNH CHO KHÁCH HÀNG</span></h2>
            <p class="section-subtitle">Cam kết bảo mật tuyệt đối và dịch vụ chăm sóc chuẩn 6 sao</p>
        </div>

        <div class="concierge-grid">
            <div class="concierge-card">
                <div class="concierge-icon">🔒</div>
                <h3 class="concierge-title">Bảo Mật Danh Tính 100%</h3>
                <p class="concierge-desc">Mọi giao dịch và thông tin sở hữu siêu xe đều được bảo mật tuyệt đối theo tiêu chuẩn cao nhất.</p>
            </div>
            <div class="concierge-card">
                <div class="concierge-icon">🚛</div>
                <h3 class="concierge-title">Bàn Giao Xe Thùng Kín</h3>
                <p class="concierge-desc">Vận chuyển xe bằng chuyên cơ mặt đất bọc nhung khép kín đến tận sân nhà hoặc du thuyền của quý khách.</p>
            </div>
            <div class="concierge-card">
                <div class="concierge-icon">🏁</div>
                <h3 class="concierge-title">Trải Nghiệm Trường Đua F1</h3>
                <p class="concierge-desc">Độc quyền đặt lịch lái thử giới hạn tại các đường đua tiêu chuẩn FIA quốc tế cùng tay đua chuyên nghiệp.</p>
            </div>
            <div class="concierge-card">
                <div class="concierge-icon">⚙️</div>
                <h3 class="concierge-title">Kỹ Sư Trưởng Chính Hãng</h3>
                <p class="concierge-desc">Đội ngũ chuyên gia kỹ thuật được đào tạo trực tiếp từ trụ sở Ý, Đức, Pháp và Anh Quốc bảo dưỡng định kỳ.</p>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />
