<%@page contentType="text/html" pageEncoding="UTF-8"%>

<footer class="mt-auto py-5 border-top border-secondary bg-dark" style="border-color: #2a2d42 !important;">
    <div class="container">
        <div class="row g-4">
            <div class="col-lg-4">
                <h5 class="fw-bold text-gold" style="font-family: 'Cinzel', serif;">HYPERCAR SALE SYSTEM</h5>
                <p class="text-muted small">
                    Hệ sinh thái phân phối và bảo trợ các siêu phẩm Megacar, Hypercar đỉnh cao toàn cầu. Trải nghiệm thượng lưu, bảo mật tuyệt đối và dịch vụ chuẩn quốc tế.
                </p>
                <div class="d-flex gap-3 text-gold fs-5">
                    <i class="bi bi-facebook"></i>
                    <i class="bi bi-instagram"></i>
                    <i class="bi bi-youtube"></i>
                    <i class="bi bi-tiktok"></i>
                </div>
            </div>
            <div class="col-lg-2 col-6">
                <h6 class="text-light fw-bold mb-3">THƯƠNG HIỆU</h6>
                <ul class="list-unstyled text-muted small">
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cars?brandId=1" class="text-muted text-decoration-none hover-gold">Bugatti</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cars?brandId=2" class="text-muted text-decoration-none hover-gold">Koenigsegg</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cars?brandId=3" class="text-muted text-decoration-none hover-gold">Pagani</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cars?brandId=4" class="text-muted text-decoration-none hover-gold">Ferrari</a></li>
                </ul>
            </div>
            <div class="col-lg-2 col-6">
                <h6 class="text-light fw-bold mb-3">DỊCH VỤ VIP</h6>
                <ul class="list-unstyled text-muted small">
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/test-drive" class="text-muted text-decoration-none hover-gold">Trải Nghiệm Lái Thử</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cart" class="text-muted text-decoration-none hover-gold">Đặt Cọc Siêu Xe</a></li>
                    <li class="mb-2"><a href="#" class="text-muted text-decoration-none hover-gold">Tùy Biến Bespoke</a></li>
                    <li class="mb-2"><a href="#" class="text-muted text-decoration-none hover-gold">Bảo Hiểm Megacar</a></li>
                </ul>
            </div>
            <div class="col-lg-4">
                <h6 class="text-light fw-bold mb-3">SHOWROOM TRUNG TÂM</h6>
                <p class="text-muted small mb-2"><i class="bi bi-geo-alt-fill text-gold me-2"></i> Đường Đua F1 Mỹ Đình, Hà Nội & Quận 1, TP. Hồ Chí Minh</p>
                <p class="text-muted small mb-2"><i class="bi bi-telephone-fill text-gold me-2"></i> VIP Concierge Hotline: 1900 888 999</p>
                <p class="text-muted small"><i class="bi bi-envelope-fill text-gold me-2"></i> vip@hypercar.luxury</p>
            </div>
        </div>
        <hr class="border-secondary my-4">
        <div class="text-center text-muted small">
            &copy; 2026 HyperCarSaleSystem (PRJ301 Assignment). Kiến trúc MVC-V2 Java EE & SQL Server chuẩn 3NF.
        </div>
    </div>
</footer>

<!-- Bootstrap Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
