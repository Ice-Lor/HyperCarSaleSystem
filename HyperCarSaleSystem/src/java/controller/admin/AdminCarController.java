package controller.admin;

import dal.ActivityLogDAO;
import dal.BrandDAO;
import dal.CarDAO;
import dal.CategoryDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Brand;
import model.Car;
import model.Category;
import model.User;
import util.CSRFUtil;
import util.ValidationUtil;

public class AdminCarController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            List<Brand> brands = brandDAO.getAllBrands();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
            return;
        } else if ("edit".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("id"));
            Car car = carDAO.getCarById(carId);
            List<Brand> brands = brandDAO.getAllBrands();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("car", car);
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
            return;
        }

        List<Car> cars = carDAO.getAllCarsAdmin();
        request.setAttribute("cars", cars);
        request.getRequestDispatcher("/WEB-INF/views/admin/car-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/cars");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            String carIdStr = request.getParameter("carId");
            String modelName = ValidationUtil.sanitize(request.getParameter("modelName"));
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            BigDecimal depositRate = new BigDecimal(request.getParameter("depositRate"));
            int year = Integer.parseInt(request.getParameter("year"));
            int horsepower = Integer.parseInt(request.getParameter("horsepower"));
            double acceleration = Double.parseDouble(request.getParameter("acceleration0100"));
            int topSpeed = Integer.parseInt(request.getParameter("topSpeed"));
            int stock = Integer.parseInt(request.getParameter("stockQuantity"));
            String thumbnail = ValidationUtil.sanitize(request.getParameter("thumbnailUrl"));
            String colors = ValidationUtil.sanitize(request.getParameter("colorOptions"));
            String engine = ValidationUtil.sanitize(request.getParameter("engineSpec"));
            String desc = ValidationUtil.sanitize(request.getParameter("description"));
            int status = Integer.parseInt(request.getParameter("status"));

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
            car.setStockQuantity(stock);
            car.setThumbnailUrl(thumbnail);
            car.setColorOptions(colors);
            car.setEngineSpec(engine);
            car.setDescription(desc);
            car.setStatus(status);

            if (carIdStr != null && !carIdStr.isEmpty()) {
                car.setCarId(Integer.parseInt(carIdStr));
                carDAO.updateCar(car);
                logDAO.log(currentUser.getUserId(), "UPDATE_CAR", "Cập nhật siêu xe: " + modelName);
                session.setAttribute("successMessage", "Cập nhật thông tin siêu xe thành công!");
            } else {
                carDAO.insertCar(car);
                logDAO.log(currentUser.getUserId(), "ADD_CAR", "Thêm mới siêu xe: " + modelName);
                session.setAttribute("successMessage", "Thêm siêu xe mới vào showroom thành công!");
            }

        } else if ("delete".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("id"));
            carDAO.deleteCar(carId);
            logDAO.log(currentUser.getUserId(), "DELETE_CAR", "Xóa siêu xe ID: " + carId);
            session.setAttribute("successMessage", "Xóa siêu xe thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/admin/cars");
    }
}
