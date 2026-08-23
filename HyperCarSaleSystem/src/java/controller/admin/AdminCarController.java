package controller.admin;

import dal.ActivityLogDAO;
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
import javax.servlet.http.HttpSession;
import model.Brand;
import model.Car;
import model.Category;
import model.User;
import util.ValidationUtil;

/**
 * Controller Quản lý kho siêu xe (Admin Car Management - CRUD Siêu Xe).
 */
@WebServlet(name = "AdminCarController", urlPatterns = {"/admin/cars"})
public class AdminCarController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (!ValidationUtil.isNotEmpty(action)) {
            action = "list";
        }

        switch (action.toLowerCase()) {
            case "create":
                prepareFormDropdowns(request);
                request.setAttribute("isEdit", false);
                request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = ValidationUtil.parseInt(request.getParameter("id"), 0);
                Car car = carDAO.getCarById(editId);
                if (car == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/cars");
                    return;
                }
                prepareFormDropdowns(request);
                request.setAttribute("car", car);
                request.setAttribute("isEdit", true);
                request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
                break;

            case "list":
            default:
                List<Car> cars = carDAO.getAllCarsAdmin();
                request.setAttribute("cars", cars);
                request.getRequestDispatcher("/WEB-INF/views/admin/car-manage.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (!ValidationUtil.isNotEmpty(action)) {
            action = "insert";
        }

        // 1. Xử lý Xóa xe an toàn qua POST
        if ("delete".equalsIgnoreCase(action)) {
            int deleteId = ValidationUtil.parseInt(request.getParameter("id"), 0);
            if (deleteId > 0) {
                carDAO.deleteCar(deleteId);
                logUserAction(request, "DELETE_CAR", "Admin đã xóa siêu xe ID: " + deleteId);
            }
            response.sendRedirect(request.getContextPath() + "/admin/cars?msg=deleted");
            return;
        }

        // 2. Xử lý Bật/Tắt trạng thái bán qua POST
        if ("toggle_status".equalsIgnoreCase(action)) {
            int toggleId = ValidationUtil.parseInt(request.getParameter("id"), 0);
            int newStatus = ValidationUtil.parseInt(request.getParameter("status"), 1);
            if (toggleId > 0) {
                carDAO.updateStatus(toggleId, newStatus);
                logUserAction(request, "UPDATE_CAR_STATUS", "Admin cập nhật trạng thái xe ID: " + toggleId + " sang: " + newStatus);
            }
            response.sendRedirect(request.getContextPath() + "/admin/cars?msg=status_updated");
            return;
        }

        // 3. Xử lý Thêm mới hoặc Cập nhật thông tin xe
        int carId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
        String modelName = ValidationUtil.sanitize(request.getParameter("modelName"));
        int brandId = ValidationUtil.parseInt(request.getParameter("brandId"), 0);
        int categoryId = ValidationUtil.parseInt(request.getParameter("categoryId"), 0);
        BigDecimal price = ValidationUtil.parseBigDecimal(request.getParameter("price"), BigDecimal.ZERO);
        BigDecimal depositRate = ValidationUtil.parseBigDecimal(request.getParameter("depositRate"), new BigDecimal("10.0"));
        int year = ValidationUtil.parseInt(request.getParameter("year"), 2024);
        int horsepower = ValidationUtil.parseInt(request.getParameter("horsepower"), 0);
        double acceleration = ValidationUtil.parseDouble(request.getParameter("acceleration"), 0.0);
        int topSpeed = ValidationUtil.parseInt(request.getParameter("topSpeed"), 0);
        int stockQuantity = ValidationUtil.parseInt(request.getParameter("stockQuantity"), 1);
        String thumbnailUrl = ValidationUtil.sanitize(request.getParameter("thumbnailUrl"));
        String colorOptions = ValidationUtil.sanitize(request.getParameter("colorOptions"));
        String engineSpec = ValidationUtil.sanitize(request.getParameter("engineSpec"));
        String description = ValidationUtil.sanitize(request.getParameter("description"));
        int status = ValidationUtil.parseInt(request.getParameter("status"), 1);

        Car car = new Car();
        car.setModelName(modelName);
        car.setBrandId(brandId);
        car.setCategoryId(categoryId);
        car.setPrice(price);
        car.setDepositRate(depositRate);
        car.setYear(year);
        car.setHorsepower(horsepower);
        car.setAcceleration0100(acceleration);
        car.setTopSpeed(topSpeed);
        car.setStockQuantity(stockQuantity);
        car.setThumbnailUrl(thumbnailUrl);
        car.setColorOptions(colorOptions);
        car.setEngineSpec(engineSpec);
        car.setDescription(description);
        car.setStatus(status);

        if ("edit".equalsIgnoreCase(action) && carId > 0) {
            car.setCarId(carId);
            boolean updated = carDAO.updateCar(car);
            if (updated) {
                logUserAction(request, "UPDATE_CAR", "Admin cập nhật thông tin siêu xe: " + modelName + " (ID: " + carId + ")");
            }
        } else {
            int newId = carDAO.insertCar(car);
            if (newId > 0) {
                logUserAction(request, "CREATE_CAR", "Admin thêm mới siêu xe: " + modelName + " (ID: " + newId + ")");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/cars?msg=saved");
    }

    private void prepareFormDropdowns(HttpServletRequest request) {
        List<Brand> brands = brandDAO.getAllBrands();
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
    }

    private void logUserAction(HttpServletRequest request, String action, String details) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            logDAO.log(user.getUserId(), action, details);
        }
    }
}
