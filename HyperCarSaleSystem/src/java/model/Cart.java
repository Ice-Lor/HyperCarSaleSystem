package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<CartItem>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(Car car, int quantity, String color, String customOptions) {
        for (CartItem item : items) {
            if (item.getCar().getCarId() == car.getCarId()) {
                item.setQuantity(item.getQuantity() + quantity);
                if (color != null && !color.isEmpty()) {
                    item.setSelectedColor(color);
                }
                return;
            }
        }
        items.add(new CartItem(car, quantity, color, customOptions));
    }

    public void updateQuantity(int carId, int quantity) {
        if (quantity <= 0) {
            removeItem(carId);
            return;
        }
        for (CartItem item : items) {
            if (item.getCar().getCarId() == carId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeItem(int carId) {
        items.removeIf(item -> item.getCar().getCarId() == carId);
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getItemTotal());
        }
        return total;
    }

    public BigDecimal getTotalDeposit() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getItemDeposit());
        }
        return total;
    }
}
