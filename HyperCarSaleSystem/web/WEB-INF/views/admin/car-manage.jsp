<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Quản Lý Kho Siêu Xe" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="cars" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">QUẢN LÝ KHO SIÊU XE SHOWROOM</h1>
            <div class="admin-topbar-actions">
                <a href="${pageContext.request.contextPath}/admin/cars?action=create" class="btn btn-gold btn-sm">
                    + THÊM SIÊU XE MỚI
                </a>
            </div>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div class="alert alert-success">✓ Lưu thông tin siêu xe thành công!</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div class="alert alert-info">✓ Đã xóa siêu xe khỏi hệ thống.</div>
        </c:if>
        <c:if test="${param.msg == 'status_updated'}">
            <div class="alert alert-success">✓ Cập nhật trạng thái mở bán thành công!</div>
        </c:if>

        <div class="card p-4 mt-3">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Hình Ảnh</th>
                        <th>Tên Siêu Xe</th>
                        <th>Hãng</th>
                        <th>Giá Niêm Yết (USD)</th>
                        <th>Hiệu Năng</th>
                        <th>Tồn Kho</th>
                        <th>Mở Bán</th>
                        <th>Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="car" items="${cars}">
                        <tr>
                            <td>${car.carId}</td>
                            <td>
                                <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" 
                                     alt="${car.modelName}" class="admin-thumb">
                            </td>
                            <td class="font-bold">
                                <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}" target="_blank">
                                    ${car.modelName}
                                </a>
                                <div class="font-sm text-muted">Năm: ${car.year} | ${car.categoryName}</div>
                            </td>
                            <td><span class="badge badge-brand">${car.brandName}</span></td>
                            <td class="font-bold text-gold">
                                <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                            <td>
                                <div class="font-sm">⚡ ${car.horsepower} HP</div>
                                <div class="font-sm text-muted">🚀 ${car.topSpeed} km/h</div>
                            </td>
                            <td>
                                <span class="badge ${car.stockQuantity > 0 ? 'badge-stock' : 'badge-danger'}">
                                    ${car.stockQuantity} xe
                                </span>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/cars" method="POST">
                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                    <input type="hidden" name="action" value="toggle_status" />
                                    <input type="hidden" name="id" value="${car.carId}" />
                                    <input type="hidden" name="status" value="${car.status == 1 ? 0 : 1}" />
                                    <button type="submit" class="btn btn-sm ${car.status == 1 ? 'btn-success' : 'btn-outline'}">
                                        ${car.status == 1 ? 'Đang Mở Bán' : 'Tạm Ẩn'}
                                    </button>
                                </form>
                            </td>
                            <td>
                                <div class="d-flex">
                                    <a href="${pageContext.request.contextPath}/admin/cars?action=edit&id=${car.carId}" 
                                       class="btn btn-outline btn-sm mr-2">✏️ Sửa</a>

                                    <form action="${pageContext.request.contextPath}/admin/cars" method="POST" 
                                          onsubmit="return confirm('Bạn có chắc chắn muốn xóa siêu xe này?')">
                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                        <input type="hidden" name="action" value="delete" />
                                        <input type="hidden" name="id" value="${car.carId}" />
                                        <button type="submit" class="btn btn-outline text-danger btn-sm">🗑️</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
