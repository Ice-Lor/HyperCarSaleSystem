<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Đăng Nhập Khách Hàng VIP - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="hyper-card p-4 p-md-5">
                <div class="text-center mb-4">
                    <i class="bi bi-speedometer2 gold-text fs-1"></i>
                    <h3 class="font-brand fw-bold text-white mt-2">ĐĂNG NHẬP VIP</h3>
                    <p class="text-secondary small">Cổng thông tin khách hàng và quản trị hệ thống</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small" role="alert">
                        <i class="bi bi-exclamation-octagon me-2"></i> ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">TÊN ĐĂNG NHẬP HOẶC EMAIL</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-secondary text-muted"><i class="bi bi-person"></i></span>
                            <input type="text" name="username" class="form-control form-control-dark" value="${username}" placeholder="Nhập username/email" required autofocus>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-secondary small fw-bold">MẬT KHẨU</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-secondary text-muted"><i class="bi bi-lock"></i></span>
                            <input type="password" name="password" class="form-control form-control-dark" placeholder="Nhập mật khẩu" required>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-gold btn-lg">
                            <i class="bi bi-box-arrow-in-right me-2"></i> ĐĂNG NHẬP
                        </button>
                    </div>

                    <div class="text-center text-secondary small">
                        Chưa có tài khoản thượng lưu? 
                        <a href="${pageContext.request.contextPath}/register" class="text-warning fw-bold text-decoration-none">Đăng ký ngay</a>
                    </div>
                </form>

                <!-- Demo Accounts Hint -->
                <div class="mt-4 pt-3 border-top border-secondary border-opacity-25 text-secondary small">
                    <div class="fw-bold text-white mb-1"><i class="bi bi-info-circle me-1 text-warning"></i> Tài khoản Demo:</div>
                    <div>• <strong>Admin:</strong> <code>admin</code> / <code>123456</code></div>
                    <div>• <strong>Khách hàng VIP:</strong> <code>johnwick</code> / <code>123456</code></div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
