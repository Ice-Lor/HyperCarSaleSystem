package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class TestDriveBooking implements Serializable {
    private int bookingId;
    private int userId;
    private int carId;
    private Date bookingDate;
    private String timeSlot;
    private String locationTrack;
    private String driverLicenseNumber;
    private String note;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private Timestamp createdAt;

    // Join attributes
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String carModelName;
    private String carThumbnail;
    private String brandName;

    public TestDriveBooking() {}

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

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

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCarModelName() { return carModelName; }
    public void setCarModelName(String carModelName) { this.carModelName = carModelName; }

    public String getCarThumbnail() { return carThumbnail; }
    public void setCarThumbnail(String carThumbnail) { this.carThumbnail = carThumbnail; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
}
