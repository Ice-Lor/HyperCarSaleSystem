package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ActivityLog;

public class ActivityLogDAO extends DBContext {

    public List<ActivityLog> getRecentLogs(int limit) {
        List<ActivityLog> list = new ArrayList<ActivityLog>();
        String sql = "SELECT TOP (?) l.*, u.username "
                   + "FROM ActivityLogs l "
                   + "LEFT JOIN Users u ON l.user_id = u.user_id "
                   + "ORDER BY l.log_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setLogId(rs.getInt("log_id"));
                    int uid = rs.getInt("user_id");
                    log.setUserId(rs.wasNull() ? null : uid);
                    log.setUsername(rs.getString("username"));
                    log.setAction(rs.getString("action"));
                    log.setDetails(rs.getString("details"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(log);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ActivityLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean log(Integer userId, String action, String details) {
        String sql = "INSERT INTO ActivityLogs (user_id, action, details) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, action);
            ps.setString(3, details);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ActivityLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
