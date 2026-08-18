package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Coupon;

public class CouponDAO extends DBContext {

    public Coupon getValidCoupon(String couponCode) {
        String sql = "SELECT * FROM Coupons WHERE coupon_code = ? AND is_active = 1 AND expiry_date >= GETDATE()";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, couponCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coupon cp = new Coupon();
                    cp.setCouponCode(rs.getString("coupon_code"));
                    cp.setDiscountPercent(rs.getDouble("discount_percent"));
                    cp.setMaxDiscount(rs.getBigDecimal("max_discount"));
                    cp.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
                    cp.setExpiryDate(rs.getTimestamp("expiry_date"));
                    cp.setIsActive(rs.getInt("is_active"));
                    return cp;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CouponDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
