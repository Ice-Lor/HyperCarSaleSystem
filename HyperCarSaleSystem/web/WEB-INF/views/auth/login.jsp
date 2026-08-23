<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Đăng Nhập Thành Viên" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-header">
            <h2 class="auth-title">ĐĂNG NHẬP <span class="text-gold">HỆ THỐNG</span></h2>
            <p class="auth-subtitle">Cổng giao dịch & quản trị siêu xe độc bản HyperCar</p>
        </div>

        <!-- Thông báo lỗi hoặc thành công -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${param.error == 'auth_required'}">
            <div class="alert alert-warning">Vui lòng đăng nhập để thực hiện giao dịch này!</div>
        </c:if>
        <c:if test="${param.error == 'admin_required'}">
            <div class="alert alert-danger">Khu vực yêu cầu quyền Quản Trị Viên (Admin)!</div>
        </c:if>
        <c:if test="${param.success == 'registered'}">
            <div class="alert alert-success">Đăng ký tài khoản thành công! Vui lòng đăng nhập.</div>
        </c:if>
        <c:if test="${param.msg == 'logged_out'}">
            <div class="alert alert-info">Đã đăng xuất tài khoản an toàn khỏi hệ thống.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST" class="auth-form">
            <!-- Mã bảo mật CSRF Token -->
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="form-group">
                <label for="username" class="form-label">Tên Đăng Nhập</label>
                <input type="text" id="username" name="username" class="form-control" 
                       placeholder="Nhập username của bạn" value="${username}" required autofocus>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Mật Khẩu Bảo Mật</label>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Nhập mật khẩu" required>
            </div>

            <button type="submit" class="btn btn-gold btn-block btn-lg">ĐĂNG NHẬP NGAY</button>
        </form>

        <div class="auth-footer">
            <p>Chưa có tài khoản? 
                <a href="${pageContext.request.contextPath}/register" class="text-gold font-bold">Đăng ký tài khoản mới</a>
            </p>
        </div>

        <!-- Gợi ý tài khoản demo -->
        <div class="demo-box">
            <div class="demo-title">💡 Tài khoản thử nghiệm (Mật khẩu: 123456):</div>
            <div class="demo-list">
                <span>👑 <strong>Admin:</strong> admin</span> | 
                <span>🏎️ <strong>Khách hàng:</strong> johnwick / tonystark</span>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
