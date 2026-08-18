package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Coupon implements Serializable {
    private String couponCode;
    private double discountPercent;
    private double maxDiscount;
    private double minOrderAmount;
    private Timestamp expiryDate;
    private int isActive;

    public Coupon() {}

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public double getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(double maxDiscount) { this.maxDiscount = maxDiscount; }

    public double getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(double minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public Timestamp getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Timestamp expiryDate) { this.expiryDate = expiryDate; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

    public boolean isValid(double totalAmount) {
        if (isActive != 1) return false;
        if (expiryDate != null && expiryDate.getTime() < System.currentTimeMillis()) return false;
        return totalAmount >= minOrderAmount;
    }

    public double calculateDiscount(double totalAmount) {
        if (!isValid(totalAmount)) return 0;
        double discount = (totalAmount * discountPercent) / 100.0;
        if (maxDiscount > 0 && discount > maxDiscount) {
            discount = maxDiscount;
        }
        return discount;
    }
}
