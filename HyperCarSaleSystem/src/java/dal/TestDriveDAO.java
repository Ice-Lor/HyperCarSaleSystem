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

/**
 * Lớp truy xuất dữ liệu đăng ký trải nghiệm & lái thử Track VIP (TestDriveBookings).
 */
public class TestDriveDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(TestDriveDAO.class.getName());

    /**
     * Tạo mới một đơn đăng ký lái thử siêu xe trường đua.
     */
    public int createBooking(TestDriveBooking booking) {
        String sql = "INSERT INTO TestDriveBookings (user_id, car_id, booking_date, time_slot, "
                   + "location_track, driver_license_number, note, status, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
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

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tạo lịch đăng ký lái thử cho user: " + booking.getUserId(), ex);
        }
        return -1;
    }

    /**
     * Lấy danh sách lịch sử đăng ký lái thử của một khách hàng VIP.
     */
    public List<TestDriveBooking> getBookingsByUserId(int userId) {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT b.*, c.model_name AS car_model_name, c.thumbnail_url AS car_thumbnail_url, "
                   + "u.username, u.full_name AS user_full_name, u.phone AS user_phone "
                   + "FROM TestDriveBookings b "
                   + "JOIN Cars c ON b.car_id = c.car_id "
                   + "JOIN Users u ON b.user_id = u.user_id "
                   + "WHERE b.user_id = ? "
                   + "ORDER BY b.booking_date DESC, b.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBooking(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy lịch sử lái thử của user ID: " + userId, ex);
        }
        return list;
    }

    /**
     * Lấy toàn bộ danh sách đăng ký lái thử (dùng cho Admin quản lý và duyệt lịch).
     */
    public List<TestDriveBooking> getAllBookingsAdmin() {
        List<TestDriveBooking> list = new ArrayList<TestDriveBooking>();
        String sql = "SELECT b.*, c.model_name AS car_model_name, c.thumbnail_url AS car_thumbnail_url, "
                   + "u.username, u.full_name AS user_full_name, u.phone AS user_phone "
                   + "FROM TestDriveBookings b "
                   + "JOIN Cars c ON b.car_id = c.car_id "
                   + "JOIN Users u ON b.user_id = u.user_id "
                   + "ORDER BY b.booking_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBooking(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách toàn bộ lịch lái thử cho Admin", ex);
        }
        return list;
    }

    /**
     * Cập nhật trạng thái lịch lái thử (CONFIRMED: Đã duyệt, COMPLETED: Hoàn thành, CANCELLED: Đã hủy).
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE TestDriveBookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật trạng thái lái thử ID: " + bookingId, ex);
        }
        return false;
    }

    /**
     * Đếm số lượng lịch lái thử đang chờ duyệt (PENDING) cho Bàn Quản Trị (Admin Dashboard).
     */
    public int countPendingBookings() {
        String sql = "SELECT COUNT(*) FROM TestDriveBookings WHERE status = 'PENDING'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đếm số lượng lịch lái thử chờ duyệt", ex);
        }
        return 0;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng TestDriveBooking.
     */
    private TestDriveBooking mapBooking(ResultSet rs) throws SQLException {
        TestDriveBooking b = new TestDriveBooking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setUsername(rs.getString("username"));
        b.setUserFullName(rs.getString("user_full_name"));
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
