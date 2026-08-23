<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Bộ Sưu Tập Siêu Xe Độc Bản" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">BỘ SƯU TẬP <span class="text-gold">SIÊU XE ĐỘC BẢN</span></h1>
        <p class="page-subtitle">Khám phá và đặt cọc những kiệt tác cơ khí đỉnh cao từ các thương hiệu huyền thoại thế giới</p>
    </div>
</div>

<div class="container section">
    <div class="car-list-layout">
        <!-- BỘ LỌC TÌM KIẾM SIDEBAR QUA MAINCONTROLLER -->
        <aside class="filter-sidebar">
            <div class="filter-card">
                <h3 class="filter-title">🔍 BỘ LỌC TÌM KIẾM</h3>
                
                <form action="${pageContext.request.contextPath}/MainController" method="GET" id="filterForm">
                    <input type="hidden" name="action" value="Cars" />

                    <!-- Từ khóa -->
                    <div class="form-group">
                        <label class="form-label">Từ Khóa</label>
                        <input type="text" name="keyword" class="form-control form-control-sm" 
                               placeholder="vd: Chiron, Revuelto..." value="${selectedKeyword}">
                    </div>

                    <!-- Hãng sản xuất -->
                    <div class="form-group">
                        <label class="form-label">Thương Hiệu</label>
                        <select name="brandId" class="form-control form-control-sm">
                            <option value="">-- Tất Cả Các Hãng --</option>
                            <c:forEach var="brand" items="${brands}">
                                <option value="${brand.brandId}" ${selectedBrandId == brand.brandId ? 'selected' : ''}>
                                    ${brand.brandName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Phân khúc siêu xe -->
                    <div class="form-group">
                        <label class="form-label">Phân Khúc Xe</label>
                        <select name="categoryId" class="form-control form-control-sm">
                            <option value="">-- Tất Cả Phân Khúc --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}" ${selectedCategoryId == cat.categoryId ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Khoảng giá USD -->
                    <div class="form-group">
                        <label class="form-label">Khoảng Giá (USD)</label>
                        <div class="form-row">
                            <div class="col-6">
                                <input type="number" name="minPrice" class="form-control form-control-sm" 
                                       placeholder="Từ $" value="${selectedMinPrice}">
                            </div>
                            <div class="col-6">
                                <input type="number" name="maxPrice" class="form-control form-control-sm" 
                                       placeholder="Đến $" value="${selectedMaxPrice}">
                            </div>
                        </div>
                    </div>

                    <!-- Sắp xếp theo -->
                    <div class="form-group">
                        <label class="form-label">Sắp Xếp Theo</label>
                        <select name="sortBy" class="form-control form-control-sm">
                            <option value="newest" ${selectedSortBy == 'newest' ? 'selected' : ''}>Mới Nhất</option>
                            <option value="price_asc" ${selectedSortBy == 'price_asc' ? 'selected' : ''}>Giá: Thấp Đến Cao</option>
                            <option value="price_desc" ${selectedSortBy == 'price_desc' ? 'selected' : ''}>Giá: Cao Đến Thấp</option>
                            <option value="hp_desc" ${selectedSortBy == 'hp_desc' ? 'selected' : ''}>Mã Lực: Khủng Nhất</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-gold btn-block">ÁP DỤNG BỘ LỌC</button>
                    <a href="${pageContext.request.contextPath}/MainController?action=Cars" class="btn btn-outline btn-block mt-2">XÓA BỘ LỌC</a>
                </form>
            </div>
        </aside>

        <!-- DANH SÁCH SIÊU XE & PHÂN TRANG -->
        <main class="car-list-main">
            <!-- Thanh trạng thái kết quả -->
            <div class="results-header">
                <div class="results-count">
                    Tìm thấy <span class="text-gold font-bold">${totalCars}</span> kiệt tác siêu xe
                </div>
            </div>

            <!-- Lưới hiển thị danh sách xe -->
            <c:choose>
                <c:when test="${not empty cars}">
                    <div class="car-grid">
                        <c:forEach var="car" items="${cars}">
                            <div class="car-card">
                                <div class="car-thumb-wrap">
                                    <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" alt="${car.modelName}" class="car-thumb">
                                    <span class="badge badge-brand">${car.brandName}</span>
                                </div>

                                <div class="car-body">
                                    <h3 class="car-title">
                                        <a href="${pageContext.request.contextPath}/MainController?action=CarDetail&id=${car.carId}">${car.modelName}</a>
                                    </h3>

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
                                            <span class="spec-label">Tối Đa</span>
                                            <span class="spec-val">${car.topSpeed} km/h</span>
                                        </div>
                                    </div>

                                    <div class="car-divider"></div>

                                    <div class="car-price-row">
                                        <div>
                                            <span class="price-label">Giá Niêm Yết</span>
                                            <div class="price-val">
                                                <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                            </div>
                                        </div>
                                        <div class="text-right">
                                            <span class="price-label">Cọc (10%)</span>
                                            <div class="deposit-val">
                                                <fmt:formatNumber value="${car.price * 0.10}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="car-actions">
                                        <a href="${pageContext.request.contextPath}/MainController?action=CarDetail&id=${car.carId}" class="btn btn-outline btn-block">
                                            XEM CHI TIẾT
                                        </a>
                                        <form action="${pageContext.request.contextPath}/MainController" method="POST" style="margin: 0; width: 100%;">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                            <input type="hidden" name="action" value="AddToCart" />
                                            <input type="hidden" name="carId" value="${car.carId}" />
                                            <input type="hidden" name="quantity" value="1" />
                                            <button type="submit" class="btn btn-gold btn-block btn-add-cart" data-car-id="${car.carId}">
                                                + ĐẶT CỌC XE
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- THANH ĐIỀU HƯỚNG PHÂN TRANG (PAGINATION QUA MAINCONTROLLER) -->
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <!-- Nút Trang trước -->
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/MainController?action=Cars&page=${currentPage - 1}&keyword=${selectedKeyword}&brandId=${selectedBrandId}&categoryId=${selectedCategoryId}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&sortBy=${selectedSortBy}" 
                                   class="page-link page-prev">« Trước</a>
                            </c:if>

                            <!-- Các số trang -->
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="${pageContext.request.contextPath}/MainController?action=Cars&page=${i}&keyword=${selectedKeyword}&brandId=${selectedBrandId}&categoryId=${selectedCategoryId}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&sortBy=${selectedSortBy}" 
                                   class="page-link ${currentPage == i ? 'active' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>

                            <!-- Nút Trang sau -->
                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/MainController?action=Cars&page=${currentPage + 1}&keyword=${selectedKeyword}&brandId=${selectedBrandId}&categoryId=${selectedCategoryId}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&sortBy=${selectedSortBy}" 
                                   class="page-link page-next">Sau »</a>
                            </c:if>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">🏎️</div>
                        <h3>Không tìm thấy siêu xe phù hợp</h3>
                        <p>Quý khách vui lòng thử tìm kiếm với các tiêu chí lọc khác hoặc liên hệ Concierge để đặt hàng riêng.</p>
                        <a href="${pageContext.request.contextPath}/MainController?action=Cars" class="btn btn-gold mt-3">XEM TOÀN BỘ SHOWROOM</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
