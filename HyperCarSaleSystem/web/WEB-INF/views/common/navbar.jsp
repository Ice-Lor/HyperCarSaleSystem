<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- THANH ĐIỀU HƯỚNG THƯỢNG LƯU (LUXURY GOLD & DARK NAVBAR) -->
<nav class="navbar">
    <div class="nav-container">
        <!-- Logo Showroom -->
        <a href="${pageContext.request.contextPath}/home" class="nav-logo">
            <span class="logo-gold">HYPER</span>CAR
        </a>

        <!-- Menu điều hướng chính -->
        <ul class="nav-menu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/home" class="nav-link">Trang Chủ</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/cars" class="nav-link">Bộ Sưu Tập Xe</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/test-drive" class="nav-link">Lái Thử F1</a>
            </li>
        </ul>

        <!-- Thanh Tìm Kiếm Realtime AJAX Live Search -->
        <div class="nav-search-container">
            <div class="search-box">
                <input type="text" id="liveSearchInput" class="search-input" 
                       placeholder="Tìm kiếm siêu xe... (vd: Bugatti, Ferrari)" 
                       autocomplete="off">
                <span class="search-icon">🔍</span>
            </div>
            <!-- Dropdown danh sách kết quả AJAX -->
            <div id="liveSearchResults" class="search-dropdown" style="display: none;"></div>
        </div>

        <!-- Khu vực Tài khoản & Giỏ xe đặt cọc -->
        <div class="nav-actions">
            <!-- Nút Giỏ Hàng -->
            <a href="${pageContext.request.contextPath}/cart" class="cart-btn" title="Giỏ hàng đặt cọc">
                🛒
                <span id="cartBadge" class="cart-badge">
                    ${not empty sessionScope.cart ? sessionScope.cart.totalQuantity : 0}
                </span>
            </a>

            <!-- Trạng thái Đăng nhập -->
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <div class="user-dropdown">
                        <button class="btn btn-outline btn-sm user-dropdown-btn">
                            👤 ${sessionScope.user.fullName} ▼
                        </button>
                        <div class="dropdown-menu">
                            <a href="${pageContext.request.contextPath}/profile" class="dropdown-item">👤 Hồ Sơ Cá Nhân</a>
                            <a href="${pageContext.request.contextPath}/order-history" class="dropdown-item">📜 Lịch Sử Đặt Cọc</a>
                            <a href="${pageContext.request.contextPath}/test-drive" class="dropdown-item">🏎️ Lịch Lái Thử</a>
                            
                            <c:if test="${sessionScope.user.isAdmin()}">
                                <div class="dropdown-divider"></div>
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="dropdown-item admin-link">
                                    ⚙️ Bàn Quản Trị (Admin)
                                </a>
                            </c:if>
                            
                            <div class="dropdown-divider"></div>
                            <a href="${pageContext.request.contextPath}/logout" class="dropdown-item logout-link">🚪 Đăng Xuất</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="auth-buttons">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Đăng Nhập</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-gold btn-sm">Đăng Ký</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>
