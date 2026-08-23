<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${isEdit ? 'Chỉnh Sửa Siêu Xe' : 'Thêm Mới Siêu Xe'}" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="cars" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">${isEdit ? 'CHỈNH SỬA THÔNG TIN SIÊU XE' : 'THÊM MỚI SIÊU XE SHOWROOM'}</h1>
            <div class="admin-topbar-actions">
                <a href="${pageContext.request.contextPath}/admin/cars" class="btn btn-outline btn-sm">
                    ← Quay lại danh sách
                </a>
            </div>
        </div>

        <div class="card p-4 mt-3">
            <form action="${pageContext.request.contextPath}/admin/cars" method="POST">
                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                <input type="hidden" name="action" value="${isEdit ? 'edit' : 'insert'}" />
                <c:if test="${isEdit}">
                    <input type="hidden" name="carId" value="${car.carId}" />
                </c:if>

                <div class="form-row">
                    <div class="form-group col-6">
                        <label class="form-label">Tên Kiệt Tác Siêu Xe <span class="text-danger">*</span></label>
                        <input type="text" name="modelName" class="form-control" 
                               placeholder="vd: Bugatti Chiron Pur Sport" value="${car.modelName}" required>
                    </div>
                    <div class="form-group col-3">
                        <label class="form-label">Thương Hiệu <span class="text-danger">*</span></label>
                        <select name="brandId" class="form-control" required>
                            <c:forEach var="b" items="${brands}">
                                <option value="${b.brandId}" ${car.brandId == b.brandId ? 'selected' : ''}>
                                    ${b.brandName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group col-3">
                        <label class="form-label">Phân Khúc Xe <span class="text-danger">*</span></label>
                        <select name="categoryId" class="form-control" required>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}" ${car.categoryId == cat.categoryId ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group col-4">
                        <label class="form-label">Giá Niêm Yết (USD) <span class="text-danger">*</span></label>
                        <input type="number" name="price" class="form-control" step="1000" 
                               placeholder="vd: 3800000" value="${car.price}" required>
                    </div>
                    <div class="form-group col-4">
                        <label class="form-label">Tỷ Lệ Đặt Cọc (%) <span class="text-danger">*</span></label>
                        <input type="number" name="depositRate" class="form-control" step="0.1" 
                               placeholder="vd: 10.0" value="${empty car.depositRate ? 10.0 : car.depositRate}" required>
                    </div>
                    <div class="form-group col-4">
                        <label class="form-label">Năm Sản Xuất <span class="text-danger">*</span></label>
                        <input type="number" name="year" class="form-control" 
                               placeholder="vd: 2024" value="${empty car.year ? 2024 : car.year}" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group col-3">
                        <label class="form-label">Công Suất (HP) <span class="text-danger">*</span></label>
                        <input type="number" name="horsepower" class="form-control" 
                               placeholder="vd: 1500" value="${car.horsepower}" required>
                    </div>
                    <div class="form-group col-3">
                        <label class="form-label">Tăng Tốc 0-100 km/h (giây) <span class="text-danger">*</span></label>
                        <input type="number" name="acceleration" class="form-control" step="0.1" 
                               placeholder="vd: 2.3" value="${car.acceleration0100}" required>
                    </div>
                    <div class="form-group col-3">
                        <label class="form-label">Tốc Độ Tối Đa (km/h) <span class="text-danger">*</span></label>
                        <input type="number" name="topSpeed" class="form-control" 
                               placeholder="vd: 350" value="${car.topSpeed}" required>
                    </div>
                    <div class="form-group col-3">
                        <label class="form-label">Số Lượng Tồn Kho <span class="text-danger">*</span></label>
                        <input type="number" name="stockQuantity" class="form-control" min="0" 
                               placeholder="vd: 1" value="${empty car.stockQuantity ? 1 : car.stockQuantity}" required>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Đường Dẫn Ảnh Thumbnail (Local Path) <span class="text-danger">*</span></label>
                    <input type="text" name="thumbnailUrl" class="form-control" 
                           placeholder="vd: assets/images/cars/bugatti-chiron.jpg" value="${car.thumbnailUrl}" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Cấu Hình Động Cơ Chi Tiết:</label>
                    <input type="text" name="engineSpec" class="form-control" 
                           placeholder="vd: 8.0L Quad-Turbo W16, 64 Valves, 1600 Nm Torque" value="${car.engineSpec}">
                </div>

                <div class="form-group">
                    <label class="form-label">Các Tùy Chọn Màu Sơn Bespoke (Phân cách bằng dấu phẩy):</label>
                    <input type="text" name="colorOptions" class="form-control" 
                           placeholder="vd: Tuxedo Black, Rosso Corsa, French Racing Blue" value="${car.colorOptions}">
                </div>

                <div class="form-group">
                    <label class="form-label">Mô Tả & Di Sản Lịch Sử:</label>
                    <textarea name="description" class="form-control" rows="4" 
                              placeholder="Mô tả thiết kế khí động học, sợi carbon nguyên khối...">${car.description}</textarea>
                </div>

                <div class="form-group">
                    <label class="form-label">Trạng Thái Mở Bán:</label>
                    <select name="status" class="form-control">
                        <option value="1" ${car.status == 1 || empty car ? 'selected' : ''}>1 - Đang Mở Bán Trực Tuyến</option>
                        <option value="0" ${car.status == 0 ? 'selected' : ''}>0 - Tạm Ẩn Khỏi Showroom</option>
                    </select>
                </div>

                <div class="mt-4">
                    <button type="submit" class="btn btn-gold btn-lg">💾 LƯU THÔNG TIN SIÊU XE</button>
                    <a href="${pageContext.request.contextPath}/admin/cars" class="btn btn-outline btn-lg ml-2">HỦY</a>
                </div>
            </form>
        </div>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/main.js" charset="UTF-8"></script>
</body>
</html>
