<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Hồ Sơ Cá Nhân" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">HỒ SƠ <span class="text-gold">CÁ NHÂN</span></h1>
        <p class="page-subtitle">Quản lý thông tin định danh cá nhân và thiết lập bảo mật tài khoản</p>
    </div>
</div>

<div class="container section">
    <div class="profile-layout">
        <!-- CỘT TRÁI: THẺ THÀNH VIÊN CARD -->
        <div class="profile-sidebar-col">
            <div class="vip-card">
                <div class="vip-card-chip">💳</div>
                <div class="vip-card-brand"><span class="text-gold">HYPER</span>CAR MEMBER</div>
                <div class="vip-card-number">**** **** **** ${sessionScope.user.userId + 8888}</div>
                <div class="vip-card-holder">
                    <div class="holder-label">CHỦ THẺ THÀNH VIÊN</div>
                    <div class="holder-name">${sessionScope.user.fullName}</div>
                </div>
                <div class="vip-card-badge">OFFICIAL MEMBER</div>
            </div>

            <div class="card p-3 mt-4">
                <div class="font-bold text-gold mb-2">QUYỀN LỢI CỦA THÀNH VIÊN:</div>
                <ul class="vip-perks-list font-sm">
                    <li>✓ Ưu tiên trải nghiệm trường đua F1</li>
                    <li>✓ Quyền đặt trước các mẫu siêu xe độc bản (1 of 1)</li>
                    <li>✓ Chuyên viên Concierge phục vụ riêng 24/7</li>
                    <li>✓ Miễn phí bảo dưỡng định kỳ 2 năm đầu</li>
                </ul>
            </div>
        </div>

        <!-- CỘT PHẢI: FORM CẬP NHẬT THÔNG TIN & ĐỔI MẬT KHẨU -->
        <div class="profile-main-col">
            <!-- 1. Form Cập nhật Thông tin cá nhân -->
            <div class="card p-4 mb-4">
                <h3 class="card-title">1. THÔNG TIN ĐỊNH DANH CÁ NHÂN</h3>

                <c:if test="${not empty profileError}">
                    <div class="alert alert-danger">${profileError}</div>
                </c:if>
                <c:if test="${not empty profileSuccess}">
                    <div class="alert alert-success">${profileSuccess}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/MainController" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                    <input type="hidden" name="action" value="update_profile" />

                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Tên Đăng Nhập (Cố định):</label>
                            <input type="text" class="form-control" value="${sessionScope.user.username}" disabled>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Địa Chỉ Email <span class="text-danger">*</span></label>
                            <input type="email" name="email" class="form-control" 
                                   value="${sessionScope.user.email}" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Họ Và Tên Khách Hàng <span class="text-danger">*</span></label>
                            <input type="text" name="fullName" class="form-control" 
                                   value="${sessionScope.user.fullName}" required>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Số Điện Thoại:</label>
                            <input type="tel" name="phone" class="form-control" 
                                   value="${sessionScope.user.phone}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Địa Chỉ Thường Trú / Giao Xe:</label>
                        <input type="text" name="address" class="form-control" 
                               value="${sessionScope.user.address}">
                    </div>

                    <button type="submit" class="btn btn-gold">LƯU THAY ĐỔI HỒ SƠ</button>
                </form>
            </div>

            <!-- 2. Form Đổi Mật Khẩu Bảo Mật -->
            <div class="card p-4">
                <h3 class="card-title">2. THIẾT LẬP MẬT KHẨU MỚI</h3>

                <c:if test="${not empty passwordError}">
                    <div class="alert alert-danger">${passwordError}</div>
                </c:if>
                <c:if test="${not empty passwordSuccess}">
                    <div class="alert alert-success">${passwordSuccess}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/MainController" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                    <input type="hidden" name="action" value="change_password" />

                    <div class="form-group">
                        <label class="form-label">Mật Khẩu Hiện Tại <span class="text-danger">*</span></label>
                        <input type="password" name="oldPassword" class="form-control" 
                               placeholder="Nhập mật khẩu đang sử dụng" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Mật Khẩu Mới <span class="text-danger">*</span></label>
                            <input type="password" name="newPassword" class="form-control" 
                                   placeholder="Tối thiểu 6 ký tự" required>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Xác Nhận Mật Khẩu Mới <span class="text-danger">*</span></label>
                            <input type="password" name="confirmPassword" class="form-control" 
                                   placeholder="Nhập lại mật khẩu mới" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-outline">CẬP NHẬT MẬT KHẨU BẢO MẬT</button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
