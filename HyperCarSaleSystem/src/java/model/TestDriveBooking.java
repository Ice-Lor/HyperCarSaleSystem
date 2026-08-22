package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng TestDriveBookings (Đăng ký trải nghiệm & lái thử Track VIP).
 */
public class TestDriveBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    private int bookingId;
    private int userId;
    private String username; // Thuộc tính bổ trợ JOIN bảng Users
    private String userFullName; // Họ tên khách VIP
    private String userPhone; // SĐT khách VIP
    private int carId;
    private String carModelName; // Thuộc tính bổ trợ JOIN bảng Cars
    private String carThumbnailUrl; // Ảnh đại diện siêu xe lái thử
    private Date bookingDate; // Ngày trải nghiệm
    private String timeSlot; // Khung giờ (ví dụ: 09:00 - 11:00)
    private String locationTrack; // Địa điểm trường đua (Hà Nội F1 Circuit, Sepang...)
    private String driverLicenseNumber; // Số bằng lái xe quốc tế/B2
    private String note; // Ghi chú yêu cầu huấn luyện viên F1
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private Timestamp createdAt;

    public TestDriveBooking() {
        this.status = "PENDING";
    }

    public TestDriveBooking(int bookingId, int userId, int carId, Date bookingDate, 
                            String timeSlot, String locationTrack, String driverLicenseNumber, 
                            String note, String status, Timestamp createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.carId = carId;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        this.locationTrack = locationTrack;
        this.driverLicenseNumber = driverLicenseNumber;
        this.note = note;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getCarThumbnailUrl() {
        return carThumbnailUrl;
    }

    public void setCarThumbnailUrl(String carThumbnailUrl) {
        this.carThumbnailUrl = carThumbnailUrl;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getLocationTrack() {
        return locationTrack;
    }

    public void setLocationTrack(String locationTrack) {
        this.locationTrack = locationTrack;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TestDriveBooking{" + "bookingId=" + bookingId + ", userFullName=" + userFullName 
                + ", carModelName=" + carModelName + ", bookingDate=" + bookingDate 
                + ", status=" + status + '}';
    }
}
