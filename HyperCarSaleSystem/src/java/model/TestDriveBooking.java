package model;

import java.io.Serializable;
import java.util.Date;

public class TestDriveBooking implements Serializable {
    private int bookingId;
    private int userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private int carId;
    private String carModelName;
    private String carThumbnailUrl;
    private Date bookingDate;
    private String timeSlot;
    private String locationTrack;
    private String driverLicenseNumber;
    private String note;
    private String status;
    private Date createdAt;

    public TestDriveBooking() {}

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getCarModelName() { return carModelName; }
    public void setCarModelName(String carModelName) { this.carModelName = carModelName; }

    public String getCarThumbnailUrl() { return carThumbnailUrl; }
    public void setCarThumbnailUrl(String carThumbnailUrl) { this.carThumbnailUrl = carThumbnailUrl; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getLocationTrack() { return locationTrack; }
    public void setLocationTrack(String locationTrack) { this.locationTrack = locationTrack; }

    public String getDriverLicenseNumber() { return driverLicenseNumber; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
