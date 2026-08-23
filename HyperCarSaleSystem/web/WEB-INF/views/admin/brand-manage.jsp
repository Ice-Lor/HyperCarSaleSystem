<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản Lý Hãng Xe" scope="request"/>
<jsp:include page="../common/header.jsp" />

<div class="admin-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="active" value="brands" />
    </jsp:include>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1 class="admin-page-title">QUẢN LÝ THƯƠNG HIỆU SIÊU XE</h1>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div class="alert alert-success">✓ Lưu thông tin thương hiệu thành công!</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div class="alert alert-info">✓ Đã xóa thương hiệu.</div>
        </c:if>
        <c:if test="${param.error == 'name_exists'}">
            <div class="alert alert-danger">Tên thương hiệu đã tồn tại trong hệ thống!</div>
        </c:if>

        <div class="row mt-3">
            <!-- CỘT TRÁI: FORM THÊM / SỬA HÃNG -->
            <div class="col-4">
                <div class="card p-4">
                    <h3 class="card-title">Thêm / Sửa Thương Hiệu</h3>
                    <form action="${pageContext.request.contextPath}/admin/brands" method="POST">
                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                        <input type="hidden" name="action" value="insert" />

                        <div class="form-group">
                            <label class="form-label">Tên Hãng Xe <span class="text-danger">*</span></label>
                            <input type="text" name="brandName" class="form-control" 
                                   placeholder="vd: Bugatti" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Quốc Gia Xuất Xứ <span class="text-danger">*</span></label>
                            <input type="text" name="country" class="form-control" 
                                   placeholder="vd: Pháp (France)" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Đường Dẫn Logo (Local Path):</label>
                            <input type="text" name="logoUrl" class="form-control" 
                                   placeholder="assets/images/brands/bugatti.jpg">
                        </div>

                        <div class="form-group">
                            <label class="form-label">Mô Tả Lịch Sử Thương Hiệu:</label>
                            <textarea name="description" class="form-control" rows="3" 
                                      placeholder="Hãng siêu xe độc bản thành lập năm 1909..."></textarea>
                        </div>

                        <button type="submit" class="btn btn-gold btn-block">💾 LƯU THƯƠNG HIỆU</button>
                    </form>
                </div>
            </div>

            <!-- CỘT PHẢI: BẢNG DANH SÁCH CÁC HÃNG -->
            <div class="col-8">
                <div class="card p-4">
                    <h3 class="card-title">Danh Sách 8 Thương Hiệu Chính Thức</h3>
                    <table class="table admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Logo</th>
                                <th>Tên Hãng</th>
                                <th>Quốc Gia</th>
                                <th>Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="b" items="${brands}">
                                <tr>
                                    <td>${b.brandId}</td>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/${b.logoUrl}" 
                                             alt="${b.brandName}" class="brand-logo-table">
                                    </td>
                                    <td class="font-bold text-gold">${b.brandName}</td>
                                    <td>${b.country}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/brands" method="POST" 
                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa thương hiệu này?')">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id" value="${b.brandId}" />
                                            <button type="submit" class="btn btn-outline text-danger btn-sm">🗑️ Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/main.js" charset="UTF-8"></script>
</body>
</html>
