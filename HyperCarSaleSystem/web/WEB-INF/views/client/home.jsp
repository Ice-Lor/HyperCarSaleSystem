<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="HYPERCAR - Thế Giới Siêu Xe Đẳng Cấp Thượng Lưu" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<!-- Hero Section -->
<section class="hero-section text-white py-5">
    <div class="container py-5">
        <div class="row align-items-center">
            <div class="col-lg-8">
                <span class="badge bg-warning text-dark px-3 py-2 text-uppercase fw-bold mb-3 font-brand">
                    <i class="bi bi-fire me-1"></i> Biểu Tượng Tốc Độ & Quyền Lực
                </span>
                <h1 class="display-3 fw-bold font-brand mb-3">
                    CHINH PHỤC <span class="gold-gradient-text">ĐỈNH CAO</span> TỐC ĐỘ
                </h1>
                <p class="lead text-light text-opacity-75 mb-4" style="max-width: 600px;">
                    Sở hữu những kiệt tác cơ khí triệu đô giới hạn toàn cầu. Bugatti, Ferrari, Koenigsegg, Pagani sẵn sàng tại Showroom để bàn giao đến bộ sưu tập của đại ca.
                </p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-lg px-4">
                        <i class="bi bi-grid-fill me-2"></i> Khám Phá Bộ Sưu Tập
                    </a>
                    <a href="${pageContext.request.contextPath}/test-drive" class="btn btn-outline-gold btn-lg px-4">
                        <i class="bi bi-flag-fill me-2"></i> Đặt Lịch Lái Thử VIP
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Brand Carousel / Grid -->
<section class="py-5 border-bottom border-secondary border-opacity-25" style="background-color: #0d0d11;">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="font-brand text-uppercase gold-text fw-bold mb-0">
                <i class="bi bi-stars me-2"></i> Các Thương Hiệu Huyền Thoại
            </h4>
            <a href="${pageContext.request.contextPath}/cars" class="text-secondary small text-decoration-none">
                Xem tất cả <i class="bi bi-arrow-right"></i>
            </a>
        </div>
        <div class="row row-cols-2 row-cols-md-4 row-cols-lg-8 g-3 text-center">
            <c:forEach var="b" items="${brands}">
                <div class="col">
                    <a href="${pageContext.request.contextPath}/cars?brandId=${b.brandId}" class="hyper-card p-3 d-block text-decoration-none h-100">
                        <img src="${b.logoUrl}" alt="${b.brandName}" class="img-fluid rounded mb-2" style="max-height: 45px; object-fit: contain;">
                        <div class="text-white small fw-bold">${b.brandName}</div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Featured Hypercars Section -->
<section class="py-5">
    <div class="container">
        <div class="text-center mb-5">
            <h6 class="text-uppercase gold-text fw-bold font-brand">Tuyển Tập Giới Hạn</h6>
            <h2 class="font-brand fw-bold text-white">SIÊU XE NỔI BẬT NHẤT</h2>
            <div class="mx-auto" style="width: 80px; height: 3px; background: var(--gold-primary);"></div>
        </div>

        <div class="row g-4">
            <c:forEach var="car" items="${featuredCars}">
                <div class="col-lg-4 col-md-6">
                    <div class="hyper-card h-100 d-flex flex-column">
                        <!-- Car Thumbnail -->
                        <div class="card-img-wrapper">
                            <img src="${car.thumbnailUrl}" alt="${car.modelName}">
                            <span class="position-absolute top-0 end-0 m-3 badge bg-dark bg-opacity-75 text-warning border border-secondary">
                                ${car.brandName}
                            </span>
                        </div>

                        <!-- Content -->
                        <div class="p-4 d-flex flex-column flex-grow-1">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h5 class="fw-bold text-white mb-0 text-truncate" title="${car.modelName}">${car.modelName}</h5>
                                <span class="badge bg-secondary text-uppercase">${car.categoryName}</span>
                            </div>

                            <p class="text-secondary small text-truncate-2 mb-3" style="min-height: 40px;">
                                ${car.engineSpec}
                            </p>

                            <!-- Specs Grid -->
                            <div class="row g-2 mb-3">
                                <div class="col-4">
                                    <div class="spec-badge">
                                        <div class="spec-title">Công Suất</div>
                                        <div class="spec-value">${car.horsepower} <span class="fs-6">HP</span></div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="spec-badge">
                                        <div class="spec-title">0-100 km/h</div>
                                        <div class="spec-value">${car.acceleration0100}s</div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="spec-badge">
                                        <div class="spec-title">Tốc Độ Max</div>
                                        <div class="spec-value">${car.topSpeed} <span class="fs-6">km/h</span></div>
                                    </div>
                                </div>
                            </div>

                            <!-- Price & Actions -->
                            <div class="mt-auto pt-3 border-top border-secondary border-opacity-25 d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="text-secondary small d-block">Giá niêm yết:</span>
                                    <span class="fs-5 fw-bold text-warning font-brand">
                                        <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </span>
                                </div>
                                <div class="d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}" class="btn btn-outline-gold btn-sm">
                                        Chi Tiết
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="text-center mt-5">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-lg px-5">
                Xem Toàn Bộ ${featuredCars.size()}+ Siêu Xe Có Sẵn
            </a>
        </div>
    </div>
</section>

<!-- VIP Experience Highlights -->
<section class="py-5 border-top border-secondary border-opacity-25" style="background-color: #0c0c10;">
    <div class="container">
        <div class="row g-4 text-center">
            <div class="col-md-4">
                <div class="p-4 hyper-card h-100">
                    <div class="fs-1 text-warning mb-3"><i class="bi bi-shield-shaded"></i></div>
                    <h5 class="fw-bold text-white font-brand">Giao Dịch Bảo Mật Tuyệt Đối</h5>
                    <p class="text-secondary small">
                        Hợp đồng cọc và chuyển nhượng được xác thực bởi đội ngũ chuyên gia pháp lý và kiểm toán tài chính quốc tế.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="p-4 hyper-card h-100">
                    <div class="fs-1 text-warning mb-3"><i class="bi bi-flag-fill"></i></div>
                    <h5 class="fw-bold text-white font-brand">Trải Nghiệm Track Day VIP</h5>
                    <p class="text-secondary small">
                        Đặc quyền cầm lái siêu xe tại các trường đua F1 chuẩn quốc tế cùng huấn luyện viên đua xe chuyên nghiệp.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="p-4 hyper-card h-100">
                    <div class="fs-1 text-warning mb-3"><i class="bi bi-palette-fill"></i></div>
                    <h5 class="fw-bold text-white font-brand">Cá Nhân Hóa Bespoke</h5>
                    <p class="text-secondary small">
                        Tự do lựa chọn màu sơn độc bản, gói khí động học sợi carbon và các vật liệu nội thất da quý hiếm.
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
