<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${car != null ? 'Cập Nhật Siêu Xe' : 'Thêm Siêu Xe Mới'} - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="cars" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Form Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">${car != null ? 'CẬP NHẬT SIÊU XE' : 'THÊM SIÊU XE MỚI'}</h3>
                    <p class="text-secondary small mb-0">Điền thông số chi tiết của mẫu xe để đưa lên hệ thống</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/cars" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Quay Lại Danh Sách
                </a>
            </div>

            <div class="hyper-card p-4">
                <form action="${pageContext.request.contextPath}/admin/cars" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="save">
                    <input type="hidden" name="carId" value="${car != null ? car.carId : 0}">

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">TÊN MẪU SIÊU XE (*)</label>
                            <input type="text" name="modelName" class="form-control form-control-dark" value="${car.modelName}" placeholder="VD: Bugatti Tourbillon V16" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">THƯƠNG HIỆU (*)</label>
                            <select name="brandId" class="form-select form-select-dark" required>
                                <c:forEach var="b" items="${brands}">
                                    <option value="${b.brandId}" ${car != null && car.brandId == b.brandId ? 'selected' : ''}>${b.brandName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">PHÂN LOẠI (*)</label>
                            <select name="categoryId" class="form-select form-select-dark" required>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" ${car != null && car.categoryId == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label class="form-label text-secondary small fw-bold">GIÁ NIÊM YẾT (USD) (*)</label>
                            <input type="number" step="1000" name="price" class="form-control form-control-dark" value="${car != null ? car.price : 1000000}" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label text-secondary small fw-bold">TỶ LỆ ĐẶT CỌC (%) (*)</label>
                            <input type="number" step="0.5" name="depositRate" class="form-control form-control-dark" value="${car != null ? car.depositRate : 10.0}" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label text-secondary small fw-bold">SỐ LƯỢNG TỒN KHO (*)</label>
                            <input type="number" name="stockQuantity" class="form-control form-control-dark" value="${car != null ? car.stockQuantity : 1}" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">NĂM SẢN XUẤT</label>
                            <input type="number" name="year" class="form-control form-control-dark" value="${car != null ? car.year : 2025}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">CÔNG SUẤT (HP)</label>
                            <input type="number" name="horsepower" class="form-control form-control-dark" value="${car != null ? car.horsepower : 1000}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">0 - 100 KM/H (GIÂY)</label>
                            <input type="number" step="0.01" name="acceleration0100" class="form-control form-control-dark" value="${car != null ? car.acceleration0100 : 2.5}" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label text-secondary small fw-bold">TỐC ĐỘ MAX (KM/H)</label>
                            <input type="number" name="topSpeed" class="form-control form-control-dark" value="${car != null ? car.topSpeed : 350}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">LINK HÌNH ẢNH ĐẠI DIỆN (URL)</label>
                        <input type="url" name="thumbnailUrl" class="form-control form-control-dark" value="${car.thumbnailUrl}" placeholder="https://images.unsplash.com/..." required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">THÔNG SỐ ĐỘNG CƠ</label>
                            <input type="text" name="engineSpec" class="form-control form-control-dark" value="${car.engineSpec}" placeholder="VD: 8.3L V16 Tự nhiên + 3 Motor điện">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">TÙY CHỌN MÀU SƠN (Cách nhau bằng dấu phẩy)</label>
                            <input type="text" name="colorOptions" class="form-control form-control-dark" value="${car.colorOptions}" placeholder="VD: Đỏ Rosso Corsa, Vàng Giallo, Đen Carbon">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">MÔ TẢ CHI TIẾT & ĐẶC ĐIỂM NỔI BẬT</label>
                        <textarea name="description" rows="4" class="form-control form-control-dark" placeholder="Mô tả công nghệ khung gầm, vật liệu titanium...">${car.description}</textarea>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-secondary small fw-bold">TRẠNG THÁI BÁN</label>
                        <select name="status" class="form-select form-select-dark w-auto">
                            <option value="1" ${car == null || car.status == 1 ? 'selected' : ''}>Đang mở bán</option>
                            <option value="0" ${car != null && car.status == 0 ? 'selected' : ''}>Tạm ngừng bán</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-gold btn-lg px-5">
                        <i class="bi bi-check2-circle me-1"></i> ${car != null ? 'Lưu Thay Đổi' : 'Thêm Siêu Xe Vào Showroom'}
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
