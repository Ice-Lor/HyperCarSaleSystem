package controller.client;

import dal.BrandDAO;
import dal.CarDAO;
import dal.CategoryDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Brand;
import model.Car;
import model.Category;
import util.ValidationUtil;

/**
 * Controller Danh mục siêu xe (Showroom & Tìm kiếm đa tiêu chí kết hợp phân trang).
 */
@WebServlet(name = "CarListController", urlPatterns = {"/cars", "/car-list"})
public class CarListController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 6;

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Đọc và làm sạch các tham số lọc tìm kiếm
        String keyword = ValidationUtil.sanitize(request.getParameter("keyword"));
        
        String brandIdRaw = request.getParameter("brandId");
        Integer brandId = (ValidationUtil.isNotEmpty(brandIdRaw) && ValidationUtil.parseInt(brandIdRaw, 0) > 0)
                ? ValidationUtil.parseInt(brandIdRaw, 0) : null;

        String categoryIdRaw = request.getParameter("categoryId");
        Integer categoryId = (ValidationUtil.isNotEmpty(categoryIdRaw) && ValidationUtil.parseInt(categoryIdRaw, 0) > 0)
                ? ValidationUtil.parseInt(categoryIdRaw, 0) : null;

        String minPriceRaw = request.getParameter("minPrice");
        BigDecimal minPrice = (ValidationUtil.isNotEmpty(minPriceRaw))
                ? ValidationUtil.parseBigDecimal(minPriceRaw, null) : null;

        String maxPriceRaw = request.getParameter("maxPrice");
        BigDecimal maxPrice = (ValidationUtil.isNotEmpty(maxPriceRaw))
                ? ValidationUtil.parseBigDecimal(maxPriceRaw, null) : null;

        String sortBy = ValidationUtil.sanitize(request.getParameter("sortBy"));
        if (!ValidationUtil.isNotEmpty(sortBy)) {
            sortBy = "newest"; // Mặc định: Mới nhất
        }

        // 2. Xử lý phân trang
        int page = ValidationUtil.parseInt(request.getParameter("page"), 1);
        if (page < 1) {
            page = 1;
        }

        // 3. Đếm tổng số xe thỏa mãn bộ lọc
        int totalCars = carDAO.countSearchCars(keyword, brandId, categoryId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) totalCars / DEFAULT_PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        // 4. Truy vấn danh sách xe theo trang hiện tại
        List<Car> cars = carDAO.searchCars(keyword, brandId, categoryId, minPrice, maxPrice, sortBy, page, DEFAULT_PAGE_SIZE);

        // 5. Lấy danh sách Hãng và Phân khúc để hiển thị bộ lọc Sidebar
        List<Brand> brands = brandDAO.getAllBrands();
        List<Category> categories = categoryDAO.getAllCategories();

        // 6. Gửi dữ liệu ra View JSP
        request.setAttribute("cars", cars);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);

        // Giữ lại trạng thái bộ lọc đang chọn trên giao diện
        request.setAttribute("selectedKeyword", keyword);
        request.setAttribute("selectedBrandId", brandId);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("selectedMinPrice", minPriceRaw);
        request.setAttribute("selectedMaxPrice", maxPriceRaw);
        request.setAttribute("selectedSortBy", sortBy);

        request.getRequestDispatcher("/WEB-INF/views/client/car-list.jsp").forward(request, response);
    }
}
