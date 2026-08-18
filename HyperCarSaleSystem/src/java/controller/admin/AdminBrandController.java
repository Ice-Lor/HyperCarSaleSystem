package controller.admin;

import dal.BrandDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Brand;
import util.CSRFUtil;
import util.ValidationUtil;

public class AdminBrandController extends HttpServlet {

    private final BrandDAO brandDAO = new BrandDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Brand> brands = brandDAO.getAllBrands();
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("/WEB-INF/views/admin/brand-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (!CSRFUtil.isValidToken(request)) {
            if (session != null) session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin/brands");
            return;
        }

        String action = request.getParameter("action");

        if ("save".equalsIgnoreCase(action)) {
            try {
                String idStr = request.getParameter("brandId");
                String brandName = ValidationUtil.sanitize(request.getParameter("brandName"));
                String country = ValidationUtil.sanitize(request.getParameter("country"));
                String logoUrl = request.getParameter("logoUrl");
                String description = ValidationUtil.sanitize(request.getParameter("description"));

                Brand b = new Brand();
                b.setBrandName(brandName);
                b.setCountry(country);
                b.setLogoUrl(logoUrl);
                b.setDescription(description);

                if (idStr != null && !idStr.trim().isEmpty() && !"0".equals(idStr.trim())) {
                    b.setBrandId(Integer.parseInt(idStr.trim()));
                    brandDAO.updateBrand(b);
                    if (session != null) session.setAttribute("toastMessage", "Cập nhật hãng xe thành công!");
                } else {
                    brandDAO.insertBrand(b);
                    if (session != null) session.setAttribute("toastMessage", "Thêm mới hãng xe thành công!");
                }
            } catch (Exception e) {
                if (session != null) session.setAttribute("errorMessage", "Lỗi dữ liệu: " + e.getMessage());
            }

        } else if ("delete".equalsIgnoreCase(action)) {
            try {
                int brandId = Integer.parseInt(request.getParameter("id"));
                brandDAO.deleteBrand(brandId);
                if (session != null) session.setAttribute("toastMessage", "Xóa hãng xe thành công!");
            } catch (Exception e) {
                if (session != null) session.setAttribute("errorMessage", "Không thể xóa hãng xe đang có sản phẩm!");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/brands");
    }
}
