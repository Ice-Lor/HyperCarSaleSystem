package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TestDriveBooking;

public class TestDriveDAO extends DBContext {

    public boolean insertBooking(TestDriveBooking booking) {
        String sql = "INSERT INTO TestDriveBookings (user_id, car_id, booking_date, time_slot, "
                   + "location_track, driver_license_number, note, status, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING', GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getCarId());
            ps.setDate(3, new java.sql.Date(booking.getBookingDate().getTime()));
            ps.setString(4, booking.getTimeSlot());
            ps.setString(5, booking.getLocationTrack());
            ps.setString(6, booking.getDriverLicenseNumber());
            ps.setString(7, booking.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<TestDriveBooking> getBookingsByUserId(int userId) {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT t.*, u.full_name as user_name, u.email as user_email, u.phone as user_phone, "
                   + "c.model_name as car_model_name, c.thumbnail_url as car_thumbnail_url "
                   + "FROM TestDriveBookings t "
                   + "JOIN Users u ON t.user_id = u.user_id "
                   + "JOIN Cars c ON t.car_id = c.car_id "
                   + "WHERE t.user_id = ? "
                   + "ORDER BY t.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBooking(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<TestDriveBooking> getAllBookingsAdmin() {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT t.*, u.full_name as user_name, u.email as user_email, u.phone as user_phone, "
                   + "c.model_name as car_model_name, c.thumbnail_url as car_thumbnail_url "
                   + "FROM TestDriveBookings t "
                   + "JOIN Users u ON t.user_id = u.user_id "
                   + "JOIN Cars c ON t.car_id = c.car_id "
                   + "ORDER BY t.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBooking(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE TestDriveBookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TestDriveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private TestDriveBooking mapBooking(ResultSet rs) throws SQLException {
        TestDriveBooking b = new TestDriveBooking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setUserName(rs.getString("user_name"));
        b.setUserEmail(rs.getString("user_email"));
        b.setUserPhone(rs.getString("user_phone"));
        b.setCarId(rs.getInt("car_id"));
        b.setCarModelName(rs.getString("car_model_name"));
        b.setCarThumbnailUrl(rs.getString("car_thumbnail_url"));
        b.setBookingDate(rs.getDate("booking_date"));
        b.setTimeSlot(rs.getString("time_slot"));
        b.setLocationTrack(rs.getString("location_track"));
        b.setDriverLicenseNumber(rs.getString("driver_license_number"));
        b.setNote(rs.getString("note"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
