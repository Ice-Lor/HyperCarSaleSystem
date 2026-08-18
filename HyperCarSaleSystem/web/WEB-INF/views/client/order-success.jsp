<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Đặt Cọc Thành Công - HyperCar Sale System"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

<div class="container py-5 text-center">
    <div class="row justify-content-center">
        <div class="col-md-7">
            <div class="card card-luxury p-5 my-4">
                <div class="mb-4">
                    <i class="bi bi-patch-check-fill text-gold" style="font-size: 4rem;"></i>
                </div>

                <h2 class="fw-bold text-light mb-2" style="font-family: 'Cinzel', serif;">ĐẶT CỌC THÀNH CÔNG!</h2>
                <p style="color: #b0b3c0;">Chúc mừng quý khách đã hoàn tất thủ tục giữ chỗ siêu phẩm Megacar / Hypercar độc bản.</p>

                <div class="p-3 rounded bg-surface my-4 text-start" style="background-color: #12131b;">
                    <div class="row g-2 small">
                        <div class="col-6" style="color: #b0b3c0;">Mã hợp đồng:</div>
                        <div class="col-6 text-end fw-bold text-gold">${orderCode}</div>
                        <div class="col-6" style="color: #b0b3c0;">Trạng thái hợp đồng:</div>
                        <div class="col-6 text-end"><span class="badge bg-warning text-dark">Chờ Xác Nhận Cọc</span></div>
                        <div class="col-6" style="color: #b0b3c0;">Thời gian tạo:</div>
                        <div class="col-6 text-end text-light">Ngay bây giờ</div>
                    </div>
                </div>

                <p class="small mb-4" style="color: #b0b3c0;">
                    Giám đốc phụ trách dịch vụ khách hàng VIP của HyperCar sẽ liên hệ với quý khách trong vòng 15 phút để hoàn tất thủ tục bàn giao và giao kết hợp đồng gốc.
                </p>

                <div class="d-flex justify-content-center gap-3">
                    <a href="${pageContext.request.contextPath}/order-history" class="btn btn-gold px-4">
                        <i class="bi bi-receipt me-1"></i> Xem Hợp Đồng Của Tôi
                    </a>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-gold px-4">
                        <i class="bi bi-house-door-fill me-1"></i> Về Trang Chủ
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
