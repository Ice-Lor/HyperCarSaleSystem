<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Đăng Ký Thành Viên VIP" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="auth-wrapper">
    <div class="auth-card auth-card-lg">
        <div class="auth-header">
            <h2 class="auth-title">ĐĂNG KÝ <span class="text-gold">THÀNH VIÊN VIP</span></h2>
            <p class="auth-subtitle">Trải nghiệm dịch vụ cá nhân hóa và đặc quyền đặt cọc siêu xe giới hạn</p>
        </div>

        <!-- Thông báo lỗi nếu có -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST" class="auth-form">
            <!-- Mã bảo mật CSRF Token -->
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="form-row">
                <div class="form-group col-6">
                    <label for="username" class="form-label">Tên Đăng Nhập <span class="text-danger">*</span></label>
                    <input type="text" id="username" name="username" class="form-control" 
                           placeholder="vd: brucewayne" value="${username}" required>
                </div>
                <div class="form-group col-6">
                    <label for="email" class="form-label">Địa Chỉ Email <span class="text-danger">*</span></label>
                    <input type="email" id="email" name="email" class="form-control" 
                           placeholder="vd: vip@wayne.com" value="${email}" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group col-6">
                    <label for="fullName" class="form-label">Họ Và Tên Khách Hàng <span class="text-danger">*</span></label>
                    <input type="text" id="fullName" name="fullName" class="form-control" 
                           placeholder="vd: Bruce Wayne" value="${fullName}" required>
                </div>
                <div class="form-group col-6">
                    <label for="phone" class="form-label">Số Điện Thoại (10 chữ số)</label>
                    <input type="tel" id="phone" name="phone" class="form-control" 
                           placeholder="vd: 0912345678" value="${phone}">
                </div>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Địa Chỉ Thường Trú / Giao Xe</label>
                <input type="text" id="address" name="address" class="form-control" 
                       placeholder="vd: Penthouse Landmark 81, Vinhomes Central Park, TP.HCM" value="${address}">
            </div>

            <div class="form-row">
                <div class="form-group col-6">
                    <label for="password" class="form-label">Mật Khẩu <span class="text-danger">*</span></label>
                    <input type="password" id="password" name="password" class="form-control" 
                           placeholder="Tối thiểu 6 ký tự" required>
                </div>
                <div class="form-group col-6">
                    <label for="confirmPassword" class="form-label">Xác Nhận Mật Khẩu <span class="text-danger">*</span></label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                           placeholder="Nhập lại mật khẩu" required>
                </div>
            </div>

            <button type="submit" class="btn btn-gold btn-block btn-lg mt-3">HOÀN TẤT ĐĂNG KÝ VIP</button>
        </form>

        <div class="auth-footer">
            <p>Đã là thành viên thượng lưu? 
                <a href="${pageContext.request.contextPath}/login" class="text-gold font-bold">Đăng nhập ngay</a>
            </p>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
