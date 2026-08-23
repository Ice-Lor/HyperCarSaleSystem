<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- CHÂN TRANG THƯƠNG HIỆU SHOWROOM (LUXURY FOOTER) -->
<footer class="footer">
    <div class="container footer-container">
        <!-- Cột 1: Thông tin Showroom Độc Bản -->
        <div class="footer-col">
            <h3 class="footer-logo"><span class="logo-gold">HYPER</span>CAR SHOWROOM</h3>
            <p class="footer-desc">
                Hệ thống phân phối và nhập khẩu siêu xe độc bản hàng đầu Châu Á. Nơi hội tụ những kiệt tác cơ khí đỉnh cao dành riêng cho giới thượng lưu.
            </p>
            <div class="footer-contact">
                <p>📍 <strong>Trụ sở chính:</strong> Tòa nhà Landmark 81, Vinhomes Central Park, TP. Hồ Chí Minh</p>
                <p>📍 <strong>Showroom Trưng Bày:</strong> Khu Đô Thị Sala, TP. Thủ Đức, TP. HCM</p>
                <p>📞 <strong>Hotline 24/7:</strong> 1900 8888 - 0988 888 888</p>
                <p>✉️ <strong>Email Concierge:</strong> contact@hypercars.vn</p>
            </div>
        </div>

        <!-- Cột 2: Các Thương Hiệu Độc Quyền -->
        <div class="footer-col">
            <h4 class="footer-heading">Thương Hiệu Siêu Xe</h4>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/cars?brandId=1">Bugatti Automobiles</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=2">Scuderia Ferrari</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=3">Automobili Lamborghini</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=4">Koenigsegg Megacars</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=5">McLaren Racing</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=6">Pagani Automobili</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=7">Porsche Motorsport</a></li>
                <li><a href="${pageContext.request.contextPath}/cars?brandId=8">Rimac Automobili</a></li>
            </ul>
        </div>

        <!-- Cột 3: Dịch Vụ & Đặc Quyền Khách Hàng -->
        <div class="footer-col">
            <h4 class="footer-heading">Đặc Quyền Khách Hàng</h4>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/test-drive">Đăng Ký Lái Thử Track F1</a></li>
                <li><a href="${pageContext.request.contextPath}/cars">Bộ Sưu Tập Giới Hạn</a></li>
                <li><a href="${pageContext.request.contextPath}/cart">Hợp Đồng Đặt Cọc Trực Tuyến</a></li>
                <li><a href="#">Bảo Hiểm Siêu Xe Hạng A+</a></li>
                <li><a href="#">Vận Chuyển Chuyên Dụng Bọc Kín</a></li>
            </ul>
        </div>
    </div>

    <!-- Dòng bản quyền dưới cùng -->
    <div class="footer-bottom">
        <div class="container footer-bottom-content">
            <p>&copy; 2026 HYPERCAR SALE SYSTEM. All Rights Reserved. Bản quyền thuộc về Hệ Thống Showroom Siêu Xe Độc Bản.</p>
            <p class="font-muted">Phát triển theo chuẩn kiến trúc MVC Java Web Servlet & JSP (PRJ301).</p>
        </div>
    </div>
</footer>

<!-- File JavaScript điều khiển AJAX Live Search, Live Cart & Validate Voucher -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
