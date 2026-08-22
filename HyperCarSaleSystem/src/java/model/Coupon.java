package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng Coupons (Mã ưu đãi / Chiết khấu VIP đặt cọc).
 */
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    private String couponCode;
    private BigDecimal discountPercent; // Phần trăm giảm giá (ví dụ: 2.0%)
    private BigDecimal maxDiscount; // Số tiền giảm tối đa USD
    private BigDecimal minOrderAmount; // Giá trị đơn hàng tối thiểu để áp dụng mã
    private Timestamp expiryDate; // Thời hạn hiệu lực
    private int isActive; // 1: Đang hoạt động, 0: Đã vô hiệu hóa

    public Coupon() {
        this.discountPercent = BigDecimal.ZERO;
        this.maxDiscount = BigDecimal.ZERO;
        this.minOrderAmount = BigDecimal.ZERO;
        this.isActive = 1;
    }

    public Coupon(String couponCode, BigDecimal discountPercent, BigDecimal maxDiscount, 
                  BigDecimal minOrderAmount, Timestamp expiryDate, int isActive) {
        this.couponCode = couponCode;
        this.discountPercent = discountPercent != null ? discountPercent : BigDecimal.ZERO;
        this.maxDiscount = maxDiscount != null ? maxDiscount : BigDecimal.ZERO;
        this.minOrderAmount = minOrderAmount != null ? minOrderAmount : BigDecimal.ZERO;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    /**
     * Kiểm tra mã coupon có còn hiệu lực với giá trị đơn hàng hiện tại hay không.
     */
    public boolean isValid(BigDecimal totalAmount) {
        if (this.isActive != 1) {
            return false;
        }
        if (this.expiryDate != null && this.expiryDate.before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        if (totalAmount == null || totalAmount.compareTo(this.minOrderAmount) < 0) {
            return false;
        }
        return true;
    }

    /**
     * Tính toán số tiền chiết khấu thực tế (không vượt quá maxDiscount).
     */
    public BigDecimal calculateDiscount(BigDecimal totalAmount) {
        if (!isValid(totalAmount)) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = totalAmount.multiply(this.discountPercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        if (this.maxDiscount != null && this.maxDiscount.compareTo(BigDecimal.ZERO) > 0 
                && discount.compareTo(this.maxDiscount) > 0) {
            return this.maxDiscount;
        }
        return discount;
    }

    @Override
    public String toString() {
        return "Coupon{" + "couponCode=" + couponCode + ", discountPercent=" + discountPercent 
                + ", maxDiscount=" + maxDiscount + ", isActive=" + isActive + '}';
    }
}
