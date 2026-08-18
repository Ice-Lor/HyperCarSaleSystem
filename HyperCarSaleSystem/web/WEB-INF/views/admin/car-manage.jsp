<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Siêu Xe - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="cars" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">QUẢN LÝ DANH MỤC SIÊU XE</h3>
                    <p class="text-secondary small mb-0">Quản lý kho hàng, thông số kỹ thuật và giá bán niêm yết</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/cars?action=add" class="btn btn-gold btn-sm">
                        <i class="bi bi-plus-circle me-1"></i> Thêm Siêu Xe Mới
                    </a>
                </div>
            </div>

            <div class="hyper-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Siêu Xe</th>
                                <th>Thương Hiệu</th>
                                <th>Phân Loại</th>
                                <th>Giá Niêm Yết</th>
                                <th>Tồn Kho</th>
                                <th>Trạng Thái</th>
                                <th>Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${carList}">
                                <tr>
                                    <td class="text-secondary small">${c.carId}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${c.thumbnailUrl}" alt="${c.modelName}" class="rounded me-2" style="width: 60px; height: 38px; object-fit: cover;">
                                            <div>
                                                <div class="fw-bold text-white">${c.modelName}</div>
                                                <div class="small text-secondary">${c.horsepower} HP • ${c.year}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-warning">${c.brandName}</td>
                                    <td><span class="badge bg-secondary">${c.categoryName}</span></td>
                                    <td class="text-white font-brand">
                                        <fmt:formatNumber value="${c.price}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                                    </td>
                                    <td>
                                        <span class="badge ${c.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">
                                            ${c.stockQuantity} chiếc
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${c.status == 1 ? 'bg-success' : 'bg-secondary'}">
                                            ${c.status == 1 ? 'Đang Bán' : 'Tạm Dừng'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <a href="${pageContext.request.contextPath}/admin/cars?action=edit&id=${c.carId}" class="btn btn-sm btn-outline-warning" title="Chỉnh sửa">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/admin/cars" method="POST" onsubmit="return confirm('Đại ca có chắc muốn tạm dừng bán xe này?');" style="display:inline;">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${c.carId}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger" title="Tạm dừng bán">
                                                    <i class="bi bi-pause-circle"></i>
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
