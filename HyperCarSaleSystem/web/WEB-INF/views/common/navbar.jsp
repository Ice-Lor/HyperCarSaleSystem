<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-dark navbar-luxury sticky-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-shield-fill-check text-gold fs-3 me-2"></i>
            <span class="fw-bold tracking-wide" style="font-family: 'Cinzel', serif; letter-spacing: 2px;">HYPER<span class="text-gold">CAR</span></span>
        </a>
        
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <!-- Search Bar với AJAX Live Search -->
            <div class="mx-auto my-2 my-lg-0 position-relative" style="width: 100%; max-width: 400px;">
                <div class="input-group">
                    <span class="input-group-text bg-dark border-secondary text-gold"><i class="bi bi-search"></i></span>
                    <input type="text" id="globalSearchInput" class="form-control bg-dark border-secondary text-light" placeholder="Tìm kiếm Bugatti, Ferrari, Koenigsegg..." autocomplete="off">
                </div>
                <div id="searchResultsDropdown" class="search-results-dropdown"></div>
            </div>

            <ul class="navbar-nav align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/cars' ? 'active' : ''}" href="${pageContext.request.contextPath}/cars">
                        <i class="bi bi-grid-3x3-gap me-1"></i> Bộ Sưu Tập Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/test-drive' ? 'active' : ''}" href="${pageContext.request.contextPath}/test-drive">
                        <i class="bi bi-speedometer2 text-gold me-1"></i> Lái Thử VIP
                    </a>
                </li>
                
                <!-- Giỏ xe VIP -->
                <li class="nav-item me-lg-3">
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart">
                        <i class="bi bi-bag-fill fs-5"></i>
                        <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                            <span class="position-absolute top-1 start-100 translate-middle badge rounded-pill bg-gold text-dark">
                                ${sessionScope.cart.totalQuantity}
                            </span>
                        </c:if>
                    </a>
                </li>

                <!-- User Session Menu -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle fs-5 me-1 text-gold"></i>
                                <span>${sessionScope.currentUser.fullName != null ? sessionScope.currentUser.fullName : sessionScope.currentUser.username}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark bg-surface border-secondary shadow">
                                <c:if test="${sessionScope.currentUser.roleName == 'ADMIN'}">
                                    <li>
                                        <a class="dropdown-item text-gold fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer me-2"></i> Bàn Quản Trị (Admin)
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider border-secondary"></li>
                                </c:if>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person-lines-fill me-2"></i> Hồ Sơ Cá Nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/order-history">
                                        <i class="bi bi-receipt me-2"></i> Hợp Đồng Đặt Cọc
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/test-drive">
                                        <i class="bi bi-calendar2-check me-2"></i> Lịch Lái Thử F1
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider border-secondary"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Đăng Xuất
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="btn btn-outline-gold btn-sm px-3 me-2" href="${pageContext.request.contextPath}/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Đăng Nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-gold btn-sm px-3" href="${pageContext.request.contextPath}/register">
                                <i class="bi bi-person-plus me-1"></i> Gia Nhập VIP
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
