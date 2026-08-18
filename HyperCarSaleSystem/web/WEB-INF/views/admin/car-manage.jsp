<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Quản Lý Siêu Xe - Admin HyperCar"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="cars"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">QUẢN LÝ DANH MỤC SIÊU XE</h3>
                    <p class="small mb-0" style="color: #b0b3c0;">Thêm mới, cập nhật giá và số lượng siêu xe trong showroom</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/cars?action=add" class="btn btn-gold btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Thêm Siêu Xe Mới
                </a>
            </div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success py-2 small mb-4">${sessionScope.successMessage}</div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <div class="card card-luxury p-3">
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                        <thead>
                            <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                                <th>ID</th>
                                <th>Siêu Xe</th>
                                <th>Hãng & Phân Khúc</th>
                                <th>Giá Niêm Yết</th>
                                <th>Công Suất</th>
                                <th>Tồn Kho</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${cars}" var="c">
                                <tr class="border-bottom border-secondary">
                                    <td class="text-light">#${c.carId}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${pageContext.request.contextPath}/${c.thumbnailUrl}" alt="${c.modelName}" style="width: 70px; height: 45px; object-fit: cover;" class="rounded me-2">
                                            <div>
                                                <div class="fw-bold text-light">${c.modelName}</div>
                                                <small style="color: #b0b3c0;">${c.year}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="text-gold fw-semibold">${c.brandName}</div>
                                        <small style="color: #b0b3c0;">${c.categoryName}</small>
                                    </td>
                                    <td class="fw-bold text-gold"><fmt:formatNumber value="${c.price}" type="currency" currencySymbol="$"/></td>
                                    <td class="text-light">${c.horsepower} HP (${c.topSpeed} km/h)</td>
                                    <td>
                                        <span class="badge ${c.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">${c.stockQuantity}</span>
                                    </td>
                                    <td>
                                        <span class="badge ${c.status == 1 ? 'bg-primary' : 'bg-secondary'}">
                                            ${c.status == 1 ? 'Hiển Thị' : 'Tạm Ẩn'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <div class="d-flex justify-content-end gap-1">
                                            <a href="${pageContext.request.contextPath}/admin/cars?action=edit&id=${c.carId}" class="btn btn-outline-warning btn-sm">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/admin/cars" method="POST" onsubmit="return confirm('Đại ca có chắc muốn xóa siêu xe này?');">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${c.carId}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
