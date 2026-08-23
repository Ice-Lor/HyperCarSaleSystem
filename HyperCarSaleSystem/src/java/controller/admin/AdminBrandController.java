package controller.admin;

import dal.ActivityLogDAO;
import dal.BrandDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Brand;
import model.User;
import util.ValidationUtil;

/**
 * Controller Quản lý Thương hiệu siêu xe (Admin Brand Management).
 */
@WebServlet(name = "AdminBrandController", urlPatterns = {"/admin/brands"})
public class AdminBrandController extends HttpServlet {

    private final BrandDAO brandDAO = new BrandDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

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

        String action = request.getParameter("action");
        if (!ValidationUtil.isNotEmpty(action)) {
            action = "insert";
        }

        // 1. Xử lý Xóa thương hiệu (POST an toàn chống CSRF và Web Prefetch)
        if ("delete".equalsIgnoreCase(action)) {
            int deleteId = ValidationUtil.parseInt(request.getParameter("id"), 0);
            if (deleteId > 0) {
                brandDAO.deleteBrand(deleteId);
                logUserAction(request, "DELETE_BRAND", "Admin xóa thương hiệu ID: " + deleteId);
            }
            response.sendRedirect(request.getContextPath() + "/admin/brands?msg=deleted");
            return;
        }

        // 2. Xử lý Thêm mới hoặc Cập nhật thương hiệu
        int brandId = ValidationUtil.parseInt(request.getParameter("brandId"), 0);
        String brandName = ValidationUtil.sanitize(request.getParameter("brandName"));
        String country = ValidationUtil.sanitize(request.getParameter("country"));
        String logoUrl = ValidationUtil.sanitize(request.getParameter("logoUrl"));
        String description = ValidationUtil.sanitize(request.getParameter("description"));

        if (!ValidationUtil.isNotEmpty(brandName)) {
            response.sendRedirect(request.getContextPath() + "/admin/brands?error=name_required");
            return;
        }

        // Kiểm tra trùng tên thương hiệu
        if (brandDAO.checkBrandNameExists(brandName, brandId)) {
            response.sendRedirect(request.getContextPath() + "/admin/brands?error=name_exists");
            return;
        }

        Brand brand = new Brand();
        brand.setBrandName(brandName);
        brand.setCountry(country);
        brand.setLogoUrl(logoUrl);
        brand.setDescription(description);

        if ("edit".equalsIgnoreCase(action) && brandId > 0) {
            brand.setBrandId(brandId);
            brandDAO.updateBrand(brand);
            logUserAction(request, "UPDATE_BRAND", "Admin cập nhật thương hiệu: " + brandName);
        } else {
            brandDAO.insertBrand(brand);
            logUserAction(request, "CREATE_BRAND", "Admin thêm thương hiệu mới: " + brandName);
        }

        response.sendRedirect(request.getContextPath() + "/admin/brands?msg=saved");
    }

    private void logUserAction(HttpServletRequest request, String action, String details) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            logDAO.log(user.getUserId(), action, details);
        }
    }
}
