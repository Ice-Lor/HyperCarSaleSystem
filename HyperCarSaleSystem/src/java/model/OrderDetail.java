package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetail implements Serializable {
    private int detailId;
    private int orderId;
    private int carId;
    private String carModelName;
    private String carThumbnailUrl;
    private int quantity;
    private BigDecimal unitPrice;
    private String selectedColor;
    private String customOptions;

    public OrderDetail() {}

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getCarModelName() { return carModelName; }
    public void setCarModelName(String carModelName) { this.carModelName = carModelName; }

    public String getCarThumbnailUrl() { return carThumbnailUrl; }
    public void setCarThumbnailUrl(String carThumbnailUrl) { this.carThumbnailUrl = carThumbnailUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getSelectedColor() { return selectedColor; }
    public void setSelectedColor(String selectedColor) { this.selectedColor = selectedColor; }

    public String getCustomOptions() { return customOptions; }
    public void setCustomOptions(String customOptions) { this.customOptions = customOptions; }

    public BigDecimal getSubTotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}
