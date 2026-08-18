package controller.client;

import dal.BrandDAO;
import dal.CarDAO;
import dal.CategoryDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Brand;
import model.Car;
import model.Category;

public class CarListController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private static final int PAGE_SIZE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String brandIdStr = request.getParameter("brandId");
        String categoryIdStr = request.getParameter("categoryId");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String minHpStr = request.getParameter("minHp");
        String sortBy = request.getParameter("sortBy");
        String pageStr = request.getParameter("page");

        Integer brandId = parseInteger(brandIdStr);
        Integer categoryId = parseInteger(categoryIdStr);
        Double minPrice = parseDouble(minPriceStr);
        Double maxPrice = parseDouble(maxPriceStr);
        Integer minHp = parseInteger(minHpStr);

        int page = 1;
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageStr.trim()));
            } catch (NumberFormatException ignored) {}
        }

        // Lọc xe & đếm tổng số trang
        List<Car> carList = carDAO.filterCars(keyword, brandId, categoryId, minPrice, maxPrice, minHp, sortBy, page, PAGE_SIZE);
        int totalCars = carDAO.countFilteredCars(keyword, brandId, categoryId, minPrice, maxPrice, minHp);
        int totalPages = (int) Math.ceil((double) totalCars / PAGE_SIZE);

        List<Brand> brands = brandDAO.getAllBrands();
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("carList", carList);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);

        // Giữ lại các filter params trên view
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedBrandId", brandId);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("minHp", minHp);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/WEB-INF/views/client/car-list.jsp").forward(request, response);
    }

    private Integer parseInteger(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
