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
import model.TestDriveBooking;

public class TestDriveDAO extends DBContext {

    public boolean createBooking(TestDriveBooking booking) {
        String sql = "INSERT INTO TestDriveBookings (user_id, car_id, booking_date, time_slot, location_track, driver_license_number, note, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getCarId());
            ps.setDate(3, booking.getBookingDate());
            ps.setString(4, booking.getTimeSlot());
            ps.setString(5, booking.getLocationTrack());
            ps.setString(6, booking.getDriverLicenseNumber());
            ps.setString(7, booking.getNote());
            ps.setString(8, booking.getStatus() != null ? booking.getStatus() : "PENDING");
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        booking.setBookingId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<TestDriveBooking> getBookingsByUserId(int userId) {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT b.*, u.full_name as customer_name, u.email as customer_email, u.phone as customer_phone, "
                   + "c.model_name as car_model_name, c.thumbnail_url as car_thumbnail, br.brand_name "
                   + "FROM TestDriveBookings b "
                   + "JOIN Users u ON b.user_id = u.user_id "
                   + "JOIN Cars c ON b.car_id = c.car_id "
                   + "JOIN Brands br ON c.brand_id = br.brand_id "
                   + "WHERE b.user_id = ? ORDER BY b.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractBooking(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<TestDriveBooking> getAllBookings() {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT b.*, u.full_name as customer_name, u.email as customer_email, u.phone as customer_phone, "
                   + "c.model_name as car_model_name, c.thumbnail_url as car_thumbnail, br.brand_name "
                   + "FROM TestDriveBookings b "
                   + "JOIN Users u ON b.user_id = u.user_id "
                   + "JOIN Cars c ON b.car_id = c.car_id "
                   + "JOIN Brands br ON c.brand_id = br.brand_id "
                   + "ORDER BY b.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractBooking(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateStatus(int bookingId, String newStatus) {
        String sql = "UPDATE TestDriveBookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private TestDriveBooking extractBooking(ResultSet rs) throws SQLException {
        TestDriveBooking b = new TestDriveBooking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setCarId(rs.getInt("car_id"));
        b.setBookingDate(rs.getDate("booking_date"));
        b.setTimeSlot(rs.getString("time_slot"));
        b.setLocationTrack(rs.getString("location_track"));
        b.setDriverLicenseNumber(rs.getString("driver_license_number"));
        b.setNote(rs.getString("note"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        
        try {
            b.setCustomerName(rs.getString("customer_name"));
            b.setCustomerEmail(rs.getString("customer_email"));
            b.setCustomerPhone(rs.getString("customer_phone"));
            b.setCarModelName(rs.getString("car_model_name"));
            b.setCarThumbnail(rs.getString("car_thumbnail"));
            b.setBrandName(rs.getString("brand_name"));
        } catch (SQLException ignored) {}
        
        return b;
    }
}
