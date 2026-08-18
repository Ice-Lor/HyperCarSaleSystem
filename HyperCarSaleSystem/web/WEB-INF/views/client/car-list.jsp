<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Bộ Sưu Tập Siêu Xe - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <!-- Breadcrumb & Header -->
    <div class="mb-4">
        <h2 class="fw-bold" style="font-family: 'Cinzel', serif;">BỘ SƯU TẬP SIÊU XE TOÀN CẦU</h2>
        <p class="text-muted small">Tìm thấy <span class="text-gold fw-bold">${totalCars}</span> mẫu siêu phẩm sẵn sàng bàn giao</p>
    </div>

    <div class="row g-4">
        <!-- Sidebar Filter -->
        <div class="col-lg-3">
            <div class="card card-luxury p-3">
                <h5 class="fw-bold mb-3 text-gold border-bottom border-secondary pb-2">
                    <i class="bi bi-funnel-fill me-2"></i> BỘ LỌC TÌM KIẾM
                </h5>

                <form action="${pageContext.request.contextPath}/cars" method="GET">
                    <!-- Keyword -->
                    <div class="mb-3">
                        <label class="form-label text-muted small">Từ khóa</label>
                        <input type="text" name="keyword" class="form-control bg-dark border-secondary text-light form-control-sm" 
                               placeholder="Model xe..." value="${keyword}">
                    </div>

                    <!-- Brand -->
                    <div class="mb-3">
                        <label class="form-label text-muted small">Hãng sản xuất</label>
                        <select name="brandId" class="form-select bg-dark border-secondary text-light form-select-sm">
                            <option value="">-- Tất Cả Hãng --</option>
                            <c:forEach items="${brands}" var="b">
                                <option value="${b.brandId}" ${selectedBrandId == b.brandId ? 'selected' : ''}>
                                    ${b.brandName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Category -->
                    <div class="mb-3">
                        <label class="form-label text-muted small">Phân loại xe</label>
                        <select name="categoryId" class="form-select bg-dark border-secondary text-light form-select-sm">
                            <option value="">-- Tất Cả Phân Loại --</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.categoryId}" ${selectedCategoryId == cat.categoryId ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Price Range -->
                    <div class="mb-3">
                        <label class="form-label text-muted small">Khoảng giá ($)</label>
                        <div class="row g-2">
                            <div class="col-6">
                                <input type="number" name="minPrice" class="form-control bg-dark border-secondary text-light form-control-sm" 
                                       placeholder="Từ $" value="${minPrice}">
                            </div>
                            <div class="col-6">
                                <input type="number" name="maxPrice" class="form-control bg-dark border-secondary text-light form-control-sm" 
                                       placeholder="Đến $" value="${maxPrice}">
                            </div>
                        </div>
                    </div>

                    <!-- Sort -->
                    <div class="mb-4">
                        <label class="form-label text-muted small">Sắp xếp theo</label>
                        <select name="sortBy" class="form-select bg-dark border-secondary text-light form-select-sm">
                            <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                            <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                            <option value="hp_desc" ${sortBy == 'hp_desc' ? 'selected' : ''}>Công suất mạnh nhất</option>
                            <option value="speed_desc" ${sortBy == 'speed_desc' ? 'selected' : ''}>Tốc độ tối đa cao nhất</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-gold w-100 btn-sm py-2 mb-2">
                        <i class="bi bi-search me-1"></i> Áp Dụng Bộ Lọc
                    </button>
                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-secondary w-100 btn-sm text-light">
                        <i class="bi bi-arrow-counterclockwise me-1"></i> Xóa Bộ Lọc
                    </a>
                </form>
            </div>
        </div>

        <!-- Car Cards Grid -->
        <div class="col-lg-9">
            <c:if test="${empty cars}">
                <div class="card card-luxury p-5 text-center text-muted">
                    <i class="bi bi-search text-gold fs-1 mb-3"></i>
                    <h5>Không tìm thấy mẫu siêu xe phù hợp</h5>
                    <p class="small">Đại ca hãy thử điều chỉnh lại bộ lọc tìm kiếm hoặc từ khóa.</p>
                </div>
            </c:if>

            <div class="row g-4">
                <c:forEach items="${cars}" var="c">
                    <div class="col-md-6 col-xl-4">
                        <div class="card card-luxury h-100">
                            <div class="position-relative overflow-hidden" style="height: 200px;">
                                <img src="${c.thumbnailUrl}" class="w-100 h-100 object-fit-cover" alt="${c.modelName}">
                                <span class="position-absolute top-0 end-0 m-2 badge bg-gold text-dark fw-bold">
                                    ${c.brandName}
                                </span>
                            </div>
                            <div class="card-body p-3 d-flex flex-column">
                                <h6 class="fw-bold mb-1" style="font-family: 'Cinzel', serif;">${c.modelName}</h6>
                                <small class="text-muted mb-2">${c.categoryName} • ${c.year}</small>

                                <div class="row g-1 text-center small text-muted mb-3 py-1 rounded bg-surface" style="background-color: #222436; font-size: 0.75rem;">
                                    <div class="col-4 border-end border-secondary">${c.horsepower} HP</div>
                                    <div class="col-4 border-end border-secondary">${c.acceleration0100}s</div>
                                    <div class="col-4">${c.topSpeed} km/h</div>
                                </div>

                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <span class="fw-bold text-gold">
                                        <fmt:formatNumber value="${c.price}" type="currency" currencySymbol="$"/>
                                    </span>
                                    <a href="${pageContext.request.contextPath}/car-detail?id=${c.carId}" class="btn btn-outline-gold btn-sm px-2">
                                        Chi Tiết <i class="bi bi-chevron-right"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav class="mt-5 d-flex justify-content-center">
                    <ul class="pagination pagination-sm">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link ${currentPage == i ? 'bg-gold border-gold text-dark fw-bold' : 'bg-dark border-secondary text-light'}" 
                                   href="${pageContext.request.contextPath}/cars?page=${i}&keyword=${keyword}&brandId=${selectedBrandId}&categoryId=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sortBy=${sortBy}">
                                    ${i}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
