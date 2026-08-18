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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String brandIdStr = request.getParameter("brandId");
        String categoryIdStr = request.getParameter("categoryId");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String sortBy = request.getParameter("sortBy");
        String pageStr = request.getParameter("page");

        Integer brandId = (brandIdStr != null && !brandIdStr.isEmpty()) ? Integer.parseInt(brandIdStr) : null;
        Integer categoryId = (categoryIdStr != null && !categoryIdStr.isEmpty()) ? Integer.parseInt(categoryIdStr) : null;
        Double minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? Double.parseDouble(minPriceStr) : null;
        Double maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? Double.parseDouble(maxPriceStr) : null;

        int page = 1;
        int pageSize = 6;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageStr));
            } catch (NumberFormatException ignored) {}
        }

        List<Car> cars = carDAO.searchCarsDynamic(keyword, brandId, categoryId, minPrice, maxPrice, sortBy, page, pageSize);
        int totalCars = carDAO.countCarsDynamic(keyword, brandId, categoryId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) totalCars / pageSize);

        List<Brand> brands = brandDAO.getAllBrands();
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("cars", cars);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);

        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedBrandId", brandId);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/WEB-INF/views/client/car-list.jsp").forward(request, response);
    }
}
