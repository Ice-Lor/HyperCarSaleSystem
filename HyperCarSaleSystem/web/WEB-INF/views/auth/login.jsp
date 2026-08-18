<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Đăng Nhập VIP - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card card-luxury p-4 p-md-5">
                <div class="text-center mb-4">
                    <i class="bi bi-shield-lock-fill text-gold fs-1"></i>
                    <h3 class="fw-bold text-light mt-2" style="font-family: 'Cinzel', serif;">ĐĂNG NHẬP VIP</h3>
                    <p class="small" style="color: #b0b3c0;">Chào mừng quý khách đến với Showroom HyperCar</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger py-2 small">${errorMessage}</div>
                </c:if>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger py-2 small">${sessionScope.errorMessage}</div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success py-2 small">${sessionScope.successMessage}</div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Tên đăng nhập</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-secondary text-gold"><i class="bi bi-person"></i></span>
                            <input type="text" name="username" class="form-control bg-dark border-secondary text-light" value="${username}" required autofocus>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label small" style="color: #b0b3c0;">Mật khẩu</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-secondary text-gold"><i class="bi bi-key"></i></span>
                            <input type="password" name="password" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-gold w-100 py-2 mb-3">
                        <i class="bi bi-box-arrow-in-right me-1"></i> Đăng Nhập
                    </button>

                    <div class="text-center small" style="color: #b0b3c0;">
                        Chưa là thành viên VIP? 
                        <a href="${pageContext.request.contextPath}/register" class="text-gold text-decoration-none fw-bold">Gia nhập ngay</a>
                    </div>
                </form>

                <div class="mt-4 pt-3 border-top border-secondary text-center small" style="color: #b0b3c0;">
                    <div class="fw-bold mb-1 text-gold">Tài khoản trải nghiệm nhanh:</div>
                    <div>Admin: <code>admin</code> / <code>123456</code></div>
                    <div>Khách VIP: <code>johnwick</code> / <code>123456</code></div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
