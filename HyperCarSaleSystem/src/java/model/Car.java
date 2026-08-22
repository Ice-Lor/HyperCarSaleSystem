package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng Cars (Danh mục siêu xe thượng lưu độc bản).
 */
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private int carId;
    private String modelName;
    private int brandId;
    private String brandName; // Thuộc tính bổ trợ JOIN bảng Brands
    private String brandCountry; // Xuất xứ thương hiệu
    private String brandLogoUrl; // Logo hãng
    private int categoryId;
    private String categoryName; // Thuộc tính bổ trợ JOIN bảng Categories
    private BigDecimal price; // Giá niêm yết USD
    private BigDecimal depositRate; // Tỷ lệ đặt cọc % (mặc định 10.0)
    private int year; // Năm sản xuất
    private int horsepower; // Mã lực (HP)
    private double acceleration0100; // Tăng tốc 0-100 km/h (giây)
    private int topSpeed; // Tốc độ tối đa (km/h)
    private int stockQuantity; // Số lượng sẵn sàng giao dịch
    private String thumbnailUrl; // Ảnh đại diện xe
    private String colorOptions; // Danh sách màu sơn tùy chọn
    private String engineSpec; // Thông số động cơ (W16, V12, Quad-Motor...)
    private String description; // Mô tả chi tiết di sản & thiết kế
    private int status; // 1: Đang mở bán, 0: Tạm ngừng kinh doanh
    private Timestamp createdAt;

    public Car() {
        this.depositRate = new BigDecimal("10.0");
        this.status = 1;
        this.stockQuantity = 1;
    }

    public Car(int carId, String modelName, int brandId, int categoryId, BigDecimal price, 
               BigDecimal depositRate, int year, int horsepower, double acceleration0100, 
               int topSpeed, int stockQuantity, String thumbnailUrl, String colorOptions, 
               String engineSpec, String description, int status, Timestamp createdAt) {
        this.carId = carId;
        this.modelName = modelName;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
        this.depositRate = depositRate != null ? depositRate : new BigDecimal("10.0");
        this.year = year;
        this.horsepower = horsepower;
        this.acceleration0100 = acceleration0100;
        this.topSpeed = topSpeed;
        this.stockQuantity = stockQuantity;
        this.thumbnailUrl = thumbnailUrl;
        this.colorOptions = colorOptions;
        this.engineSpec = engineSpec;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCountry() {
        return brandCountry;
    }

    public void setBrandCountry(String brandCountry) {
        this.brandCountry = brandCountry;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public double getAcceleration0100() {
        return acceleration0100;
    }

    public void setAcceleration0100(double acceleration0100) {
        this.acceleration0100 = acceleration0100;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(int topSpeed) {
        this.topSpeed = topSpeed;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getColorOptions() {
        return colorOptions;
    }

    public void setColorOptions(String colorOptions) {
        this.colorOptions = colorOptions;
    }

    public String getEngineSpec() {
        return engineSpec;
    }

    public void setEngineSpec(String engineSpec) {
        this.engineSpec = engineSpec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Tính toán số tiền đặt cọc cần thanh toán (Giá xe * Tỷ lệ cọc / 100).
     */
    public BigDecimal getDepositAmount() {
        if (this.price == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal rate = this.depositRate != null ? this.depositRate : new BigDecimal("10.0");
        return this.price.multiply(rate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public boolean isAvailable() {
        return this.status == 1 && this.stockQuantity > 0;
    }

    @Override
    public String toString() {
        return "Car{" + "carId=" + carId + ", modelName=" + modelName + ", brandName=" + brandName 
                + ", price=" + price + ", horsepower=" + horsepower + ", stockQuantity=" + stockQuantity + '}';
    }
}
