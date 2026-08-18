package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Car implements Serializable {
    private int carId;
    private String modelName;
    private int brandId;
    private String brandName;
    private String brandCountry;
    private int categoryId;
    private String categoryName;
    private BigDecimal price;
    private BigDecimal depositRate;
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
    private Date createdAt;

    public Car() {}

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getBrandCountry() { return brandCountry; }
    public void setBrandCountry(String brandCountry) { this.brandCountry = brandCountry; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getDepositRate() { return depositRate; }
    public void setDepositRate(BigDecimal depositRate) { this.depositRate = depositRate; }

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

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
