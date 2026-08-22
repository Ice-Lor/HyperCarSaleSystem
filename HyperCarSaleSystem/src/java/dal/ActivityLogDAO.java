package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ActivityLog;

/**
 * Lớp truy xuất và ghi vết hoạt động hệ thống (ActivityLogs / Audit Trail).
 */
public class ActivityLogDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ActivityLogDAO.class.getName());

    /**
     * Ghi một vết hoạt động mới vào hệ thống.
     * 
     * @param userId ID người dùng thực hiện (có thể null nếu là tác vụ hệ thống)
     * @param action Tên hành động (vd: LOGIN, REGISTER, PLACE_ORDER, LOCK_USER...)
     * @param details Chi tiết cụ thể của hành động
     */
    public void log(Integer userId, String action, String details) {
        String sql = "INSERT INTO ActivityLogs (user_id, action, details, created_at) "
                   + "VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId != null && userId > 0) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi ghi nhật ký hoạt động: " + action, ex);
        }
    }

    /**
     * Lấy danh sách các hoạt động gần đây nhất để hiển thị trên Bàn Quản Trị (Admin Dashboard).
     * 
     * @param limit Số lượng bản ghi cần lấy (vd: 10)
     * @return Danh sách ActivityLog
     */
    public List<ActivityLog> getRecentLogs(int limit) {
        List<ActivityLog> list = new ArrayList<ActivityLog>();
        String sql = "SELECT TOP (?) l.*, u.username FROM ActivityLogs l "
                   + "LEFT JOIN Users u ON l.user_id = u.user_id "
                   + "ORDER BY l.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setLogId(rs.getInt("log_id"));
                    int uid = rs.getInt("user_id");
                    if (!rs.wasNull()) {
                        log.setUserId(uid);
                    }
                    log.setUsername(rs.getString("username"));
                    log.setAction(rs.getString("action"));
                    log.setDetails(rs.getString("details"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(log);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách nhật ký hoạt động gần đây", ex);
        }
        return list;
    }
}
