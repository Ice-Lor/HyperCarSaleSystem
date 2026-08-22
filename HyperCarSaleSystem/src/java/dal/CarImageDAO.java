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
import model.CarImage;

/**
 * Lớp truy xuất dữ liệu bộ sưu tập hình ảnh chi tiết của siêu xe (CarImages).
 */
public class CarImageDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(CarImageDAO.class.getName());

    /**
     * Lấy danh sách toàn bộ ảnh chi tiết của một siêu xe theo car_id.
     */
    public List<CarImage> getImagesByCarId(int carId) {
        List<CarImage> list = new ArrayList<CarImage>();
        String sql = "SELECT * FROM CarImages WHERE car_id = ? ORDER BY image_id ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCarImage(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách ảnh của xe ID: " + carId, ex);
        }
        return list;
    }

    /**
     * Thêm một ảnh mới vào bộ sưu tập của xe.
     */
    public int insertImage(CarImage img) {
        String sql = "INSERT INTO CarImages (car_id, image_url, caption) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, img.getCarId());
            ps.setString(2, img.getImageUrl());
            ps.setString(3, img.getCaption());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi thêm ảnh mới cho xe ID: " + img.getCarId(), ex);
        }
        return -1;
    }

    /**
     * Xóa một ảnh theo image_id.
     */
    public boolean deleteImage(int imageId) {
        String sql = "DELETE FROM CarImages WHERE image_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa ảnh ID: " + imageId, ex);
        }
        return false;
    }

    /**
     * Xóa toàn bộ ảnh chi tiết của một siêu xe (dùng khi xóa xe).
     */
    public boolean deleteImagesByCarId(int carId) {
        String sql = "DELETE FROM CarImages WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa bộ sưu tập ảnh của xe ID: " + carId, ex);
        }
        return false;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng CarImage.
     */
    private CarImage mapCarImage(ResultSet rs) throws SQLException {
        CarImage img = new CarImage();
        img.setImageId(rs.getInt("image_id"));
        img.setCarId(rs.getInt("car_id"));
        img.setImageUrl(rs.getString("image_url"));
        img.setCaption(rs.getString("caption"));
        return img;
    }
}
