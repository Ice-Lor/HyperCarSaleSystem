package model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Car car;
    private int quantity;
    private String selectedColor;
    private String customOptions;

    public CartItem() {}

    public CartItem(Car car, int quantity, String selectedColor, String customOptions) {
        this.car = car;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.customOptions = customOptions;
    }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSelectedColor() { return selectedColor; }
    public void setSelectedColor(String selectedColor) { this.selectedColor = selectedColor; }

    public String getCustomOptions() { return customOptions; }
    public void setCustomOptions(String customOptions) { this.customOptions = customOptions; }

    public double getTotalPrice() {
        if (car == null) return 0;
        return car.getPrice() * quantity;
    }

    public double getTotalDeposit() {
        if (car == null) return 0;
        return car.getDepositAmount() * quantity;
    }
}
