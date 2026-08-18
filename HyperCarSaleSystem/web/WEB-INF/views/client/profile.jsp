<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Hồ Sơ Cá Nhân VIP - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <div class="row g-4">
        <!-- Sidebar / Summary -->
        <div class="col-lg-4">
            <div class="hyper-card p-4 text-center">
                <div class="rounded-circle bg-warning text-dark d-inline-flex align-items-center justify-content-center fw-bold fs-2 mb-3" style="width: 80px; height: 80px;">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h4 class="font-brand fw-bold text-white mb-1">${user.fullName}</h4>
                <p class="text-secondary small mb-2">@${user.username}</p>
                <span class="badge bg-danger text-uppercase mb-3">${user.roleName}</span>

                <div class="text-start border-top border-secondary border-opacity-25 pt-3 text-secondary small lh-lg">
                    <div><i class="bi bi-envelope me-2 text-warning"></i> ${user.email}</div>
                    <div><i class="bi bi-telephone me-2 text-warning"></i> ${not empty user.phone ? user.phone : 'Chưa cập nhật'}</div>
                    <div><i class="bi bi-geo-alt me-2 text-warning"></i> ${not empty user.address ? user.address : 'Chưa cập nhật'}</div>
                    <div><i class="bi bi-calendar3 me-2 text-warning"></i> Tham gia: <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy" /></div>
                </div>
            </div>
        </div>

        <!-- Update Forms -->
        <div class="col-lg-8">
            <!-- Update Profile Info -->
            <div class="hyper-card p-4 mb-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    <i class="bi bi-pencil-square me-2"></i> CẬP NHẬT THÔNG TIN CÁ NHÂN
                </h5>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="updateProfile">

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">HỌ VÀ TÊN</label>
                        <input type="text" name="fullName" class="form-control form-control-dark" value="${user.fullName}" required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">EMAIL</label>
                            <input type="email" name="email" class="form-control form-control-dark" value="${user.email}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">SỐ ĐIỆN THOẠI</label>
                            <input type="text" name="phone" class="form-control form-control-dark" value="${user.phone}">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-secondary small fw-bold">ĐỊA CHỈ NHẬN BÀN GIAO XE</label>
                        <input type="text" name="address" class="form-control form-control-dark" value="${user.address}">
                    </div>

                    <button type="submit" class="btn btn-gold btn-sm">
                        <i class="bi bi-check2 me-1"></i> Lưu Thay Đổi
                    </button>
                </form>
            </div>

            <!-- Change Password -->
            <div class="hyper-card p-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    <i class="bi bi-shield-lock-fill me-2"></i> THAY ĐỔI MẬT KHẨU
                </h5>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                    <input type="hidden" name="action" value="changePassword">

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">MẬT KHẨU HIỆN TẠI</label>
                        <input type="password" name="oldPassword" class="form-control form-control-dark" required>
                    </div>

                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">MẬT KHẨU MỚI</label>
                            <input type="password" name="newPassword" class="form-control form-control-dark" minlength="6" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">XÁC NHẬN MẬT KHẨU MỚI</label>
                            <input type="password" name="confirmNewPassword" class="form-control form-control-dark" minlength="6" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-outline-gold btn-sm">
                        <i class="bi bi-key-fill me-1"></i> Đổi Mật Khẩu
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
