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
                    CarImage img = new CarImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setCarId(rs.getInt("car_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setCaption(rs.getString("caption"));
                    list.add(img);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean insertImage(CarImage image) {
        String sql = "INSERT INTO CarImages (car_id, image_url, caption) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, image.getCarId());
            ps.setString(2, image.getImageUrl());
            ps.setString(3, image.getCaption());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteImagesByCarId(int carId) {
        String sql = "DELETE FROM CarImages WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarImageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
