<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thương Hiệu - HYPERCAR Admin" />
</jsp:include>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container-fluid px-4 py-4">
    <div class="row">
        <!-- Sidebar Navigation -->
        <c:set var="activeMenu" value="brands" scope="request" />
        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <!-- Main Content -->
        <div class="col-lg-9 col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="font-brand fw-bold text-white mb-1">QUẢN LÝ THƯƠNG HIỆU SIÊU XE</h3>
                    <p class="text-secondary small mb-0">Danh sách các hãng sản xuất và phân phối chính thức</p>
                </div>
            </div>

            <div class="row g-4">
                <!-- Add Brand Form -->
                <div class="col-lg-4">
                    <div class="hyper-card p-4">
                        <h5 class="fw-bold gold-text font-brand mb-3 pb-2 border-bottom border-secondary border-opacity-25">
                            THÊM HÃNG SIÊU XE
                        </h5>

                        <form action="${pageContext.request.contextPath}/admin/brands" method="POST">
                            <input type="hidden" name="csrf_token" value="${csrfToken}">
                            <input type="hidden" name="action" value="save">
                            <input type="hidden" name="brandId" value="0">

                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">TÊN THƯƠNG HIỆU (*)</label>
                                <input type="text" name="brandName" class="form-control form-control-dark" placeholder="VD: Aston Martin" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">QUỐC GIA (*)</label>
                                <input type="text" name="country" class="form-control form-control-dark" placeholder="VD: Anh Quốc, Ý, Đức" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">LINK LOGO HÃNG (URL)</label>
                                <input type="url" name="logoUrl" class="form-control form-control-dark" placeholder="https://..." required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-secondary small fw-bold">MÔ TẢ</label>
                                <textarea name="description" rows="3" class="form-control form-control-dark" placeholder="Lịch sử thương hiệu..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-gold btn-sm w-100">
                                <i class="bi bi-plus-circle me-1"></i> Lưu Thương Hiệu
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Brands List -->
                <div class="col-lg-8">
                    <div class="hyper-card p-4">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover table-dark-custom align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th>Logo</th>
                                        <th>Tên Hãng</th>
                                        <th>Quốc Gia</th>
                                        <th>Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="b" items="${brands}">
                                        <tr>
                                            <td>
                                                <img src="${b.logoUrl}" alt="${b.brandName}" class="rounded bg-dark p-1" style="max-height: 40px; max-width: 70px; object-fit: contain;">
                                            </td>
                                            <td class="fw-bold text-white">${b.brandName}</td>
                                            <td class="text-secondary">${b.country}</td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/admin/brands" method="POST" onsubmit="return confirm('Xác nhận xóa thương hiệu này?');" style="display:inline;">
                                                    <input type="hidden" name="csrf_token" value="${csrfToken}">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${b.brandId}">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
