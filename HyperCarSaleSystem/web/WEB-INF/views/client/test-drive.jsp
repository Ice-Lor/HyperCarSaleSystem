<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Đặt Lịch Lái Thử VIP - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5">
    <div class="mb-4">
        <h2 class="fw-bold" style="font-family: 'Cinzel', serif;">TRẢI NGHIỆM LÁI THỬ ĐƯỜNG ĐUA F1 VIP</h2>
        <p class="text-muted small">Cơ hội độc quyền cầm lái những cỗ máy tốc độ khủng nhất hành tinh dưới sự hướng dẫn của chuyên gia</p>
    </div>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success py-2 small mb-4">${sessionScope.successMessage}</div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger py-2 small mb-4">${sessionScope.errorMessage}</div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="row g-4">
        <!-- Booking Form -->
        <div class="col-lg-6">
            <div class="card card-luxury p-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-calendar-plus-fill me-2"></i> ĐĂNG KÝ LỊCH TRẢI NGHIỆM
                </h5>

                <form action="${pageContext.request.contextPath}/test-drive" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <div class="mb-3">
                        <label class="form-label text-muted small">Chọn siêu xe muốn trải nghiệm *</label>
                        <select name="carId" class="form-select bg-dark border-secondary text-light" required>
                            <c:forEach items="${cars}" var="c">
                                <option value="${c.carId}" ${selectedCar != null && selectedCar.carId == c.carId ? 'selected' : ''}>
                                    ${c.modelName} (${c.brandName} - ${c.horsepower} HP)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-muted small">Ngày lái thử *</label>
                            <input type="date" name="bookingDate" class="form-control bg-dark border-secondary text-light" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted small">Khung giờ *</label>
                            <select name="timeSlot" class="form-select bg-dark border-secondary text-light" required>
                                <option value="09:00 - 11:00">09:00 - 11:00 (Sáng)</option>
                                <option value="14:00 - 16:00">14:00 - 16:00 (Chiều)</option>
                                <option value="17:00 - 19:00">17:00 - 19:00 (Hoàng Hôn VIP)</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-muted small">Địa điểm đường đua *</label>
                        <select name="locationTrack" class="form-select bg-dark border-secondary text-light" required>
                            <option value="Đường Đua F1 Mỹ Đình, Hà Nội">Đường Đua F1 Quốc Tế Mỹ Đình (Hà Nội)</option>
                            <option value="Trường Đua Đại Nam, Bình Dương">Trường Đua Đại Nam Circuit (Bình Dương / TP.HCM)</option>
                            <option value="Sepang International Circuit, Malaysia">Đường Đua F1 Quốc Tế Sepang (Malaysia - Chuyên cơ đón)</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-muted small">Số bằng lái xe quốc tế / hợp lệ *</label>
                        <input type="text" name="driverLicenseNumber" class="form-control bg-dark border-secondary text-light" 
                               placeholder="B2 / C / Bằng lái quốc tế..." required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-muted small">Ghi chú thêm</label>
                        <textarea name="note" class="form-control bg-dark border-secondary text-light" rows="2" 
                                  placeholder="Yêu cầu riêng về chuyên cơ đón, trang phục đua xe..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-gold w-100 py-3 fw-bold">
                        <i class="bi bi-send-check-fill me-1"></i> Xác Nhận Đăng Ký Lái Thử
                    </button>
                </form>
            </div>
        </div>

        <!-- My Bookings History -->
        <div class="col-lg-6">
            <div class="card card-luxury p-4">
                <h5 class="fw-bold text-gold mb-3 border-bottom border-secondary pb-2" style="font-family: 'Cinzel', serif;">
                    <i class="bi bi-clock-history me-2"></i> LỊCH ĐÃ ĐĂNG KÝ CỦA TÔI
                </h5>

                <div class="d-flex flex-column gap-3">
                    <c:if test="${empty myBookings}">
                        <div class="text-center text-muted py-5">
                            <i class="bi bi-calendar2-x fs-2 d-block mb-2 text-gold"></i>
                            Đại ca chưa có lịch hẹn lái thử nào.
                        </div>
                    </c:if>
                    <c:forEach items="${myBookings}" var="b">
                        <div class="p-3 rounded bg-surface border border-secondary" style="background-color: #1a1c2b;">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <h6 class="fw-bold text-gold mb-0">${b.carModelName}</h6>
                                <span class="badge ${b.status == 'CONFIRMED' ? 'bg-success' : (b.status == 'CANCELLED' ? 'bg-danger' : 'bg-warning text-dark')}">
                                    ${b.status}
                                </span>
                            </div>
                            <div class="small text-light mb-1">
                                <i class="bi bi-calendar3 me-1 text-gold"></i> <fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy"/> (${b.timeSlot})
                            </div>
                            <div class="small text-muted mb-1">
                                <i class="bi bi-geo-alt me-1 text-gold"></i> ${b.locationTrack}
                            </div>
                            <c:if test="${not empty b.note}">
                                <div class="small text-muted" style="font-size: 0.75rem;">Ghi chú: ${b.note}</div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
