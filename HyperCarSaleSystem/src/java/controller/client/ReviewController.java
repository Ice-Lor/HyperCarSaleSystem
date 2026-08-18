package controller.client;

import dal.ReviewDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CarReview;
import model.User;
import util.CSRFUtil;
import util.ValidationUtil;

public class ReviewController extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = ValidationUtil.sanitize(request.getParameter("comment"));

            CarReview review = new CarReview();
            review.setUserId(currentUser.getUserId());
            review.setCarId(carId);
            review.setRating(rating);
            review.setComment(comment);

            reviewDAO.saveOrUpdateReview(review);
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cars");
        }
    }
}
