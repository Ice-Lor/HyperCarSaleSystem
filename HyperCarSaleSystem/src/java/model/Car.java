package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Car implements Serializable {
    private int carId;
    private String modelName;
    private int brandId;
    private int categoryId;
    private double price;
    private double depositRate;
    private int year;
    private int horsepower;
    private double acceleration0100;
    private int topSpeed;
    private int stockQuantity;
    private String thumbnailUrl;
    private String colorOptions;
    private String engineSpec;
    private String description;
    private int status;
    private Timestamp createdAt;

    // Join attributes
    private String brandName;
    private String brandLogo;
    private String categoryName;

    public Car() {}

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDepositRate() { return depositRate; }
    public void setDepositRate(double depositRate) { this.depositRate = depositRate; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getHorsepower() { return horsepower; }
    public void setHorsepower(int horsepower) { this.horsepower = horsepower; }

    public double getAcceleration0100() { return acceleration0100; }
    public void setAcceleration0100(double acceleration0100) { this.acceleration0100 = acceleration0100; }

    public int getTopSpeed() { return topSpeed; }
    public void setTopSpeed(int topSpeed) { this.topSpeed = topSpeed; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getColorOptions() { return colorOptions; }
    public void setColorOptions(String colorOptions) { this.colorOptions = colorOptions; }

    public String getEngineSpec() { return engineSpec; }
    public void setEngineSpec(String engineSpec) { this.engineSpec = engineSpec; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getBrandLogo() { return brandLogo; }
    public void setBrandLogo(String brandLogo) { this.brandLogo = brandLogo; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public double getDepositAmount() {
        return (this.price * this.depositRate) / 100.0;
    }
}
