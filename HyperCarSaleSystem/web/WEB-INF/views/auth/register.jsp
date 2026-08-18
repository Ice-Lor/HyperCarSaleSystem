<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Gia Nhập Thành Viên VIP - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7">
            <div class="card card-luxury p-4 p-md-5">
                <div class="text-center mb-4">
                    <i class="bi bi-gem text-gold fs-1"></i>
                    <h3 class="fw-bold text-light mt-2" style="font-family: 'Cinzel', serif;">GIA NHẬP CỘNG ĐỒNG VIP</h3>
                    <p class="small" style="color: #b0b3c0;">Đăng ký để nhận quyền đặt cọc độc quyền và trải nghiệm lái thử Megacar</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger py-2 small">${errorMessage}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Tên đăng nhập *</label>
                            <input type="text" name="username" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Họ và Tên quý khách *</label>
                            <input type="text" name="fullName" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Mật khẩu *</label>
                            <input type="password" name="password" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Xác nhận mật khẩu *</label>
                            <input type="password" name="confirmPassword" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Email liên hệ VIP *</label>
                            <input type="email" name="email" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small" style="color: #b0b3c0;">Số điện thoại *</label>
                            <input type="tel" name="phone" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label small" style="color: #b0b3c0;">Địa chỉ nhận hợp đồng & xe</label>
                        <input type="text" name="address" class="form-control bg-dark border-secondary text-light" placeholder="Biệt thự / Căn hộ / Địa chỉ VIP">
                    </div>

                    <button type="submit" class="btn btn-gold w-100 py-2 mb-3">
                        <i class="bi bi-check-circle-fill me-1"></i> Hoàn Tất Đăng Ký
                    </button>

                    <div class="text-center small" style="color: #b0b3c0;">
                        Đã có tài khoản? 
                        <a href="${pageContext.request.contextPath}/login" class="text-gold text-decoration-none fw-bold">Đăng nhập ngay</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
