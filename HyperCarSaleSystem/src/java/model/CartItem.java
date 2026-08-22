package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Đối tượng xe nằm trong Giỏ hàng đặt cọc (Session Cart).
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Car car;
    private int quantity;
    private String selectedColor;
    private String customOptions;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(Car car, int quantity, String selectedColor, String customOptions) {
        this.car = car;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.customOptions = customOptions;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public String getCustomOptions() {
        return customOptions;
    }

    public void setCustomOptions(String customOptions) {
        this.customOptions = customOptions;
    }

    /**
     * Tổng giá trị niêm yết của dòng sản phẩm này.
     */
    public BigDecimal getSubtotal() {
        if (this.car == null || this.car.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return this.car.getPrice().multiply(new BigDecimal(this.quantity));
    }

    /**
     * Tổng số tiền đặt cọc cần thanh toán của dòng sản phẩm này.
     */
    public BigDecimal getDepositSubtotal() {
        if (this.car == null) {
            return BigDecimal.ZERO;
        }
        return this.car.getDepositAmount().multiply(new BigDecimal(this.quantity));
    }

    @Override
    public String toString() {
        return "CartItem{" + "car=" + (car != null ? car.getModelName() : "null") 
                + ", quantity=" + quantity + ", subtotal=" + getSubtotal() + '}';
    }
}
