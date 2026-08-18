package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CarReview;

public class ReviewDAO extends DBContext {

    public List<CarReview> getReviewsByCarId(int carId) {
        List<CarReview> list = new ArrayList<CarReview>();
        String sql = "SELECT r.*, u.username, u.full_name as user_full_name "
                   + "FROM CarReviews r "
                   + "JOIN Users u ON r.user_id = u.user_id "
                   + "WHERE r.car_id = ? ORDER BY r.review_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarReview cr = new CarReview();
                    cr.setReviewId(rs.getInt("review_id"));
                    cr.setUserId(rs.getInt("user_id"));
                    cr.setCarId(rs.getInt("car_id"));
                    cr.setRating(rs.getInt("rating"));
                    cr.setComment(rs.getString("comment"));
                    cr.setCreatedAt(rs.getTimestamp("created_at"));
                    cr.setUsername(rs.getString("username"));
                    cr.setUserFullName(rs.getString("user_full_name"));
                    list.add(cr);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean hasUserReviewedCar(int userId, int carId) {
        String sql = "SELECT 1 FROM CarReviews WHERE user_id = ? AND car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, carId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Thêm mới hoặc cập nhật review nếu khách hàng đã đánh giá trước đó (do có ràng buộc UNIQUE(user_id, car_id))
     */
    public boolean saveOrUpdateReview(CarReview review) {
        if (hasUserReviewedCar(review.getUserId(), review.getCarId())) {
            String updateSql = "UPDATE CarReviews SET rating = ?, comment = ?, created_at = GETDATE() WHERE user_id = ? AND car_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, review.getRating());
                ps.setString(2, review.getComment());
                ps.setInt(3, review.getUserId());
                ps.setInt(4, review.getCarId());
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } else {
            String insertSql = "INSERT INTO CarReviews (user_id, car_id, rating, comment) VALUES (?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, review.getUserId());
                ps.setInt(2, review.getCarId());
                ps.setInt(3, review.getRating());
                ps.setString(4, review.getComment());
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }
}
