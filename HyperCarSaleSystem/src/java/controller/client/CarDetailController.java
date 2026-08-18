package controller.client;

import dal.CarDAO;
import dal.CarImageDAO;
import dal.ReviewDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Car;
import model.CarImage;
import model.CarReview;

public class CarDetailController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final CarImageDAO imageDAO = new CarImageDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        try {
            int carId = Integer.parseInt(idStr);
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            List<CarImage> gallery = imageDAO.getImagesByCarId(carId);
            List<CarReview> reviews = reviewDAO.getReviewsByCarId(carId);
            List<Car> relatedCars = carDAO.searchCarsDynamic(null, car.getBrandId(), null, null, null, null, 1, 3);

            request.setAttribute("car", car);
            request.setAttribute("gallery", gallery);
            request.setAttribute("reviews", reviews);
            request.setAttribute("relatedCars", relatedCars);

            request.getRequestDispatcher("/WEB-INF/views/client/car-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cars");
        }
    }
}
