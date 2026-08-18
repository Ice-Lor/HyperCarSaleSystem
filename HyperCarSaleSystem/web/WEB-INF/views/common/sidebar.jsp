<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-lg-2 col-md-3 bg-card p-3 border-end border-secondary min-vh-100" style="background-color: #181924;">
    <div class="text-center py-3 border-bottom border-secondary mb-3">
        <i class="bi bi-speedometer2 text-gold fs-1"></i>
        <h6 class="text-gold fw-bold mt-2" style="font-family: 'Cinzel', serif;">ADMIN PORTAL</h6>
        <span class="badge bg-gold text-dark">VIP Manager</span>
    </div>

    <ul class="nav nav-pills flex-column gap-2">
        <li class="nav-item">
            <a class="nav-link ${param.active == 'dashboard' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="bi bi-grid-1x2-fill me-2"></i> Tổng Quan (Dashboard)
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'cars' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/cars">
                <i class="bi bi-car-front-fill me-2"></i> Quản Lý Siêu Xe
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'brands' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/brands">
                <i class="bi bi-tags-fill me-2"></i> Hãng Sản Xuất
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'orders' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/orders">
                <i class="bi bi-receipt-cutoff me-2"></i> Hợp Đồng Đặt Cọc
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'bookings' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/bookings">
                <i class="bi bi-calendar2-week me-2"></i> Lịch Lái Thử VIP
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.active == 'users' ? 'active bg-gold text-dark fw-bold' : 'text-light'}" 
               href="${pageContext.request.contextPath}/admin/users">
                <i class="bi bi-people-fill me-2"></i> Khách Hàng VIP
            </a>
        </li>
        <li class="nav-item mt-4 pt-3 border-top border-secondary">
            <a class="nav-link text-info" href="${pageContext.request.contextPath}/admin/export-orders">
                <i class="bi bi-file-earmark-spreadsheet-fill me-2"></i> Xuất Báo Cáo CSV
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-warning" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-box-arrow-left me-2"></i> Về Trang Showroom
            </a>
        </li>
    </ul>
</div>
