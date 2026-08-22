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
import model.User;
import util.PasswordUtil;

/**
 * Lớp truy xuất dữ liệu người dùng (Users) trong cơ sở dữ liệu.
 */
public class UserDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    /**
     * Xác thực đăng nhập bằng Tên đăng nhập và Mật khẩu thô (sử dụng jBCrypt).
     * 
     * @param username Tên đăng nhập
     * @param rawPassword Mật khẩu chưa mã hóa từ form login
     * @return Đối tượng User nếu đăng nhập thành công & tài khoản đang mở (status = 1), ngược lại trả về null
     */
    public User login(String username, String rawPassword) {
        String sql = "SELECT u.*, r.role_name FROM Users u "
                   + "JOIN Roles r ON u.role_id = r.role_id "
                   + "WHERE u.username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordHash = rs.getString("password_hash");
                    int status = rs.getInt("status");
                    
                    // So khớp mật khẩu băm BCrypt và kiểm tra trạng thái hoạt động
                    if (status == 1 && PasswordUtil.checkPassword(rawPassword, passwordHash)) {
                        return mapUser(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đăng nhập user: " + username, ex);
        }
        return null;
    }

    /**
     * Tìm người dùng theo ID.
     */
    public User getUserById(int userId) {
        String sql = "SELECT u.*, r.role_name FROM Users u "
                   + "JOIN Roles r ON u.role_id = r.role_id "
                   + "WHERE u.user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy thông tin user theo ID: " + userId, ex);
        }
        return null;
    }

    /**
     * Tìm người dùng theo Username.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT u.*, r.role_name FROM Users u "
                   + "JOIN Roles r ON u.role_id = r.role_id "
                   + "WHERE u.username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm user theo username: " + username, ex);
        }
        return null;
    }

    /**
     * Kiểm tra Tên đăng nhập đã tồn tại trong hệ thống chưa.
     */
    public boolean checkUsernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi kiểm tra trùng username: " + username, ex);
        }
        return false;
    }

    /**
     * Kiểm tra Email đã được đăng ký bởi tài khoản khác chưa.
     */
    public boolean checkEmailExists(String email) {
        String sql = "SELECT 1 FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi kiểm tra trùng email: " + email, ex);
        }
        return false;
    }

    /**
     * Đăng ký tài khoản khách hàng VIP mới vào hệ thống.
     * Mặc định role_id = 2 (CUSTOMER) và status = 1 (Active).
     */
    public int register(User user) {
        String sql = "INSERT INTO Users (username, password_hash, full_name, email, phone, address, role_id, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 2, 1)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đăng ký tài khoản: " + user.getUsername(), ex);
        }
        return -1;
    }

    /**
     * Cập nhật thông tin hồ sơ cá nhân của khách hàng.
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE Users SET full_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật hồ sơ user ID: " + user.getUserId(), ex);
        }
        return false;
    }

    /**
     * Đổi mật khẩu tài khoản bằng chuỗi băm mới.
     */
    public boolean changePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE Users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đổi mật khẩu user ID: " + userId, ex);
        }
        return false;
    }

    /**
     * Lấy toàn bộ danh sách thành viên trong hệ thống (dùng cho Admin).
     */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT u.*, r.role_name FROM Users u "
                   + "JOIN Roles r ON u.role_id = r.role_id "
                   + "ORDER BY u.user_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách toàn bộ user", ex);
        }
        return list;
    }

    /**
     * Khóa hoặc Mở khóa tài khoản người dùng (Admin).
     * 
     * @param userId ID người dùng
     * @param newStatus 1: Active, 0: Locked
     */
    public boolean updateStatus(int userId, int newStatus) {
        String sql = "UPDATE Users SET status = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật trạng thái user ID: " + userId, ex);
        }
        return false;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng User.
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        u.setRoleId(rs.getInt("role_id"));
        u.setRoleName(rs.getString("role_name"));
        u.setStatus(rs.getInt("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
