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

public class CouponDAO extends DBContext {

    public Coupon getCouponByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        String sql = "SELECT * FROM Coupons WHERE coupon_code = ? AND is_active = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCoupon(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CouponDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Coupon> getAllCoupons() {
        List<Coupon> list = new ArrayList<Coupon>();
        String sql = "SELECT * FROM Coupons ORDER BY expiry_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractCoupon(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CouponDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean insertCoupon(Coupon c) {
        String sql = "INSERT INTO Coupons (coupon_code, discount_percent, max_discount, min_order_amount, expiry_date, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCouponCode().toUpperCase());
            ps.setDouble(2, c.getDiscountPercent());
            ps.setDouble(3, c.getMaxDiscount());
            ps.setDouble(4, c.getMinOrderAmount());
            ps.setTimestamp(5, c.getExpiryDate());
            ps.setInt(6, c.getIsActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CouponDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private Coupon extractCoupon(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponCode(rs.getString("coupon_code"));
        c.setDiscountPercent(rs.getDouble("discount_percent"));
        c.setMaxDiscount(rs.getDouble("max_discount"));
        c.setMinOrderAmount(rs.getDouble("min_order_amount"));
        c.setExpiryDate(rs.getTimestamp("expiry_date"));
        c.setIsActive(rs.getInt("is_active"));
        return c;
    }
}
