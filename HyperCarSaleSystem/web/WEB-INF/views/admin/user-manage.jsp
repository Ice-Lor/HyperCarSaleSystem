<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Quản Lý Thành Viên VIP - Admin HyperCar"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp">
            <jsp:param name="active" value="users"/>
        </jsp:include>

        <div class="col-lg-10 col-md-9 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">QUẢN LÝ TÀI KHOẢN KHÁCH HÀNG VIP</h3>
            </div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success py-2 small mb-4">${sessionScope.successMessage}</div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger py-2 small mb-4">${sessionScope.errorMessage}</div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <div class="card card-luxury p-3">
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle mb-0" style="background-color: transparent;">
                        <thead>
                            <tr class="small border-bottom border-secondary" style="color: #b0b3c0;">
                                <th>ID</th>
                                <th>Tên Đăng Nhập</th>
                                <th>Họ Và Tên</th>
                                <th>Email</th>
                                <th>Điện Thoại</th>
                                <th>Vai Trò</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${users}" var="u">
                                <tr class="border-bottom border-secondary">
                                    <td class="text-light">#${u.userId}</td>
                                    <td class="fw-bold text-gold">${u.username}</td>
                                    <td class="text-light">${u.fullName}</td>
                                    <td class="text-light">${u.email}</td>
                                    <td class="text-light">${u.phone}</td>
                                    <td>
                                        <span class="badge ${u.roleName == 'ADMIN' ? 'bg-gold text-dark fw-bold' : 'bg-surface border border-secondary text-light'}">
                                            ${u.roleName}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${u.status == 1 ? 'bg-success' : 'bg-danger'}">
                                            ${u.status == 1 ? 'Hoạt Động' : 'Đã Khóa'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <c:if test="${u.roleName != 'ADMIN'}">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="POST" class="d-inline">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="status" value="${u.status == 1 ? 0 : 1}">
                                                <button type="submit" class="btn btn-sm ${u.status == 1 ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                    <i class="bi ${u.status == 1 ? 'bi-lock-fill' : 'bi-unlock-fill'} me-1"></i>
                                                    ${u.status == 1 ? 'Khóa' : 'Kích Hoạt'}
                                                </button>
                                            </form>
                                        </c:if>
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
