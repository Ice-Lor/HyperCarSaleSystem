package model;

import java.io.Serializable;

public class CarImage implements Serializable {
    private int imageId;
    private int carId;
    private String imageUrl;
    private String caption;

    public CarImage() {}

    public CarImage(int imageId, int carId, String imageUrl, String caption) {
        this.imageId = imageId;
        this.carId = carId;
        this.imageUrl = imageUrl;
        this.caption = caption;
    }

    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}
