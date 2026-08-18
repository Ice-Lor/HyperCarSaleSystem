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

public class HomeController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Car> featuredCars = carDAO.getFeaturedCars(6);
        List<Car> latestCars = carDAO.getLatestCars(4);
        List<Brand> brands = brandDAO.getAllBrands();
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("featuredCars", featuredCars);
        request.setAttribute("latestCars", latestCars);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/views/client/home.jsp").forward(request, response);
    }
}
