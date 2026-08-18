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

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/brands");
            return;
        }

        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String brandName = ValidationUtil.sanitize(request.getParameter("brandName"));
            String country = ValidationUtil.sanitize(request.getParameter("country"));
            String logoUrl = ValidationUtil.sanitize(request.getParameter("logoUrl"));
            String desc = ValidationUtil.sanitize(request.getParameter("description"));

            Brand b = new Brand();
            b.setBrandName(brandName);
            b.setCountry(country);
            b.setLogoUrl(logoUrl);
            b.setDescription(desc);
            brandDAO.insertBrand(b);
            session.setAttribute("successMessage", "Thêm hãng xe thành công!");

        } else if ("update".equals(action)) {
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            String brandName = ValidationUtil.sanitize(request.getParameter("brandName"));
            String country = ValidationUtil.sanitize(request.getParameter("country"));
            String logoUrl = ValidationUtil.sanitize(request.getParameter("logoUrl"));
            String desc = ValidationUtil.sanitize(request.getParameter("description"));

            Brand b = new Brand();
            b.setBrandId(brandId);
            b.setBrandName(brandName);
            b.setCountry(country);
            b.setLogoUrl(logoUrl);
            b.setDescription(desc);
            brandDAO.updateBrand(b);
            session.setAttribute("successMessage", "Cập nhật hãng xe thành công!");

        } else if ("delete".equals(action)) {
            int brandId = Integer.parseInt(request.getParameter("id"));
            brandDAO.deleteBrand(brandId);
            session.setAttribute("successMessage", "Xóa hãng xe thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/admin/brands");
    }
}
