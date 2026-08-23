package model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Thực thể ánh xạ bảng OrderDetails (Chi tiết từng siêu xe trong hợp đồng đặt cọc).
 */
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int detailId;
    private int orderId;
    private int carId;
    private String carModelName; // Thuộc tính bổ trợ JOIN bảng Cars
    private String carBrandName; // Thuộc tính bổ trợ JOIN bảng Brands
    private String carThumbnailUrl; // Ảnh đại diện xe
    private int quantity; // Số lượng đặt cọc (mặc định 1)
    private BigDecimal unitPrice; // Đơn giá niêm yết tại thời điểm ký cọc
    private String selectedColor; // Màu sơn ngoại thất bespoke đã chọn
    private String customOptions; // Tùy chọn trang bị cá nhân hóa

    public OrderDetail() {
        this.quantity = 1;
    }

    public OrderDetail(int detailId, int orderId, int carId, int quantity, BigDecimal unitPrice, 
                       String selectedColor, String customOptions) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.carId = carId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.selectedColor = selectedColor;
        this.customOptions = customOptions;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getCarBrandName() {
        return carBrandName != null ? carBrandName : "";
    }

    public void setCarBrandName(String carBrandName) {
        this.carBrandName = carBrandName;
    }

    public String getCarThumbnailUrl() {
        return carThumbnailUrl;
    }

    public void setCarThumbnailUrl(String carThumbnailUrl) {
        this.carThumbnailUrl = carThumbnailUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
     * Tính tiền cọc 10% cho dòng sản phẩm này.
     */
    public BigDecimal getDepositPrice() {
        if (this.unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return this.unitPrice.multiply(new BigDecimal(this.quantity))
                             .multiply(new BigDecimal("0.10"));
    }

    /**
     * Tính tổng giá trị của dòng chi tiết này (Đơn giá * Số lượng).
     */
    public BigDecimal getSubtotal() {
        if (this.unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return this.unitPrice.multiply(new BigDecimal(this.quantity));
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "detailId=" + detailId + ", carModelName=" + carModelName 
                + ", quantity=" + quantity + ", unitPrice=" + unitPrice + '}';
    }
}
