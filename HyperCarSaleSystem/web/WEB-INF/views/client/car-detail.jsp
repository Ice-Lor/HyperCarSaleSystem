<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${car.modelName} - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="row g-5">
        <!-- Gallery Images -->
        <div class="col-lg-7">
            <div class="card card-luxury p-2 mb-3">
                <img id="mainCarImage" src="${pageContext.request.contextPath}/${car.thumbnailUrl}" class="w-100 rounded object-fit-cover" style="max-height: 480px;" alt="${car.modelName}">
            </div>
            
            <c:if test="${not empty gallery}">
                <div class="d-flex gap-2 overflow-auto pb-2">
                    <img src="${pageContext.request.contextPath}/${car.thumbnailUrl}" class="rounded border border-gold cursor-pointer" style="width: 100px; height: 65px; object-fit: cover;" 
                         onclick="document.getElementById('mainCarImage').src=this.src;">
                    <c:forEach items="${gallery}" var="img">
                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" class="rounded border border-secondary cursor-pointer opacity-75 hover-opacity-100" 
                             style="width: 100px; height: 65px; object-fit: cover;" 
                             onclick="document.getElementById('mainCarImage').src=this.src;">
                    </c:forEach>
                </div>
            </c:if>

            <!-- Specifications Table -->
            <div class="card card-luxury p-4 mt-4">
                <h5 class="fw-bold text-gold mb-3" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-cpu-fill me-2"></i> THÔNG SỐ KỸ THUẬT CHI TIẾT
                </h5>
                <div class="row g-3 small">
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Động cơ:</span>
                        <span class="fw-bold text-light">${car.engineSpec}</span>
                    </div>
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Công suất cực đại:</span>
                        <span class="fw-bold text-gold">${car.horsepower} Mã lực (HP)</span>
                    </div>
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Tăng tốc (0 - 100 km/h):</span>
                        <span class="fw-bold text-gold">${car.acceleration0100} Giây</span>
                    </div>
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Tốc độ tối đa:</span>
                        <span class="fw-bold text-gold">${car.topSpeed} km/h</span>
                    </div>
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Năm sản xuất:</span>
                        <span class="fw-bold text-light">${car.year}</span>
                    </div>
                    <div class="col-sm-6 d-flex justify-content-between border-bottom border-secondary pb-2">
                        <span style="color: #b0b3c0;">Tồn kho Showroom:</span>
                        <span class="badge ${car.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">${car.stockQuantity > 0 ? car.stockQuantity : 'Hết xe'} xe</span>
                    </div>
                </div>

                <div class="mt-4">
                    <h6 class="fw-bold text-light mb-2">Mô tả chi tiết:</h6>
                    <p class="small" style="color: #b0b3c0;">${car.description}</p>
                </div>
            </div>
        </div>

        <!-- Car Purchase & Deposit Box -->
        <div class="col-lg-5">
            <div class="card card-luxury p-4 sticky-top" style="top: 90px;">
                <span class="badge bg-gold text-dark align-self-start mb-2">${car.brandName} • ${car.brandCountry}</span>
                <h2 class="fw-bold text-light mb-2" style="font-family: 'Cinzel', serif;">${car.modelName}</h2>
                <div class="small mb-3" style="color: #b0b3c0;">Phân khúc: ${car.categoryName}</div>

                <div class="p-3 rounded bg-surface mb-4" style="background-color: #12131b;">
                    <div class="small" style="color: #b0b3c0;">Giá niêm yết chính hãng:</div>
                    <div class="display-6 fw-bold text-gold">
                        <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="$"/>
                    </div>
                    <div class="small mt-1" style="color: #b0b3c0;">
                        Mức đặt cọc giữ xe tối thiểu (${car.depositRate}%): 
                        <span class="text-light fw-bold">
                            <fmt:formatNumber value="${car.price * car.depositRate / 100}" type="currency" currencySymbol="$"/>
                        </span>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/cart" method="POST">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="carId" value="${car.carId}">

                    <!-- Custom Color -->
                    <div class="mb-3">
                        <label class="form-label small" style="color: #b0b3c0;">Tùy chọn màu sơn Bespoke:</label>
                        <select name="color" class="form-select bg-dark border-secondary text-light">
                            <c:forEach items="${car.colorOptions.split(',')}" var="c">
                                <option value="${c.trim()}">${c.trim()}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Custom Options -->
                    <div class="mb-4">
                        <label class="form-label small" style="color: #b0b3c0;">Gói phụ kiện độc bản (Tùy chọn):</label>
                        <textarea name="customOptions" class="form-control bg-dark border-secondary text-light" rows="2" 
                                  placeholder="Ví dụ: Nội thất bọc da Hermes, Vành mạ vàng 24K, Khắc tên cá nhân..."></textarea>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-gold py-3 fw-bold" ${car.stockQuantity <= 0 ? 'disabled' : ''}>
                            <i class="bi bi-bag-check-fill me-2"></i> ${car.stockQuantity > 0 ? 'Đặt Cọc Siêu Xe Ngay' : 'Tạm Hết Hàng'}
                        </button>
                        <a href="${pageContext.request.contextPath}/test-drive?carId=${car.carId}" class="btn btn-outline-gold py-2">
                            <i class="bi bi-speedometer2 me-2"></i> Đăng Ký Lái Thử Mẫu Xe Này
                        </a>
                    </div>
                </form>

                <div class="mt-4 pt-3 border-top border-secondary small" style="color: #b0b3c0;">
                    <div><i class="bi bi-shield-check text-gold me-2"></i> Cam kết bảo hành chính hãng toàn cầu</div>
                    <div><i class="bi bi-airplane-engines text-gold me-2"></i> Vận chuyển chuyên cơ giao xe tận nhà</div>
                </div>
            </div>
        </div>
    </div>

    <!-- Reviews Section -->
    <div class="row mt-5">
        <div class="col-lg-8">
            <div class="card card-luxury p-4">
                <h4 class="fw-bold text-gold mb-4" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-star-half me-2"></i> ĐÁNH GIÁ TỪ CHỦ SỞ HỮU (${reviews.size()})
                </h4>

                <c:if test="${not empty sessionScope.currentUser}">
                    <form action="${pageContext.request.contextPath}/review" method="POST" class="mb-4 pb-4 border-bottom border-secondary">
                        <input type="hidden" name="csrf_token" value="${csrfToken}">
                        <input type="hidden" name="carId" value="${car.carId}">

                        <div class="mb-3">
                            <label class="form-label small" style="color: #b0b3c0;">Mức độ hài lòng (1 - 5 Sao):</label>
                            <select name="rating" class="form-select bg-dark border-secondary text-gold w-auto">
                                <option value="5">⭐⭐⭐⭐⭐ (5/5 - Hoàn hảo)</option>
                                <option value="4">⭐⭐⭐⭐ (4/5 - Rất tốt)</option>
                                <option value="3">⭐⭐⭐ (3/5 - Bình thường)</option>
                                <option value="2">⭐⭐ (2/5 - Kém)</option>
                                <option value="1">⭐ (1/5 - Rất tệ)</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <textarea name="comment" class="form-control bg-dark border-secondary text-light" rows="3" 
                                      placeholder="Cảm nhận thực tế về sức mạnh và độ hoàn thiện của siêu xe..." required></textarea>
                        </div>

                        <button type="submit" class="btn btn-gold btn-sm px-3">
                            <i class="bi bi-send-fill me-1"></i> Gửi Đánh Giá VIP
                        </button>
                    </form>
                </c:if>

                <div class="d-flex flex-column gap-3">
                    <c:if test="${empty reviews}">
                        <div class="small text-center py-3" style="color: #b0b3c0;">Chưa có đánh giá nào cho mẫu xe này.</div>
                    </c:if>
                    <c:forEach items="${reviews}" var="r">
                        <div class="p-3 rounded bg-surface" style="background-color: #1a1c2b;">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <div class="fw-bold text-light">
                                    <i class="bi bi-person-circle text-gold me-1"></i> ${r.userFullName != null ? r.userFullName : r.username}
                                </div>
                                <span class="text-warning">
                                    <c:forEach begin="1" end="${r.rating}">★</c:forEach>
                                </span>
                            </div>
                            <p class="small mb-1" style="color: #b0b3c0;">${r.comment}</p>
                            <small style="font-size: 0.75rem; color: #b0b3c0;"><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
