package controller.client;

import dal.ActivityLogDAO;
import dal.ReviewDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CarReview;
import model.User;
import util.ValidationUtil;

/**
 * Controller xử lý gửi Đánh giá sao và Bình luận trải nghiệm siêu xe của khách hàng VIP.
 */
@WebServlet(name = "ReviewController", urlPatterns = {"/submit-review"})
public class ReviewController extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        int carId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
        int rating = ValidationUtil.parseInt(request.getParameter("rating"), 5);
        String comment = ValidationUtil.sanitize(request.getParameter("comment"));

        if (carId <= 0) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;

        // 1. Kiểm tra xem người dùng đã từng đánh giá mẫu xe này chưa
        if (reviewDAO.checkUserReviewed(currentUser.getUserId(), carId)) {
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId + "&reviewError=already_reviewed#reviews");
            return;
        }

        // 2. Thêm đánh giá mới
        CarReview review = new CarReview();
        review.setUserId(currentUser.getUserId());
        review.setCarId(carId);
        review.setRating(rating);
        review.setComment(comment);

        int result = reviewDAO.insertReview(review);
        if (result > 0) {
            logDAO.log(currentUser.getUserId(), "SUBMIT_REVIEW", 
                    "Khách hàng " + currentUser.getUsername() + " đã đánh giá " + rating + " sao cho xe ID: " + carId);
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId + "&reviewSuccess=1#reviews");
        } else {
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId + "&reviewError=failed#reviews");
        }
    }
}
