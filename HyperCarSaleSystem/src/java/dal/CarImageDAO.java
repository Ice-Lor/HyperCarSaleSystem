package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CarImage;

public class CarImageDAO extends DBContext {

    public List<CarImage> getImagesByCarId(int carId) {
        List<CarImage> list = new ArrayList<CarImage>();
        String sql = "SELECT * FROM CarImages WHERE car_id = ? ORDER BY image_id ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CarImage(
                        rs.getInt("image_id"),
                        rs.getInt("car_id"),
                        rs.getString("image_url"),
                        rs.getString("caption")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean insertImage(CarImage img) {
        String sql = "INSERT INTO CarImages (car_id, image_url, caption) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, img.getCarId());
            ps.setString(2, img.getImageUrl());
            ps.setString(3, img.getCaption());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteImage(int imageId) {
        String sql = "DELETE FROM CarImages WHERE image_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
