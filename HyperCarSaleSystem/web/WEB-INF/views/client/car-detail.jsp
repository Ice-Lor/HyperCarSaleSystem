<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${car.modelName} - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-secondary text-decoration-none">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cars" class="text-secondary text-decoration-none">Siêu xe</a></li>
            <li class="breadcrumb-item active text-warning" aria-current="page">${car.modelName}</li>
        </ol>
    </nav>

    <div class="row g-5">
        <!-- Images & Gallery -->
        <div class="col-lg-7">
            <!-- Main Image -->
            <div class="hyper-card overflow-hidden mb-3">
                <img id="mainCarImage" src="${car.thumbnailUrl}" alt="${car.modelName}" class="w-100 img-fluid" style="max-height: 480px; object-fit: cover;">
            </div>

            <!-- Thumbnail Gallery -->
            <c:if test="${not empty gallery}">
                <div class="row g-2 mb-4">
                    <div class="col-3">
                        <img src="${car.thumbnailUrl}" class="img-thumbnail bg-dark border-secondary cursor-pointer w-100" 
                             style="height: 70px; object-fit: cover; cursor: pointer;"
                             onclick="document.getElementById('mainCarImage').src=this.src;">
                    </div>
                    <c:forEach var="img" items="${gallery}">
                        <div class="col-3">
                            <img src="${img.imageUrl}" class="img-thumbnail bg-dark border-secondary cursor-pointer w-100" 
                                 style="height: 70px; object-fit: cover; cursor: pointer;"
                                 onclick="document.getElementById('mainCarImage').src=this.src;" title="${img.caption}">
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <!-- Description -->
            <div class="hyper-card p-4 mb-4">
                <h5 class="fw-bold gold-text font-brand mb-3">
                    <i class="bi bi-info-circle-fill me-2"></i> TỔNG QUAN & NGHỆ THUẬT THIẾT KẾ
                </h5>
                <p class="text-secondary lh-lg mb-0">${car.description}</p>
            </div>

            <!-- Reviews Section -->
            <div class="hyper-card p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h5 class="fw-bold gold-text font-brand mb-0">
                        <i class="bi bi-chat-square-quote-fill me-2"></i> ĐÁNH GIÁ TỪ CHỦ SỞ HỮU (${reviews.size()})
                    </h5>
                    <div class="text-warning">
                        <span class="fs-4 fw-bold me-1">${avgRating}</span>
                        <i class="bi bi-star-fill"></i>
                    </div>
                </div>

                <!-- Add Review Form (if logged in) -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <form action="${pageContext.request.contextPath}/add-review" method="POST" class="mb-4 pb-4 border-bottom border-secondary border-opacity-25">
                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                            <input type="hidden" name="carId" value="${car.carId}">
                            
                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">MỨC ĐỘ HÀI LÒNG</label>
                                <select name="rating" class="form-select form-select-dark form-select-sm w-auto">
                                    <option value="5">⭐⭐⭐⭐⭐ Tuyệt hảo (5/5)</option>
                                    <option value="4">⭐⭐⭐⭐ Rất tốt (4/5)</option>
                                    <option value="3">⭐⭐⭐ Hài lòng (3/5)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">CẢM NHẬN LÁI & ĐÁNH GIÁ</label>
                                <textarea name="comment" rows="3" class="form-control form-control-dark" placeholder="Chia sẻ trải nghiệm vận hành cỗ máy này..." required></textarea>
                            </div>

                            <button type="submit" class="btn btn-gold btn-sm">
                                <i class="bi bi-send-fill me-1"></i> Gửi Đánh Giá
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-dark border border-secondary text-secondary small mb-4">
                            <i class="bi bi-box-arrow-in-right me-2"></i> <a href="${pageContext.request.contextPath}/login" class="text-warning fw-bold text-decoration-none">Đăng nhập</a> để gửi nhận xét và đánh giá cho siêu xe này.
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Reviews List -->
                <c:forEach var="rev" items="${reviews}">
                    <div class="mb-3 pb-3 border-bottom border-secondary border-opacity-25">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <div class="fw-bold text-white">${rev.userFullName}</div>
                            <div class="text-warning small">
                                <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                            </div>
                        </div>
                        <p class="text-secondary small mb-0">${rev.comment}</p>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Specs & Booking Box -->
        <div class="col-lg-5">
            <div class="hyper-card p-4 sticky-top" style="top: 90px;">
                <!-- Brand & Model Title -->
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="badge bg-dark border border-secondary text-warning text-uppercase">
                        ${car.brandName} • ${car.categoryName}
                    </span>
                    <span class="badge ${car.stockQuantity > 0 ? 'bg-success' : 'bg-danger'} text-uppercase">
                        ${car.stockQuantity > 0 ? 'Sẵn Sàng Giao Xe' : 'Hết Hàng'}
                    </span>
                </div>

                <h2 class="font-brand fw-bold text-white mb-3">${car.modelName}</h2>

                <!-- Price Box -->
                <div class="p-3 rounded mb-4" style="background: rgba(212, 175, 55, 0.1); border: 1px solid var(--gold-primary);">
                    <div class="text-secondary small">Giá niêm yết toàn cầu:</div>
                    <div class="fs-2 fw-bold text-warning font-brand">
                        <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                    </div>
                    <div class="text-light text-opacity-75 small mt-1">
                        Tiền đặt cọc giữ xe (${car.depositRate}%): 
                        <strong class="text-white">
                            <fmt:formatNumber value="${car.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0" />
                        </strong>
                    </div>
                </div>

                <!-- Technical Specs Table -->
                <h6 class="text-uppercase gold-text fw-bold mb-3 font-brand">Thông Số Kỹ Thuật Độc Bản</h6>
                <div class="table-responsive mb-4">
                    <table class="table table-dark table-sm table-borderless small mb-0">
                        <tbody>
                            <tr>
                                <td class="text-secondary">Động cơ:</td>
                                <td class="text-white fw-bold text-end">${car.engineSpec}</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Công suất cực đại:</td>
                                <td class="text-warning fw-bold text-end font-brand">${car.horsepower} HP</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Tăng tốc 0 - 100 km/h:</td>
                                <td class="text-white fw-bold text-end font-brand">${car.acceleration0100} giây</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Tốc độ tối đa:</td>
                                <td class="text-white fw-bold text-end font-brand">${car.topSpeed} km/h</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Năm sản xuất:</td>
                                <td class="text-white fw-bold text-end">${car.year}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Add To Cart / Place Deposit Form -->
                <form action="${pageContext.request.contextPath}/cart" method="POST" class="mb-3">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="carId" value="${car.carId}">

                    <!-- Paint Color Options -->
                    <c:if test="${not empty car.colorOptions}">
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">CHỌN MÀU SƠN BESPOKE</label>
                            <select name="color" class="form-select form-select-dark form-select-sm">
                                <c:forEach var="cOption" items="${car.colorOptions.split(',')}">
                                    <option value="${cOption.trim()}">${cOption.trim()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:if>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-gold btn-lg" ${car.stockQuantity <= 0 ? 'disabled' : ''}>
                            <i class="bi bi-bag-check-fill me-2"></i> ĐẶT CỌC SIÊU XE NÀY
                        </button>
                        <a href="${pageContext.request.contextPath}/test-drive?carId=${car.carId}" class="btn btn-outline-gold">
                            <i class="bi bi-flag-fill me-2"></i> Đăng Ký Lái Thử VIP Track
                        </a>
                    </div>
                </form>

                <div class="text-center text-secondary small">
                    <i class="bi bi-telephone-inbound-fill me-1"></i> Liên hệ cố vấn VIP: <strong>1900 8888</strong>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
