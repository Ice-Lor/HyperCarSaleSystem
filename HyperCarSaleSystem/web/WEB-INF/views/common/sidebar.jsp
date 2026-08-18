<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-lg-3 col-md-4 mb-4">
    <div class="hyper-card p-3">
        <!-- Admin Profile Summary -->
        <div class="text-center pb-3 mb-3 border-bottom border-secondary border-opacity-25">
            <div class="rounded-circle bg-warning text-dark d-inline-flex align-items-center justify-content-center fw-bold fs-3 mb-2" style="width: 60px; height: 60px;">
                <i class="bi bi-shield-lock-fill"></i>
            </div>
            <h6 class="fw-bold mb-0 text-white">${sessionScope.currentUser.fullName}</h6>
            <span class="badge bg-danger text-uppercase mt-1">${sessionScope.currentUser.roleName}</span>
        </div>

        <!-- Navigation Menu -->
        <div class="nav flex-column nav-pills gap-1">
            <a class="nav-link text-white ${activeMenu == 'dashboard' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="bi bi-speedometer2 me-2"></i> Tổng Quan Dashboard
            </a>
            <a class="nav-link text-white ${activeMenu == 'cars' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/cars">
                <i class="bi bi-car-front-fill me-2"></i> Quản Lý Siêu Xe
            </a>
            <a class="nav-link text-white ${activeMenu == 'brands' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/brands">
                <i class="bi bi-tags-fill me-2"></i> Quản Lý Thương Hiệu
            </a>
            <a class="nav-link text-white ${activeMenu == 'orders' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/orders">
                <i class="bi bi-receipt me-2"></i> Hợp Đồng Đặt Cọc
            </a>
            <a class="nav-link text-white ${activeMenu == 'bookings' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/bookings">
                <i class="bi bi-calendar2-check-fill me-2"></i> Lịch Lái Thử VIP Track
            </a>
            <a class="nav-link text-white ${activeMenu == 'users' ? 'active bg-warning text-dark fw-bold' : ''}" 
               href="${pageContext.request.contextPath}/admin/users">
                <i class="bi bi-people-fill me-2"></i> Quản Lý Khách Hàng
            </a>
            <hr class="border-secondary my-2">
            <a class="nav-link text-info" href="${pageContext.request.contextPath}/admin/export-orders">
                <i class="bi bi-file-earmark-spreadsheet me-2"></i> Xuất Báo Cáo CSV (Excel)
            </a>
            <a class="nav-link text-light" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-box-arrow-left me-2"></i> Về Trang Khách Hàng
            </a>
        </div>
    </div>
</div>
