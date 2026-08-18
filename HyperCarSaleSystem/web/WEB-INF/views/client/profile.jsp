<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Hồ Sơ VIP - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="mb-4">
        <h2 class="fw-bold text-light" style="font-family: 'Cinzel', serif;">HỒ SƠ THÀNH VIÊN VIP</h2>
        <p class="small" style="color: #b0b3c0;">Quản lý thông tin bảo mật và hồ sơ chủ sở hữu siêu xe</p>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success py-2 small mb-4">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger py-2 small mb-4">${errorMessage}</div>
    </c:if>

    <div class="row g-4">
        <!-- Personal Info Form -->
        <div class="col-lg-6">
            <div class="card card-luxury p-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-person-lines-fill me-2"></i> THÔNG TIN CÁ NHÂN
                </h5>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="updateInfo">

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Tên tài khoản</label>
                        <input type="text" class="form-control bg-dark border-secondary text-light" value="${user.username}" readonly>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Họ và tên *</label>
                        <input type="text" name="fullName" class="form-control bg-dark border-secondary text-light" value="${user.fullName}" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Email liên hệ VIP *</label>
                        <input type="email" name="email" class="form-control bg-dark border-secondary text-light" value="${user.email}" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Số điện thoại *</label>
                        <input type="tel" name="phone" class="form-control bg-dark border-secondary text-light" value="${user.phone}" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label small" style="color: #b0b3c0;">Địa chỉ bàn giao mặc định</label>
                        <input type="text" name="address" class="form-control bg-dark border-secondary text-light" value="${user.address}">
                    </div>

                    <button type="submit" class="btn btn-gold w-100 py-2">
                        <i class="bi bi-check2-circle me-1"></i> Lưu Cập Nhật Thông Tin
                    </button>
                </form>
            </div>
        </div>

        <!-- Change Password Form -->
        <div class="col-lg-6">
            <div class="card card-luxury p-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-key-fill me-2"></i> ĐỔI MẬT KHẨU BẢO MẬT
                </h5>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="changePassword">

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Mật khẩu hiện tại *</label>
                        <input type="password" name="oldPassword" class="form-control bg-dark border-secondary text-light" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Mật khẩu mới *</label>
                        <input type="password" name="newPassword" class="form-control bg-dark border-secondary text-light" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label small" style="color: #b0b3c0;">Xác nhận mật khẩu mới *</label>
                        <input type="password" name="confirmPassword" class="form-control bg-dark border-secondary text-light" required>
                    </div>

                    <button type="submit" class="btn btn-outline-gold w-100 py-2">
                        <i class="bi bi-shield-lock me-1"></i> Cập Nhật Mật Khẩu
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
