<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Quản Lý Hãng Xe - Admin HyperCar"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="brands"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">QUẢN LÝ THƯƠNG HIỆU SIÊU XE</h3>
            </div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success py-2 small mb-4">${sessionScope.successMessage}</div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <div class="row g-4">
                <!-- Add Brand Form -->
                <div class="col-lg-4">
                    <div class="card card-luxury p-4">
                        <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2">Thêm Thương Hiệu Mới</h5>
                        <form action="${pageContext.request.contextPath}/admin/brands" method="POST">
                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                            <input type="hidden" name="action" value="add">

                            <div class="mb-3">
                                <label class="form-label text-muted small">Tên Hãng *</label>
                                <input type="text" name="brandName" class="form-control bg-dark border-secondary text-light" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted small">Quốc Gia *</label>
                                <input type="text" name="country" class="form-control bg-dark border-secondary text-light" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted small">URL Logo</label>
                                <input type="url" name="logoUrl" class="form-control bg-dark border-secondary text-light">
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted small">Mô Tả</label>
                                <textarea name="description" class="form-control bg-dark border-secondary text-light" rows="2"></textarea>
                            </div>

                            <button type="submit" class="btn btn-gold w-100 py-2">
                                <i class="bi bi-plus-circle me-1"></i> Thêm Hãng Xe
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Brand Table -->
                <div class="col-lg-8">
                    <div class="card card-luxury p-3">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                                <thead>
                                    <tr class="text-muted small border-bottom border-secondary">
                                        <th>ID</th>
                                        <th>Logo</th>
                                        <th>Tên Thương Hiệu</th>
                                        <th>Quốc Gia</th>
                                        <th class="text-end">Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${brands}" var="b">
                                        <tr class="border-bottom border-secondary">
                                            <td>#${b.brandId}</td>
                                            <td>
                                                <img src="${b.logoUrl}" alt="${b.brandName}" style="height: 30px; object-fit: contain;">
                                            </td>
                                            <td class="fw-bold text-light">${b.brandName}</td>
                                            <td><span class="badge bg-dark border border-secondary">${b.country}</span></td>
                                            <td class="text-end">
                                                <form action="${pageContext.request.contextPath}/admin/brands" method="POST" onsubmit="return confirm('Đại ca có chắc muốn xóa hãng này?');">
                                                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${b.brandId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
