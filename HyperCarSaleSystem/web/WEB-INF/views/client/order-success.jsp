<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Ký Hợp Đồng Đặt Cọc Thành Công" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container section">
    <div class="success-wrapper">
        <div class="card p-5 success-card text-center">
            <!-- Icon chúc mừng -->
            <div class="success-icon-wrap">
                🏆
            </div>

            <span class="badge badge-gold font-sm">GIAO DỊCH THÀNH CÔNG</span>
            <h1 class="success-title mt-2">CHÚC MỪNG QUÝ KHÁCH ĐÃ KÝ CỌC THÀNH CÔNG!</h1>
            <p class="success-desc">
                Cảm ơn quý khách <strong>${order.userFullName}</strong> đã tin tưởng lựa chọn kiệt tác siêu xe độc bản tại HyperCar Showroom.
            </p>

            <!-- Hộp mã hợp đồng độc bản -->
            <div class="order-code-box my-4 p-4">
                <span class="code-label">MÃ HỢP ĐỒNG ĐỘC BẢN:</span>
                <div class="order-code-val text-gold font-bold">${order.orderCode}</div>
                <div class="font-sm text-muted mt-1">
                    Thời gian khởi tạo: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                </div>
            </div>

            <!-- Bảng tóm tắt tiền cọc -->
            <div class="success-summary-box p-4 mb-4">
                <div class="row">
                    <div class="col-6 text-left">
                        <span class="text-muted">Tổng Tiền Cọc Đã Ký (10%):</span>
                        <div class="text-gold font-bold text-xl">
                            <fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </div>
                    </div>
                    <div class="col-6 text-right">
                        <span class="text-muted">Hình Thức Thanh Toán:</span>
                        <div class="font-bold text-lg">${order.paymentMethod}</div>
                    </div>
                </div>

                <div class="summary-divider my-3"></div>

                <div class="bank-instruction-note text-left font-sm">
                    💡 <strong>Hướng dẫn tiếp theo:</strong> Chuyên viên Concierge cao cấp của Showroom sẽ liên hệ trực tiếp với quý khách qua số điện thoại <strong>${order.phone}</strong> trong vòng 15 phút để xác nhận thủ tục bàn giao và tiến độ vận chuyển.
                </div>
            </div>

            <!-- Các nút điều hướng -->
            <div class="success-actions">
                <a href="${pageContext.request.contextPath}/order-detail?code=${order.orderCode}" class="btn btn-gold btn-lg">
                    📜 XEM HÓA ĐƠN ĐIỆN TỬ
                </a>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline btn-lg ml-3">
                    🌐 VỀ TRANG CHỦ SHOWROOM
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
