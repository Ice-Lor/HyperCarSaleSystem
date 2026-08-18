<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Đăng Ký Lái Thử VIP Track - HYPERCAR" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container py-5">
    <div class="text-center mb-5">
        <span class="badge bg-warning text-dark px-3 py-2 text-uppercase fw-bold mb-2 font-brand">
            <i class="bi bi-flag-fill me-1"></i> Đặc Quyền Thượng Lưu
        </span>
        <h2 class="font-brand fw-bold text-white">TRẢI NGHIỆM LÁI THỬ SIÊU XE TRÊN ĐƯỜNG ĐUA</h2>
        <p class="text-secondary mx-auto" style="max-width: 650px;">
            Đại ca sẽ được trực tiếp cầm lái siêu xe hàng nghìn mã lực trên các đường đua đạt chuẩn FIA quốc tế, dưới sự hướng dẫn 1-1 của tay đua chuyên nghiệp.
        </p>
    </div>

    <div class="row g-5">
        <!-- Booking Form -->
        <div class="col-lg-6">
            <div class="hyper-card p-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    <i class="bi bi-calendar2-plus-fill me-2"></i> THÔNG TIN ĐĂNG KÝ LÁI THỬ
                </h5>

                <form action="${pageContext.request.contextPath}/test-drive" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}">

                    <!-- Choose Car -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">CHỌN MẪU SIÊU XE MUỐN TRẢI NGHIỆM (*)</label>
                        <select name="carId" class="form-select form-select-dark" required>
                            <option value="">-- Chọn siêu xe --</option>
                            <c:forEach var="c" items="${carList}">
                                <option value="${c.carId}" ${selectedCar != null && selectedCar.carId == c.carId ? 'selected' : ''}>
                                    ${c.modelName} (${c.horsepower} HP - ${c.brandName})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Date & Time Slot -->
                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">NGÀY TRẢI NGHIỆM (*)</label>
                            <input type="date" name="bookingDate" class="form-control form-control-dark" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-secondary small fw-bold">KHUNG GIỜ (*)</label>
                            <select name="timeSlot" class="form-select form-select-dark" required>
                                <option value="09:00 - 11:00">09:00 - 11:00 (Buổi Sáng)</option>
                                <option value="14:00 - 16:00">14:00 - 16:00 (Buổi Chiều)</option>
                                <option value="16:30 - 18:30">16:30 - 18:30 (Hoàng Hôn)</option>
                            </select>
                        </div>
                    </div>

                    <!-- Track Location -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">ĐỊA ĐIỂM / TRƯỜNG ĐUA TRẢI NGHIỆM (*)</label>
                        <select name="locationTrack" class="form-select form-select-dark" required>
                            <option value="Đường đua F1 Hanoi Circuit, Mỹ Đình, Hà Nội">Đường đua F1 Hanoi Circuit (Mỹ Đình, Hà Nội)</option>
                            <option value="Đường đua Đại Nam Track, Bình Dương">Đường đua Đại Nam Track (Bình Dương / TP. HCM)</option>
                            <option value="Đường đua Sepang International Circuit (Chuyến bay VIP sang Malaysia)">Đường đua Sepang International Circuit (Malaysia - VIP Tour)</option>
                        </select>
                    </div>

                    <!-- Driver License -->
                    <div class="mb-3">
                        <label class="form-label text-secondary small fw-bold">SỐ BẰNG LÁI XE Ô TÔ / SUPER LICENSE (*)</label>
                        <input type="text" name="driverLicenseNumber" class="form-control form-control-dark" placeholder="VD: B2-99887766 hoặc FIA Super License" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-secondary small fw-bold">YÊU CẦU ĐẶC BIỆT KÈM THEO</label>
                        <textarea name="note" rows="3" class="form-control form-control-dark" placeholder="Ghi chú về thói quen lái, chế độ Launch Control, thiết bị ghi hình GoPro..."></textarea>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-gold btn-lg">
                            <i class="bi bi-send-check-fill me-2"></i> GỬI YÊU CẦU ĐẶT LỊCH VIP
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- History of Bookings -->
        <div class="col-lg-6">
            <div class="hyper-card p-4">
                <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                    <i class="bi bi-clock-history me-2"></i> LỊCH LÁI THỬ ĐÃ ĐĂNG KÝ
                </h5>

                <c:choose>
                    <c:when test="${empty myBookings}">
                        <div class="text-center py-5 text-secondary">
                            <i class="bi bi-calendar-x fs-1 mb-2 d-block"></i>
                            Đại ca chưa có lịch hẹn lái thử nào.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="b" items="${myBookings}">
                            <div class="p-3 rounded mb-3 border border-secondary border-opacity-25" style="background: #181822;">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <div class="fw-bold text-white fs-6">${b.carModelName}</div>
                                    <c:choose>
                                        <c:when test="${b.status == 'CONFIRMED'}"><span class="badge bg-success">ĐÃ XÁC NHẬN</span></c:when>
                                        <c:when test="${b.status == 'COMPLETED'}"><span class="badge bg-primary">ĐÃ HOÀN TẤT</span></c:when>
                                        <c:when test="${b.status == 'CANCELLED'}"><span class="badge bg-danger">ĐÃ HỦY</span></c:when>
                                        <c:otherwise><span class="badge bg-warning text-dark">CHỜ XÁC NHẬN</span></c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="small text-secondary mb-1">
                                    <i class="bi bi-calendar-event me-1 text-warning"></i> Ngày: <strong><fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy" /></strong> (${b.timeSlot})
                                </div>
                                <div class="small text-secondary mb-1">
                                    <i class="bi bi-geo-alt-fill me-1 text-warning"></i> Địa điểm: ${b.locationTrack}
                                </div>
                                <c:if test="${not empty b.note}">
                                    <div class="small text-muted mt-2 border-top border-secondary border-opacity-10 pt-1">
                                        <em>Ghi chú: ${b.note}</em>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
