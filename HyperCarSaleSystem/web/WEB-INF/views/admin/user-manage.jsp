<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản Lý Người Dùng" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="users" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">QUẢN LÝ NGƯỜI DÙNG & PHÂN QUYỀN</h1>
        </div>

        <c:if test="${param.msg == 'status_updated'}">
            <div class="alert alert-success">✓ Cập nhật trạng thái tài khoản thành công!</div>
        </c:if>

        <div class="card p-4 mt-3">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tài Khoản</th>
                        <th>Họ Và Tên</th>
                        <th>Email Liên Hệ</th>
                        <th>Số Điện Thoại</th>
                        <th>Vai Trò</th>
                        <th>Trạng Thái</th>
                        <th>Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td>${u.userId}</td>
                            <td class="font-bold text-gold">@${u.username}</td>
                            <td>${u.fullName}</td>
                            <td>${u.email}</td>
                            <td>${empty u.phone ? 'Chưa cập nhật' : u.phone}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.isAdmin()}">
                                        <span class="badge badge-danger">👑 ADMIN</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-gold">👤 CUSTOMER</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.status == 1}">
                                        <span class="badge badge-success">Đang Hoạt Động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Đã Bị Khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${!u.isAdmin()}">
                                    <form action="${pageContext.request.contextPath}/admin/users" method="POST">
                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                        <input type="hidden" name="action" value="toggle_status" />
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <input type="hidden" name="status" value="${u.status == 1 ? 0 : 1}" />
                                        <button type="submit" class="btn btn-sm ${u.status == 1 ? 'btn-outline text-danger' : 'btn-gold'}">
                                            ${u.status == 1 ? '🔒 Khóa Tài Khoản' : '🔓 Mở Khóa'}
                                        </button>
                                    </form>
                                </c:if>
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
