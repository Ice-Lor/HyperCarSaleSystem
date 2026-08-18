<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Gia Nhập Câu Lạc Bộ VIP - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="hyper-card p-4 p-md-5">
                <div class="text-center mb-4">
                    <i class="bi bi-shield-shaded gold-text fs-1"></i>
                    <h3 class="font-brand fw-bold text-white mt-2">GIA NHẬP CÂU LẠC BỘ VIP</h3>
                    <p class="text-secondary small">Trở thành thành viên để nhận quyền ưu tiên đặt cọc và tham gia các sự kiện kín</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small" role="alert">
                        <i class="bi bi-exclamation-octagon me-2"></i> ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">TÊN ĐĂNG NHẬP (*)</label>
                        <input type="text" name="username" class="form-control form-control-dark" value="${username}" placeholder="VD: tony_stark" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">HỌ VÀ TÊN (*)</label>
                        <input type="text" name="fullName" class="form-control form-control-dark" value="${fullName}" placeholder="VD: Tony Stark" required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">EMAIL (*)</label>
                            <input type="email" name="email" class="form-control form-control-dark" value="${email}" placeholder="tony@stark.com" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">SỐ ĐIỆN THOẠI (*)</label>
                            <input type="text" name="phone" class="form-control form-control-dark" value="${phone}" placeholder="0988888888" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">ĐỊA CHỈ</label>
                        <input type="text" name="address" class="form-control form-control-dark" value="${address}" placeholder="Tòa nhà Landmark 81 / Biệt thự">
                    </div>

                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">MẬT KHẨU (*)</label>
                            <input type="password" name="password" class="form-control form-control-dark" placeholder="Tối thiểu 6 ký tự" minlength="6" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">XÁC NHẬN MẬT KHẨU (*)</label>
                            <input type="password" name="confirmPassword" class="form-control form-control-dark" placeholder="Nhập lại mật khẩu" minlength="6" required>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-gold btn-lg">
                            <i class="bi bi-person-plus-fill me-2"></i> TẠO TÀI KHOẢN VIP
                        </button>
                    </div>

                    <div class="text-center text-secondary small">
                        Đã có tài khoản? 
                        <a href="${pageContext.request.contextPath}/login" class="text-warning fw-bold text-decoration-none">Đăng nhập tại đây</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
