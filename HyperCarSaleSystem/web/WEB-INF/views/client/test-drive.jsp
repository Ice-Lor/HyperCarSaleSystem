<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Đăng Ký Lái Thử Trường Đua F1" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="page-header">
    <div class="container">
        <h1 class="page-title">ĐĂNG KÝ <span class="text-gold">LÁI THỬ TRƯỜNG ĐUA F1</span></h1>
        <p class="page-subtitle">Trải nghiệm cảm giác phấn khích tột độ khi cầm lái những cỗ máy 1000+ mã lực trên đường đua quốc tế</p>
    </div>
</div>

<div class="container section">
    <c:if test="${param.success == '1'}">
        <div class="alert alert-success mb-4">
            ✓ Đăng ký lịch lái thử thành công! Chuyên viên Showroom sẽ liên hệ xác nhận trong vòng 24 giờ.
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger mb-4">${error}</div>
    </c:if>

    <div class="test-drive-layout">
        <!-- CỘT TRÁI: FORM ĐĂNG KÝ LÁI THỬ -->
        <div class="test-drive-form-col">
            <div class="card p-4">
                <h3 class="card-title">ĐẶT LỊCH TRẢI NGHIỆM ĐƯỜNG ĐUA</h3>

                <form action="${pageContext.request.contextPath}/test-drive" method="POST">
                    <input type="hidden" name="csrf_token" value="${csrfToken}" />

                    <!-- Chọn siêu xe lái thử -->
                    <div class="form-group">
                        <label class="form-label">Chọn Mẫu Siêu Xe Lái Thử <span class="text-danger">*</span></label>
                        <select name="carId" class="form-control" required>
                            <option value="">-- Chọn siêu xe quý khách muốn trải nghiệm --</option>
                            <c:forEach var="c" items="${availableCars}">
                                <option value="${c.carId}" ${preselectedCarId == c.carId ? 'selected' : ''}>
                                    ${c.brandName} - ${c.modelName} (${c.horsepower} HP | Tăng tốc ${c.acceleration0100}s)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Ngày và Khung giờ -->
                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Ngày Trải Nghiệm <span class="text-danger">*</span></label>
                            <input type="date" name="bookingDate" class="form-control" required>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Khung Giờ Lái Thử <span class="text-danger">*</span></label>
                            <select name="timeSlot" class="form-control" required>
                                <option value="09:00 - 11:00">09:00 - 11:00 (Sáng)</option>
                                <option value="14:00 - 16:00">14:00 - 16:00 (Chiều)</option>
                                <option value="16:30 - 18:30">16:30 - 18:30 (Hoàng Hôn VIP)</option>
                            </select>
                        </div>
                    </div>

                    <!-- Trường đua & Bằng lái -->
                    <div class="form-row">
                        <div class="form-group col-6">
                            <label class="form-label">Địa Điểm Trường Đua <span class="text-danger">*</span></label>
                            <select name="locationTrack" class="form-control" required>
                                <option value="Trường đua F1 Hà Nội (Hanoi Street Circuit)">Trường đua F1 Hà Nội (Hanoi Street Circuit)</option>
                                <option value="Trường đua Quốc tế Đại Nam (Bình Dương)">Trường đua Quốc tế Đại Nam (Bình Dương)</option>
                                <option value="Đường thử chuyên dụng Showroom Sala (TP.HCM)">Đường thử chuyên dụng Showroom Sala (TP.HCM)</option>
                            </select>
                        </div>
                        <div class="form-group col-6">
                            <label class="form-label">Số Giấy Phép Lái Xe (Bằng B2/Quốc Tế) <span class="text-danger">*</span></label>
                            <input type="text" name="driverLicenseNumber" class="form-control" 
                                   placeholder="vd: 790123456789" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Ghi Chú Yêu Cầu Riêng Cho Ban Tổ Chức:</label>
                        <textarea name="note" class="form-control" rows="2" 
                                  placeholder="Yêu cầu hướng dẫn viên tay đua F1 chuyên nghiệp, ghi hình flycam 4K..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-gold btn-lg btn-block">
                        🏁 XÁC NHẬN ĐĂNG KÝ LÁI THỬ TRACK F1
                    </button>
                </form>
            </div>
        </div>

        <!-- CỘT PHẢI: LỊCH SỬ ĐĂNG KÝ CỦA KHÁCH -->
        <div class="test-drive-history-col">
            <div class="card p-4">
                <h3 class="card-title">LỊCH LÁI THỬ ĐÃ ĐĂNG KÝ</h3>

                <c:choose>
                    <c:when test="${not empty myBookings}">
                        <div class="booking-list">
                            <c:forEach var="b" items="${myBookings}">
                                <div class="booking-item card p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span class="font-bold text-gold">${b.carModelName}</span>
                                        <c:choose>
                                            <c:when test="${b.status == 'PENDING'}">
                                                <span class="badge badge-warning">⏳ Chờ duyệt</span>
                                            </c:when>
                                            <c:when test="${b.status == 'CONFIRMED'}">
                                                <span class="badge badge-success">✓ Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${b.status == 'COMPLETED'}">
                                                <span class="badge badge-info">🏆 Đã hoàn thành</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">❌ Đã hủy</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="font-sm text-muted">
                                        📅 <strong>Ngày:</strong> <fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy"/> (${b.timeSlot})
                                    </div>
                                    <div class="font-sm text-muted mt-1">
                                        🏁 <strong>Trường đua:</strong> ${b.locationTrack}
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted font-sm">Quý khách chưa có lịch hẹn lái thử nào.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
