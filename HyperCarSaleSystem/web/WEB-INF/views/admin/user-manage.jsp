<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Khách Hàng & Người Dùng - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="users" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">QUẢN LÝ TÀI KHOẢN KHÁCH HÀNG & PHÂN QUYỀN</h3>
                    <p class="text-secondary small mb-0">Giám sát hoạt động, phân quyền và khóa/mở tài khoản</p>
                </div>
            </div>

            <div class="hyper-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Họ Tên</th>
                                <th>Username</th>
                                <th>Email / SĐT</th>
                                <th>Vai Trò</th>
                                <th>Ngày Tham Gia</th>
                                <th>Trạng Thái</th>
                                <th>Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>
                                        <div class="fw-bold text-white">${u.fullName}</div>
                                        <div class="small text-secondary">${not empty u.address ? u.address : 'Chưa có địa chỉ'}</div>
                                    </td>
                                    <td class="text-warning">@${u.username}</td>
                                    <td>
                                        <div class="small text-white">${u.email}</div>
                                        <div class="small text-secondary">${u.phone}</div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.roleName == 'ADMIN'}"><span class="badge bg-danger">ADMIN</span></c:when>
                                            <c:when test="${u.roleName == 'STAFF'}"><span class="badge bg-info text-dark">STAFF</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary">CUSTOMER</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-secondary small">
                                        <fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy" />
                                    </td>
                                    <td>
                                        <span class="badge ${u.status == 1 ? 'bg-success' : 'bg-danger'}">
                                            ${u.status == 1 ? 'Hoạt động' : 'Đã khóa'}
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${u.userId != sessionScope.currentUser.userId}">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="POST" onsubmit="return confirm('Xác nhận đổi trạng thái khóa/mở cho tài khoản này?');">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                <input type="hidden" name="action" value="toggleStatus">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="currentStatus" value="${u.status}">
                                                <button type="submit" class="btn btn-sm ${u.status == 1 ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                    <i class="bi ${u.status == 1 ? 'bi-lock-fill' : 'bi-unlock-fill'}"></i>
                                                    ${u.status == 1 ? 'Khóa' : 'Mở Khóa'}
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
