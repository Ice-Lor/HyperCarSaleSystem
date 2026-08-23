package controller.client;

import dal.BrandDAO;
import dal.CarDAO;
import dal.CategoryDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Brand;
import model.Car;
import model.Category;

/**
 * Controller Trang Chủ Showroom HyperCarSaleSystem.
 * Hiển thị Banner Hero, Danh mục Thương hiệu, Top Siêu xe nổi bật và Siêu xe mới nhất.
 */
@WebServlet(name = "HomeController", urlPatterns = {"/home", ""})
public class HomeController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy danh sách 6 siêu xe nổi bật nhất (Giá & Mã lực cao nhất)
        List<Car> featuredCars = carDAO.getFeaturedCars(6);
        request.setAttribute("featuredCars", featuredCars);

        // 2. Lấy danh sách 4 siêu xe mới cập nhật gần đây nhất
        List<Car> latestCars = carDAO.getLatestCars(4);
        request.setAttribute("latestCars", latestCars);

        // 3. Lấy toàn bộ 8 thương hiệu siêu xe chính thức
        List<Brand> brands = brandDAO.getAllBrands();
        request.setAttribute("brands", brands);

        // 4. Lấy toàn bộ phân khúc siêu xe
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Chuyển tiếp tới giao diện Trang Chủ
        request.getRequestDispatcher("/WEB-INF/views/client/home.jsp").forward(request, response);
    }
}
