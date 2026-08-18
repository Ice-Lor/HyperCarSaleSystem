<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${car != null ? 'Chỉnh Sửa Siêu Xe' : 'Thêm Siêu Xe Mới'} - Admin"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="cars"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">
                    ${car != null ? 'CHỈNH SỬA THÔNG TIN SIÊU XE' : 'THÊM SIÊU XE MỚI VÀO SHOWROOM'}
                </h3>
                <a href="${pageContext.request.contextPath}/admin/cars" class="btn btn-outline-secondary text-light btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Quay Lại Danh Sách
                </a>
            </div>

            <div class="card card-luxury p-4">
                <form action="${pageContext.request.contextPath}/admin/cars" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="save">
                    <c:if test="${car != null}">
                        <input type="hidden" name="carId" value="${car.carId}">
                    </c:if>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-muted small">Tên Model Xe *</label>
                            <input type="text" name="modelName" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.modelName : ''}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Hãng Sản Xuất *</label>
                            <select name="brandId" class="form-select bg-dark border-secondary text-light" required>
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.brandId}" ${car != null && car.brandId == b.brandId ? 'selected' : ''}>
                                        ${b.brandName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Phân Loại *</label>
                            <select name="categoryId" class="form-select bg-dark border-secondary text-light" required>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.categoryId}" ${car != null && car.categoryId == cat.categoryId ? 'selected' : ''}>
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label class="form-label text-muted small">Giá Niêm Yết ($) *</label>
                            <input type="number" step="0.01" name="price" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.price : ''}" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label text-muted small">Tỷ Lệ Đặt Cọc (%) *</label>
                            <input type="number" step="0.1" name="depositRate" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.depositRate : '10.0'}" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label text-muted small">Số Lượng Tồn Kho *</label>
                            <input type="number" name="stockQuantity" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.stockQuantity : '1'}" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Năm Sản Xuất *</label>
                            <input type="number" name="year" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.year : '2026'}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Công Suất (HP) *</label>
                            <input type="number" name="horsepower" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.horsepower : ''}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Tăng Tốc 0-100km/h (s) *</label>
                            <input type="number" step="0.01" name="acceleration0100" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.acceleration0100 : ''}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-muted small">Tốc Độ Tối Đa (km/h) *</label>
                            <input type="number" name="topSpeed" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.topSpeed : ''}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-muted small">URL Hình Ảnh Thumbnail *</label>
                        <input type="url" name="thumbnailUrl" class="form-control bg-dark border-secondary text-light" 
                               value="${car != null ? car.thumbnailUrl : ''}" placeholder="https://..." required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-muted small">Tùy Chọn Màu Sơn (Cách nhau bởi dấu phẩy)</label>
                            <input type="text" name="colorOptions" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.colorOptions : 'Nocturne Black, French Racing Blue, Bianco Monocerus'}">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted small">Thông Số Động Cơ</label>
                            <input type="text" name="engineSpec" class="form-control bg-dark border-secondary text-light" 
                                   value="${car != null ? car.engineSpec : '8.0L Quad-Turbo W16'}">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-muted small">Mô Tả Chi Tiết</label>
                        <textarea name="description" class="form-control bg-dark border-secondary text-light" rows="3">${car != null ? car.description : ''}</textarea>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-muted small">Trạng Thái Hiển Thị</label>
                        <select name="status" class="form-select bg-dark border-secondary text-light">
                            <option value="1" ${car != null && car.status == 1 ? 'selected' : ''}>Hiển thị trên Showroom</option>
                            <option value="0" ${car != null && car.status == 0 ? 'selected' : ''}>Tạm ẩn / Đang bảo dưỡng</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-gold px-4 py-2">
                        <i class="bi bi-save me-1"></i> Lưu Thông Tin Siêu Xe
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
