package controller.client;

import dal.CarDAO;
import dal.CarImageDAO;
import dal.ReviewDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.CarImage;
import model.CarReview;
import model.User;
import util.ValidationUtil;

/**
 * Controller xem chi tiết Siêu Xe (Thông số kỹ thuật, Gallery ảnh, Đánh giá sao, Xe tương tự).
 */
@WebServlet(name = "CarDetailController", urlPatterns = {"/car-detail"})
public class CarDetailController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final CarImageDAO carImageDAO = new CarImageDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int carId = ValidationUtil.parseInt(request.getParameter("id"), 0);

        if (carId <= 0) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        // 1. Lấy thông tin chi tiết siêu xe
        Car car = carDAO.getCarById(carId);
        if (car == null) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }
        request.setAttribute("car", car);

        // 2. Lấy bộ sưu tập hình ảnh chi tiết (Gallery)
        List<CarImage> images = carImageDAO.getImagesByCarId(carId);
        request.setAttribute("images", images);

        // 3. Lấy danh sách đánh giá sao và bình luận
        List<CarReview> reviews = reviewDAO.getReviewsByCarId(carId);
        request.setAttribute("reviews", reviews);

        // 4. Tính điểm đánh giá sao trung bình
        double averageRating = reviewDAO.getAverageRating(carId);
        request.setAttribute("averageRating", averageRating);

        // 5. Kiểm tra xem người dùng hiện tại đã từng đánh giá mẫu xe này chưa
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        boolean hasReviewed = false;
        if (currentUser != null) {
            hasReviewed = reviewDAO.checkUserReviewed(currentUser.getUserId(), carId);
        }
        request.setAttribute("hasReviewed", hasReviewed);

        // 6. Lấy 3 mẫu siêu xe cùng thương hiệu (Related Cars)
        List<Car> relatedCars = carDAO.searchCars(null, car.getBrandId(), null, null, null, "newest", 1, 3);
        // Loại bỏ chính chiếc xe hiện tại khỏi danh sách tương tự
        relatedCars.removeIf(c -> c.getCarId() == carId);
        request.setAttribute("relatedCars", relatedCars);

        request.getRequestDispatcher("/WEB-INF/views/client/car-detail.jsp").forward(request, response);
    }
}
