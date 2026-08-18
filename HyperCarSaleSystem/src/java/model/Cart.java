package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable {
    private final Map<Integer, CartItem> items = new LinkedHashMap<Integer, CartItem>();
    private Coupon appliedCoupon;

    public Cart() {}

    public void addItem(Car car, int quantity, String color, String options) {
        if (car == null) return;
        int carId = car.getCarId();
        if (items.containsKey(carId)) {
            CartItem existing = items.get(carId);
            existing.setQuantity(existing.getQuantity() + quantity);
            if (color != null && !color.isEmpty()) existing.setSelectedColor(color);
            if (options != null && !options.isEmpty()) existing.setCustomOptions(options);
        } else {
            items.put(carId, new CartItem(car, quantity, color, options));
        }
    }

    public void updateQuantity(int carId, int quantity) {
        if (quantity <= 0) {
            items.remove(carId);
        } else if (items.containsKey(carId)) {
            items.get(carId).setQuantity(quantity);
        }
    }

    public void removeItem(int carId) {
        items.remove(carId);
    }

    public void clear() {
        items.clear();
        appliedCoupon = null;
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public int getTotalItemCount() {
        int count = 0;
        for (CartItem item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getSubTotal() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public double getTotalDeposit() {
        double deposit = 0;
        for (CartItem item : items.values()) {
            deposit += item.getTotalDeposit();
        }
        return deposit;
    }

    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(Coupon appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    public double getDiscountAmount() {
        if (appliedCoupon == null) return 0;
        return appliedCoupon.calculateDiscount(getSubTotal());
    }

    public double getFinalTotal() {
        double subTotal = getSubTotal();
        double discount = getDiscountAmount();
        double finalTotal = subTotal - discount;
        return finalTotal > 0 ? finalTotal : 0;
    }

    public double getFinalDeposit() {
        double deposit = getTotalDeposit();
        // Giảm trừ tỉ lệ trên tiền cọc
        if (appliedCoupon != null && getSubTotal() > 0) {
            double discountRatio = getDiscountAmount() / getSubTotal();
            deposit = deposit * (1 - discountRatio);
        }
        return deposit > 0 ? deposit : 0;
    }
}
