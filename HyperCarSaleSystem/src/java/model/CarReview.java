package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng CarReviews (Đánh giá xếp hạng sao & bình luận của Khách hàng VIP).
 */
public class CarReview implements Serializable {

    private static final long serialVersionUID = 1L;

    private int reviewId;
    private int userId;
    private String username; // Thuộc tính bổ trợ JOIN bảng Users
    private String userFullName; // Tên hiển thị khách hàng
    private int carId;
    private String carModelName; // Thuộc tính bổ trợ JOIN bảng Cars
    private int rating; // 1 đến 5 sao
    private String comment;
    private Timestamp createdAt;

    public CarReview() {
    }

    public CarReview(int reviewId, int userId, int carId, int rating, String comment, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.carId = carId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getReviewDate() {
        return createdAt;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.createdAt = reviewDate;
    }

    @Override
    public String toString() {
        return "CarReview{" + "reviewId=" + reviewId + ", username=" + username + ", carModelName=" + carModelName 
                + ", rating=" + rating + ", comment=" + comment + '}';
    }
}
