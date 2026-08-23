<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Chi Tiết Hợp Đồng #${order.orderCode}" scope="request"/>
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container section">
    <div class="invoice-wrapper">
        <div class="card p-5 invoice-card" id="printableInvoice">
            <!-- PHẦN HEADER HÓA ĐƠN ĐIỆN TỬ -->
            <div class="invoice-header">
                <div>
                    <h1 class="invoice-logo"><span class="logo-gold">HYPER</span>CAR SHOWROOM</h1>
                    <p class="font-sm text-muted">Hệ thống phân phối siêu xe độc bản Châu Á</p>
                    <p class="font-sm">📍 Tòa nhà Landmark 81, Vinhomes Central Park, TP.HCM</p>
                    <p class="font-sm">📞 Hotline VIP: 1900 8888</p>
                </div>
                <div class="text-right">
                    <h2 class="invoice-title text-gold">HỢP ĐỒNG ĐẶT CỌC</h2>
                    <div class="invoice-code font-bold font-lg">${order.orderCode}</div>
                    <div class="invoice-date text-muted">
                        Ngày lập: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                    <div class="mt-2">
                        <c:choose>
                            <c:when test="${order.status == 'PENDING'}">
                                <span class="badge badge-warning font-sm">⏳ CHỜ XÁC NHẬN CỌC</span>
                            </c:when>
                            <c:when test="${order.status == 'CONFIRMED'}">
                                <span class="badge badge-info font-sm">✓ ĐÃ XÁC NHẬN TIỀN CỌC</span>
                            </c:when>
                            <c:when test="${order.status == 'PROCESSING'}">
                                <span class="badge badge-primary font-sm">⚙️ ĐANG CHUẨN BỊ XE</span>
                            </c:when>
                            <c:when test="${order.status == 'COMPLETED'}">
                                <span class="badge badge-success font-sm">🏆 ĐÃ BÀN GIAO XE</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger font-sm">❌ ĐÃ HỦY</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="invoice-divider"></div>

            <!-- THÔNG TIN KHÁCH HÀNG BÀN GIAO -->
            <div class="invoice-info-grid">
                <div class="info-block">
                    <h4 class="info-title">THÔNG TIN KHÁCH HÀNG (BÊN B):</h4>
                    <p><strong>Họ và tên:</strong> ${order.userFullName}</p>
                    <p><strong>Tài khoản:</strong> ${order.username}</p>
                    <p><strong>Số điện thoại:</strong> ${order.phone}</p>
                </div>
                <div class="info-block">
                    <h4 class="info-title">ĐỊA ĐIỂM BÀN GIAO XE:</h4>
                    <p><strong>Địa chỉ:</strong> ${order.deliveryAddress}</p>
                    <p><strong>Hình thức thanh toán:</strong> ${order.paymentMethod}</p>
                    <c:if test="${not empty order.note}">
                        <p><strong>Yêu cầu đặc biệt:</strong> ${order.note}</p>
                    </c:if>
                </div>
            </div>

            <!-- DANH SÁCH CHI TIẾT SIÊU XE ĐẶT CỌC -->
            <table class="table invoice-table mt-4">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Siêu Xe Độc Bản</th>
                        <th>Màu Sơn Bespoke</th>
                        <th>Số Lượng</th>
                        <th>Đơn Giá Niêm Yết</th>
                        <th class="text-right">Tiền Đặt Cọc (10%)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="det" items="${order.orderDetails}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td class="font-bold">
                                ${det.carModelName}
                                <div class="font-sm text-muted">${det.carBrandName}</div>
                            </td>
                            <td>${det.selectedColor}</td>
                            <td>${det.quantity}</td>
                            <td>
                                <fmt:formatNumber value="${det.unitPrice}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                            <td class="text-right text-gold font-bold">
                                <fmt:formatNumber value="${det.depositPrice}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- BẢNG TÍNH TỔNG TIỀN -->
            <div class="invoice-totals-row">
                <div class="invoice-note-col">
                    <div class="bank-box p-3">
                        <h4 class="font-bold text-gold">THÔNG TIN CHUYỂN KHOẢN TIỀN CỌC:</h4>
                        <p>🏦 <strong>Ngân hàng:</strong> Vietcombank - CN Tân Định VIP</p>
                        <p>💳 <strong>Số tài khoản:</strong> 8888 9999 8888 (VND/USD)</p>
                        <p>👤 <strong>Chủ tài khoản:</strong> CTY CP SIÊU XE HYPERCAR VIETNAM</p>
                        <p>📝 <strong>Cú pháp CK:</strong> <span class="text-gold font-bold">COC ${order.orderCode}</span></p>
                    </div>
                </div>

                <div class="invoice-calc-col">
                    <div class="calc-row">
                        <span>Tổng giá trị niêm yết:</span>
                        <span class="font-bold">
                            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </span>
                    </div>

                    <c:if test="${not empty order.couponCode}">
                        <div class="calc-row text-success">
                            <span>Mã giảm giá (${order.couponCode}):</span>
                            <span class="font-bold">
                                -<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </span>
                        </div>
                    </c:if>

                    <div class="calc-row calc-total-row">
                        <span>TỔNG TIỀN CỌC ĐÃ KÝ:</span>
                        <span class="text-gold font-bold text-xl">
                            <fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                        </span>
                    </div>
                </div>
            </div>

            <!-- CHỮ KÝ BÊN A & BÊN B -->
            <div class="invoice-signatures mt-5">
                <div class="text-center">
                    <p class="font-bold">ĐẠI DIỆN KHÁCH HÀNG (BÊN B)</p>
                    <p class="font-sm text-muted">(Ký và ghi rõ họ tên)</p>
                    <div class="sig-space"></div>
                    <p class="font-bold">${order.userFullName}</p>
                </div>
                <div class="text-center">
                    <p class="font-bold">ĐẠI DIỆN SHOWROOM HYPERCAR (BÊN A)</p>
                    <p class="font-sm text-muted">(Đã ký đóng dấu điện tử)</p>
                    <div class="sig-stamp">👑 HYPERCAR SEAL</div>
                    <p class="font-bold">TỔNG GIÁM ĐỐC ĐIỀU HÀNH</p>
                </div>
            </div>
        </div>

        <!-- CÁC NÚT THAO TÁC -->
        <div class="invoice-actions mt-4 text-center">
            <button type="button" class="btn btn-gold btn-lg" onclick="window.print()">
                🖨️ IN HỢP ĐỒNG ĐIỆN TỬ
            </button>
            <a href="${pageContext.request.contextPath}/order-history" class="btn btn-outline btn-lg ml-3">
                📜 VỀ LỊCH SỬ ĐẶT CỌC
            </a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
