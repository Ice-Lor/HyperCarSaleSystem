package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CarReview;

/**
 * Lớp truy xuất dữ liệu đánh giá sao và bình luận siêu xe (CarReviews).
 */
public class ReviewDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ReviewDAO.class.getName());

    /**
     * Lấy danh sách toàn bộ đánh giá của một mẫu siêu xe (kèm họ tên khách hàng).
     */
    public List<CarReview> getReviewsByCarId(int carId) {
        List<CarReview> list = new ArrayList<CarReview>();
        String sql = "SELECT r.*, u.username, u.full_name "
                   + "FROM CarReviews r "
                   + "JOIN Users u ON r.user_id = u.user_id "
                   + "WHERE r.car_id = ? "
                   + "ORDER BY r.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy đánh giá của xe ID: " + carId, ex);
        }
        return list;
    }

    /**
     * Thêm một đánh giá mới từ khách hàng VIP.
     */
    public int insertReview(CarReview review) {
        String sql = "INSERT INTO CarReviews (user_id, car_id, rating, comment, created_at) "
                   + "VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getCarId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi gửi đánh giá xe ID: " + review.getCarId(), ex);
        }
        return -1;
    }

    /**
     * Kiểm tra xem khách hàng đã từng gửi đánh giá cho mẫu xe này chưa (tránh spam đánh giá).
     */
    public boolean checkUserReviewed(int userId, int carId) {
        String sql = "SELECT 1 FROM CarReviews WHERE user_id = ? AND car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, carId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi kiểm tra đánh giá của user: " + userId, ex);
        }
        return false;
    }

    /**
     * Tính điểm đánh giá trung bình của một mẫu siêu xe (ví dụ: 4.8 / 5.0 sao).
     */
    public double getAverageRating(int carId) {
        String sql = "SELECT AVG(CAST(rating AS FLOAT)) FROM CarReviews WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tính điểm đánh giá trung bình xe ID: " + carId, ex);
        }
        return 5.0; // Mặc định 5 sao nếu chưa có đánh giá
    }

    /**
     * Xóa một đánh giá theo ID (Admin).
     */
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM CarReviews WHERE review_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa đánh giá ID: " + reviewId, ex);
        }
        return false;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng CarReview.
     */
    private CarReview mapReview(ResultSet rs) throws SQLException {
        CarReview r = new CarReview();
        r.setReviewId(rs.getInt("review_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setUsername(rs.getString("username"));
        r.setUserFullName(rs.getString("full_name"));
        r.setCarId(rs.getInt("car_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
