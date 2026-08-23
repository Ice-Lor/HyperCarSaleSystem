<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- THANH ĐIỀU HƯỚNG QUẢN TRỊ VIÊN QUA MAINCONTROLLER -->
<aside class="admin-sidebar">
    <!-- Logo & Nhãn Quản Trị -->
    <div class="sidebar-header">
        <a href="${pageContext.request.contextPath}/MainController?action=AdminDashboard" class="sidebar-logo">
            <span class="logo-gold">HYPER</span>CAR
        </a>
        <span class="badge badge-admin">ADMIN PORTAL</span>
    </div>

    <!-- Thông tin Admin đang đăng nhập -->
    <div class="sidebar-user">
        <div class="user-avatar">👑</div>
        <div class="user-info">
            <div class="user-name">${sessionScope.user.fullName}</div>
            <div class="user-role">Quản Trị Viên Hệ Thống</div>
        </div>
    </div>

    <!-- Danh sách Menu Quản trị qua MainController -->
    <ul class="sidebar-menu">
        <li class="sidebar-item ${param.active == 'dashboard' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminDashboard" class="sidebar-link">
                📊 <span>Tổng Quan (Dashboard)</span>
            </a>
        </li>
        <li class="sidebar-item ${param.active == 'cars' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminCars" class="sidebar-link">
                🏎️ <span>Kho Siêu Xe</span>
            </a>
        </li>
        <li class="sidebar-item ${param.active == 'brands' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminBrands" class="sidebar-link">
                🏷️ <span>Hãng Sản Xuất</span>
            </a>
        </li>
        <li class="sidebar-item ${param.active == 'orders' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminOrders" class="sidebar-link">
                📜 <span>Hợp Đồng Đặt Cọc</span>
            </a>
        </li>
        <li class="sidebar-item ${param.active == 'users' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminUsers" class="sidebar-link">
                👥 <span>Quản Lý Người Dùng</span>
            </a>
        </li>
        <li class="sidebar-item ${param.active == 'bookings' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/MainController?action=AdminBookings" class="sidebar-link">
                🏁 <span>Lịch Lái Thử F1</span>
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/MainController?action=ExportReport" class="sidebar-link export-link" target="_blank">
                📥 <span>Xuất Báo Cáo Excel</span>
            </a>
        </li>

        <li class="sidebar-divider"></li>

        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/MainController?action=Home" class="sidebar-link">
                🌐 <span>Xem Showroom</span>
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/MainController?action=Logout" class="sidebar-link text-danger">
                🚪 <span>Đăng Xuất</span>
            </a>
        </li>
    </ul>
</aside>
