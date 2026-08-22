package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Coupon;

/**
 * Lớp truy xuất dữ liệu mã ưu đãi / chiết khấu VIP (Coupons) trong cơ sở dữ liệu.
 */
public class CouponDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(CouponDAO.class.getName());

    /**
     * Tìm mã giảm giá theo mã code (chỉ lấy mã đang kích hoạt và chưa hết hạn).
     */
    public Coupon getCouponByCode(String couponCode) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT * FROM Coupons "
                   + "WHERE LOWER(coupon_code) = LOWER(?) "
                   + "AND is_active = 1 "
                   + "AND expiry_date >= GETDATE()";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, couponCode.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCoupon(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm coupon theo mã: " + couponCode, ex);
        }
        return null;
    }

    /**
     * Lấy toàn bộ danh sách mã ưu đãi trong hệ thống (dùng cho Admin quản lý).
     */
    public List<Coupon> getAllCoupons() {
        List<Coupon> list = new ArrayList<Coupon>();
        String sql = "SELECT * FROM Coupons ORDER BY expiry_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCoupon(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy toàn bộ danh sách coupon", ex);
        }
        return list;
    }

    /**
     * Thêm mới một mã ưu đãi (Admin).
     */
    public boolean insertCoupon(Coupon coupon) {
        String sql = "INSERT INTO Coupons (coupon_code, discount_percent, max_discount, min_order_amount, expiry_date, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coupon.getCouponCode().trim().toUpperCase());
            ps.setBigDecimal(2, coupon.getDiscountPercent());
            ps.setBigDecimal(3, coupon.getMaxDiscount());
            ps.setBigDecimal(4, coupon.getMinOrderAmount());
            ps.setTimestamp(5, coupon.getExpiryDate());
            ps.setInt(6, coupon.getIsActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi thêm coupon mới: " + coupon.getCouponCode(), ex);
        }
        return false;
    }

    /**
     * Cập nhật thông tin mã ưu đãi (Admin).
     */
    public boolean updateCoupon(Coupon coupon) {
        String sql = "UPDATE Coupons SET discount_percent = ?, max_discount = ?, min_order_amount = ?, "
                   + "expiry_date = ?, is_active = ? WHERE coupon_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, coupon.getDiscountPercent());
            ps.setBigDecimal(2, coupon.getMaxDiscount());
            ps.setBigDecimal(3, coupon.getMinOrderAmount());
            ps.setTimestamp(4, coupon.getExpiryDate());
            ps.setInt(5, coupon.getIsActive());
            ps.setString(6, coupon.getCouponCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật coupon: " + coupon.getCouponCode(), ex);
        }
        return false;
    }

    /**
     * Xóa một mã ưu đãi khỏi hệ thống (Admin).
     */
    public boolean deleteCoupon(String couponCode) {
        String sql = "DELETE FROM Coupons WHERE coupon_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, couponCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa coupon: " + couponCode, ex);
        }
        return false;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng Coupon.
     */
    private Coupon mapCoupon(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponCode(rs.getString("coupon_code"));
        c.setDiscountPercent(rs.getBigDecimal("discount_percent"));
        c.setMaxDiscount(rs.getBigDecimal("max_discount"));
        c.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        c.setExpiryDate(rs.getTimestamp("expiry_date"));
        c.setIsActive(rs.getInt("is_active"));
        return c;
    }
}
