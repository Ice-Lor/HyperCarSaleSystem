package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Giỏ hàng đặt cọc siêu xe (Lưu trữ trong HttpSession của khách hàng VIP).
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    // Sử dụng LinkedHashMap để duy trì thứ tự thêm xe vào giỏ
    private Map<Integer, CartItem> items;

    public Cart() {
        this.items = new LinkedHashMap<Integer, CartItem>();
    }

    public Collection<CartItem> getItems() {
        return this.items.values();
    }

    public Map<Integer, CartItem> getItemMap() {
        return this.items;
    }

    /**
     * Thêm siêu xe vào giỏ cọc.
     */
    public void addItem(Car car, int quantity, String selectedColor, String customOptions) {
        if (car == null || quantity <= 0) {
            return;
        }
        int carId = car.getCarId();
        if (this.items.containsKey(carId)) {
            CartItem existing = this.items.get(carId);
            existing.setQuantity(existing.getQuantity() + quantity);
            if (selectedColor != null && !selectedColor.trim().isEmpty()) {
                existing.setSelectedColor(selectedColor);
            }
            if (customOptions != null && !customOptions.trim().isEmpty()) {
                existing.setCustomOptions(customOptions);
            }
        } else {
            CartItem newItem = new CartItem(car, quantity, selectedColor, customOptions);
            this.items.put(carId, newItem);
        }
    }

    /**
     * Cập nhật số lượng của siêu xe trong giỏ cọc.
     */
    public void updateQuantity(int carId, int quantity) {
        if (quantity <= 0) {
            removeItem(carId);
        } else if (this.items.containsKey(carId)) {
            this.items.get(carId).setQuantity(quantity);
        }
    }

    /**
     * Xóa siêu xe khỏi giỏ cọc.
     */
    public void removeItem(int carId) {
        this.items.remove(carId);
    }

    /**
     * Xóa sạch toàn bộ giỏ hàng sau khi ký hợp đồng đặt cọc thành công.
     */
    public void clear() {
        this.items.clear();
    }

    /**
     * Tổng số lượng xe đang có trong giỏ cọc.
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : this.items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Tổng giá trị niêm yết của tất cả các xe trong giỏ cọc.
     */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : this.items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * Tổng số tiền đặt cọc cần thanh toán trước (10% - 20%).
     */
    public BigDecimal getDepositAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : this.items.values()) {
            total = total.add(item.getDepositSubtotal());
        }
        return total;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public String toString() {
        return "Cart{" + "totalQuantity=" + getTotalQuantity() + ", totalAmount=" + getTotalAmount() 
                + ", depositAmount=" + getDepositAmount() + '}';
    }
}
