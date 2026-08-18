<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Bộ Sưu Tập Siêu Xe - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <!-- Breadcrumb & Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="font-brand fw-bold text-white mb-1">BỘ SƯU TẬP SIÊU XE</h2>
            <p class="text-secondary small mb-0">Tìm thấy <span class="text-warning fw-bold">${totalCars}</span> siêu xe thỏa mãn tiêu chí</p>
        </div>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-secondary text-decoration-none">Trang chủ</a></li>
                <li class="breadcrumb-item active text-warning" aria-current="page">Bộ sưu tập</li>
            </ol>
        </nav>
    </div>

    <div class="row g-4">
        <!-- Filter Sidebar -->
        <div class="col-lg-3">
            <div class="hyper-card p-4 sticky-top" style="top: 90px;">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    <i class="bi bi-funnel-fill me-2"></i> BỘ LỌC TÌM KIẾM
                </h5>

                <form action="${pageContext.request.contextPath}/cars" method="GET">
                    <!-- Keyword -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">TỪ KHÓA / MODEL</label>
                        <input type="text" name="keyword" class="form-control form-control-dark form-control-sm" 
                               placeholder="VD: Chiron, SF90..." value="${keyword}">
                    </div>

                    <!-- Brand -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">THƯƠNG HIỆU</label>
                        <select name="brandId" class="form-select form-select-dark form-select-sm">
                            <option value="">-- Tất cả hãng --</option>
                            <c:forEach var="b" items="${brands}">
                                <option value="${b.brandId}" ${selectedBrandId == b.brandId ? 'selected' : ''}>
                                    ${b.brandName} (${b.country})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Category -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">PHÂN LOẠI DÒNG XE</label>
                        <select name="categoryId" class="form-select form-select-dark form-select-sm">
                            <option value="">-- Tất cả phân loại --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}" ${selectedCategoryId == cat.categoryId ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Min HP -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">CÔNG SUẤT TỐI THIỂU</label>
                        <select name="minHp" class="form-select form-select-dark form-select-sm">
                            <option value="">-- Mọi mức công suất --</option>
                            <option value="800" ${minHp == 800 ? 'selected' : ''}>Từ 800 HP trở lên</option>
                            <option value="1000" ${minHp == 1000 ? 'selected' : ''}>Từ 1,000 HP trở lên</option>
                            <option value="1500" ${minHp == 1500 ? 'selected' : ''}>Từ 1,500 HP trở lên (Megacar)</option>
                        </select>
                    </div>

                    <!-- Sort By -->
                    <div class="mb-4">
                        <label class="form-label text-secondary small fw-bold">SẮP XẾP THEO</label>
                        <select name="sortBy" class="form-select form-select-dark form-select-sm">
                            <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                            <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                            <option value="hp_desc" ${sortBy == 'hp_desc' ? 'selected' : ''}>Mã lực khủng nhất</option>
                            <option value="speed_desc" ${sortBy == 'speed_desc' ? 'selected' : ''}>Tốc độ tối đa</option>
                        </select>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-gold btn-sm">
                            <i class="bi bi-search me-1"></i> Áp Dụng Bộ Lọc
                        </button>
                        <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-secondary btn-sm">
                            <i class="bi bi-arrow-counterclockwise me-1"></i> Xóa Bộ Lọc
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Car Grid -->
        <div class="col-lg-9">
            <c:choose>
                <c:when test="${empty carList}">
                    <div class="hyper-card p-5 text-center my-5">
                        <i class="bi bi-car-front fs-1 text-secondary mb-3 d-block"></i>
                        <h4 class="text-white">Không tìm thấy siêu xe phù hợp</h4>
                        <p class="text-secondary small">Vui lòng thử điều chỉnh lại bộ lọc giá hoặc thương hiệu.</p>
                        <a href="${pageContext.request.contextPath}/cars" class="btn btn-gold btn-sm mt-2">Xem Tất Cả Xe</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-4">
                        <c:forEach var="car" items="${carList}">
                            <div class="col-md-6 col-lg-6">
                                <div class="hyper-card h-100 d-flex flex-column">
                                    <div class="card-img-wrapper">
                                        <img src="${car.thumbnailUrl}" alt="${car.modelName}">
                                        <span class="position-absolute top-0 end-0 m-3 badge bg-dark bg-opacity-75 text-warning border border-secondary">
                                            ${car.brandName}
                                        </span>
                                    </div>
                                    <div class="p-4 d-flex flex-column flex-grow-1">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <h5 class="fw-bold text-white mb-0 text-truncate" title="${car.modelName}">${car.modelName}</h5>
                                            <span class="badge bg-secondary text-uppercase">${car.categoryName}</span>
                                        </div>

                                        <p class="text-secondary small text-truncate-2 mb-3">
                                            ${car.engineSpec}
                                        </p>

                                        <!-- Specs -->
                                        <div class="row g-2 mb-3">
                                            <div class="col-4">
                                                <div class="spec-badge">
                                                    <div class="spec-title">Mã Lực</div>
                                                    <div class="spec-value">${car.horsepower}</div>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="spec-badge">
                                                    <div class="spec-title">0-100</div>
                                                    <div class="spec-value">${car.acceleration0100}s</div>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="spec-badge">
                                                    <div class="spec-title">Tốc Độ</div>
                                                    <div class="spec-value">${car.topSpeed}</div>
                                                </div>
                                            </div>
                                        </div>

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

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <nav class="mt-5" aria-label="Page navigation">
                            <ul class="pagination justify-content-center">
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                                        <a class="page-link ${p == currentPage ? 'bg-warning text-dark border-warning' : 'bg-dark text-white border-secondary'}" 
                                           href="${pageContext.request.contextPath}/cars?page=${p}&keyword=${keyword}&brandId=${selectedBrandId}&categoryId=${selectedCategoryId}&minHp=${minHp}&sortBy=${sortBy}">
                                            ${p}
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
