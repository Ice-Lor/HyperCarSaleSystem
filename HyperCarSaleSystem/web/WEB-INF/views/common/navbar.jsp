<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top border-bottom border-secondary border-opacity-25" style="background-color: rgba(10, 10, 12, 0.95) !important; backdrop-filter: blur(10px);">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-speedometer2 gold-text fs-3 me-2"></i>
            <span class="font-brand fw-bold fs-4 gold-gradient-text">HYPER<span class="text-white">CAR</span></span>
        </a>

        <!-- Mobile Toggle -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Nav Links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">Bộ Sưu Tập Siêu Xe</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-warning fw-semibold" href="${pageContext.request.contextPath}/test-drive">
                        <i class="bi bi-flag-fill me-1"></i> Lái Thử VIP Track
                    </a>
                </li>
            </ul>

            <!-- Live Search Bar -->
            <div class="position-relative me-lg-3 my-2 my-lg-0" style="min-width: 260px;">
                <div class="input-group">
                    <input type="text" id="globalSearchInput" class="form-control form-control-dark form-control-sm" 
                           placeholder="Tìm kiếm siêu xe..." autocomplete="off">
                    <span class="input-group-text bg-dark border-secondary text-muted">
                        <i class="bi bi-search"></i>
                    </span>
                </div>
                <div id="searchResults"></div>
            </div>

            <!-- Right Actions -->
            <div class="d-flex align-items-center gap-3">
                <!-- Cart Button -->
                <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-light btn-sm position-relative">
                    <i class="bi bi-cart3"></i>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">
                        ${sessionScope.cart != null ? sessionScope.cart.totalItemCount : 0}
                    </span>
                </a>

                <!-- User Account / Login -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <div class="dropdown">
                            <button class="btn btn-outline-gold btn-sm dropdown-toggle d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i>
                                <span>${sessionScope.currentUser.fullName}</span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-dark dropdown-menu-end shadow">
                                <c:if test="${sessionScope.currentUser.admin}">
                                    <li>
                                        <a class="dropdown-item text-warning fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer me-2"></i> Quản Trị Hệ Thống
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person-gear me-2"></i> Hồ Sơ Cá Nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                        <i class="bi bi-receipt me-2"></i> Hợp Đồng Đặt Cọc
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/test-drive">
                                        <i class="bi bi-calendar2-check me-2"></i> Lịch Hẹn Lái Thử
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Đăng Xuất
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">Đăng Nhập</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-gold btn-sm">Gia Nhập VIP</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

<!-- Global Toast/Alert Component -->
<c:if test="${not empty sessionScope.toastMessage}">
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1100;">
        <div class="toast align-items-center text-bg-success border-0 show" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.toastMessage}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    </div>
    <c:remove var="toastMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1100;">
        <div class="toast align-items-center text-bg-danger border-0 show" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.errorMessage}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>
