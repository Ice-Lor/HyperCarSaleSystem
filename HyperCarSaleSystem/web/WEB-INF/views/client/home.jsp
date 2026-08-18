<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Trang Chủ - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<!-- Hero Banner -->
<section class="hero-banner text-center text-light d-flex align-items-center" style="min-height: 550px;">
    <div class="container py-5">
        <span class="badge bg-gold text-dark px-3 py-2 rounded-pill mb-3 fw-bold text-uppercase tracking-wider">
            Showroom Siêu Xe Thượng Lưu Độc Bản
        </span>
        <h1 class="display-3 fw-bolder mb-3" style="font-family: 'Cinzel', serif; letter-spacing: 2px;">
            ĐẲNG CẤP VƯỢT <span class="text-gold">GIỚI HẠN</span>
        </h1>
        <p class="lead text-muted mx-auto mb-4" style="max-width: 700px;">
            Bộ sưu tập các mẫu Megacar, Hypercar phiên bản giới hạn từ Bugatti, Koenigsegg, Pagani, Ferrari. Trải nghiệm đặt cọc và lái thử đường đua VIP.
        </p>
        <div class="d-flex justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-lg px-4 py-3">
                <i class="bi bi-grid-fill me-2"></i> Khám Phá Bộ Sưu Tập
            </a>
            <a href="${pageContext.request.contextPath}/test-drive" class="btn btn-outline-gold btn-lg px-4 py-3">
                <i class="bi bi-speedometer2 me-2"></i> Đặt Lịch Lái Thử F1
            </a>
        </div>
    </div>
</section>

<!-- Brand Logos -->
<section class="py-4 border-bottom border-secondary bg-surface" style="background-color: #12131b;">
    <div class="container">
        <div class="row align-items-center text-center justify-content-center g-4">
            <c:forEach items="${brands}" var="b">
                <div class="col-6 col-md-3 col-lg-2">
                    <a href="${pageContext.request.contextPath}/cars?brandId=${b.brandId}" class="text-decoration-none text-muted hover-gold d-block">
                        <img src="${b.logoUrl}" alt="${b.brandName}" style="height: 35px; filter: grayscale(100%) brightness(200%);" class="opacity-75 hover-opacity-100">
                        <div class="small mt-2 fw-semibold text-light">${b.brandName}</div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Featured Hypercars -->
<section class="py-5">
    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-end mb-4">
            <div>
                <span class="text-gold fw-bold small text-uppercase tracking-wider">Bộ Sưu Tập Tiêu Biểu</span>
                <h2 class="fw-bold mt-1" style="font-family: 'Cinzel', serif;">SIÊU XE NỔI BẬT</h2>
            </div>
            <a href="${pageContext.request.contextPath}/cars" class="text-gold text-decoration-none fw-bold small">
                Xem Tất Cả <i class="bi bi-arrow-right"></i>
            </a>
        </div>

        <div class="row g-4">
            <c:forEach items="${featuredCars}" var="c">
                <div class="col-md-6 col-lg-4">
                    <div class="card card-luxury h-100 position-relative">
                        <div class="position-relative overflow-hidden" style="height: 240px;">
                            <img src="${c.thumbnailUrl}" class="w-100 h-100 object-fit-cover" alt="${c.modelName}">
                            <span class="position-absolute top-0 end-0 m-3 badge bg-gold text-dark fw-bold">
                                ${c.brandName}
                            </span>
                        </div>
                        <div class="card-body p-4 d-flex flex-column">
                            <h5 class="fw-bold mb-1" style="font-family: 'Cinzel', serif;">${c.modelName}</h5>
                            <div class="text-muted small mb-3">${c.categoryName} • Xuất xứ: ${c.brandCountry}</div>
                            
                            <!-- Specs Badges -->
                            <div class="row g-2 text-center small text-muted mb-3 py-2 rounded bg-surface" style="background-color: #222436;">
                                <div class="col-4 border-end border-secondary">
                                    <i class="bi bi-lightning-charge-fill text-gold"></i>
                                    <div>${c.horsepower} HP</div>
                                </div>
                                <div class="col-4 border-end border-secondary">
                                    <i class="bi bi-stopwatch text-gold"></i>
                                    <div>${c.acceleration0100}s (0-100)</div>
                                </div>
                                <div class="col-4">
                                    <i class="bi bi-speedometer text-gold"></i>
                                    <div>${c.topSpeed} km/h</div>
                                </div>
                            </div>

                            <div class="mt-auto d-flex justify-content-between align-items-center pt-2">
                                <div>
                                    <small class="text-muted d-block">Giá niêm yết</small>
                                    <span class="fs-5 fw-bold text-gold">
                                        <fmt:formatNumber value="${c.price}" type="currency" currencySymbol="$"/>
                                    </span>
                                </div>
                                <a href="${pageContext.request.contextPath}/car-detail?id=${c.carId}" class="btn btn-outline-gold btn-sm px-3">
                                    Chi Tiết <i class="bi bi-chevron-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- VIP Concierge Banner -->
<section class="py-5 bg-card border-top border-bottom border-secondary my-4">
    <div class="container py-3">
        <div class="row align-items-center g-4">
            <div class="col-lg-7">
                <span class="badge bg-gold text-dark mb-2">Đặc Quyền Thượng Lưu</span>
                <h3 class="fw-bold mb-3" style="font-family: 'Cinzel', serif;">Trải Nghiệm Lái Thử Trên Đường Đua Quốc Tế</h3>
                <p class="text-muted mb-0">
                    Mỗi khách hàng đặt lịch sẽ được cung cấp chuyên cơ đưa đón, huấn luyện viên trường đua F1 và phục vụ tiệc trà thượng hạng.
                </p>
            </div>
            <div class="col-lg-5 text-lg-end">
                <a href="${pageContext.request.contextPath}/test-drive" class="btn btn-gold btn-lg px-4">
                    <i class="bi bi-calendar-event me-2"></i> Đăng Ký Lịch Trải Nghiệm
                </a>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
