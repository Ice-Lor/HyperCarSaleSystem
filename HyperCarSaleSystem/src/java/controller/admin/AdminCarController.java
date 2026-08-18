package controller.admin;

import dal.BrandDAO;
import dal.CarDAO;
import dal.CategoryDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Brand;
import model.Car;
import model.Category;
import util.CSRFUtil;
import util.ValidationUtil;

public class AdminCarController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            List<Brand> brands = brandDAO.getAllBrands();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            try {
                int carId = Integer.parseInt(request.getParameter("id"));
                Car car = carDAO.getCarById(carId);
                if (car != null) {
                    List<Brand> brands = brandDAO.getAllBrands();
                    List<Category> categories = categoryDAO.getAllCategories();
                    request.setAttribute("car", car);
                    request.setAttribute("brands", brands);
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/WEB-INF/views/admin/car-form.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        List<Car> carList = carDAO.getAllCarsForAdmin();
        request.setAttribute("carList", carList);
        request.getRequestDispatcher("/WEB-INF/views/admin/car-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (!CSRFUtil.isValidToken(request)) {
            if (session != null) session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin/cars");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            try {
                int carId = Integer.parseInt(request.getParameter("id"));
                carDAO.deleteCar(carId);
                if (session != null) session.setAttribute("toastMessage", "Đã ngừng kinh doanh siêu xe thành công!");
            } catch (NumberFormatException ignored) {}
            response.sendRedirect(request.getContextPath() + "/admin/cars");
            return;
        }

        if ("save".equalsIgnoreCase(action)) {
            try {
                String idStr = request.getParameter("carId");
                String modelName = ValidationUtil.sanitize(request.getParameter("modelName"));
                int brandId = Integer.parseInt(request.getParameter("brandId"));
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                double price = Double.parseDouble(request.getParameter("price"));
                double depositRate = Double.parseDouble(request.getParameter("depositRate"));
                int year = Integer.parseInt(request.getParameter("year"));
                int horsepower = Integer.parseInt(request.getParameter("horsepower"));
                double acceleration0100 = Double.parseDouble(request.getParameter("acceleration0100"));
                int topSpeed = Integer.parseInt(request.getParameter("topSpeed"));
                int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
                String thumbnailUrl = request.getParameter("thumbnailUrl");
                String colorOptions = ValidationUtil.sanitize(request.getParameter("colorOptions"));
                String engineSpec = ValidationUtil.sanitize(request.getParameter("engineSpec"));
                String description = ValidationUtil.sanitize(request.getParameter("description"));
                int status = Integer.parseInt(request.getParameter("status"));

                Car car = new Car();
                car.setModelName(modelName);
                car.setBrandId(brandId);
                car.setCategoryId(categoryId);
                car.setPrice(price);
                car.setDepositRate(depositRate);
                car.setYear(year);
                car.setHorsepower(horsepower);
                car.setAcceleration0100(acceleration0100);
                car.setTopSpeed(topSpeed);
                car.setStockQuantity(stockQuantity);
                car.setThumbnailUrl(thumbnailUrl);
                car.setColorOptions(colorOptions);
                car.setEngineSpec(engineSpec);
                car.setDescription(description);
                car.setStatus(status);

                boolean success;
                if (idStr != null && !idStr.trim().isEmpty() && !"0".equals(idStr.trim())) {
                    car.setCarId(Integer.parseInt(idStr.trim()));
                    success = carDAO.updateCar(car);
                    if (session != null) session.setAttribute("toastMessage", "Cập nhật siêu xe thành công!");
                } else {
                    success = carDAO.insertCar(car);
                    if (session != null) session.setAttribute("toastMessage", "Thêm mới siêu xe vào showroom thành công!");
                }

                if (!success && session != null) {
                    session.setAttribute("errorMessage", "Không thể lưu thông tin siêu xe!");
                }
            } catch (Exception e) {
                if (session != null) session.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/cars");
    }
}
