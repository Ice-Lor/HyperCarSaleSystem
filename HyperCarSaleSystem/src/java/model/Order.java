package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Thực thể ánh xạ bảng Orders (Hợp đồng giao dịch đặt cọc siêu xe).
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int orderId;
    private String orderCode; // Mã hợp đồng độc bản (ví dụ: ORD-2026-0001)
    private int userId;
    private String username; // Thuộc tính bổ trợ JOIN bảng Users
    private String userFullName; // Họ tên khách hàng
    private BigDecimal totalAmount; // Tổng giá trị siêu xe
    private BigDecimal depositAmount; // Số tiền đặt cọc cần thanh toán (10% - 20%)
    private String couponCode; // Mã ưu đãi áp dụng (cho phép null)
    private BigDecimal discountAmount; // Số tiền được giảm giá
    private String status; // PENDING, CONFIRMED, PROCESSING, COMPLETED, CANCELLED
    private String paymentMethod; // BANK_TRANSFER, CRYPTO_USDT, SHOWROOM_DIRECT
    private String deliveryAddress; // Địa chỉ bàn giao xe VIP
    private String phone; // Số điện thoại liên hệ
    private String note; // Ghi chú yêu cầu đặc biệt của khách
    private Timestamp orderDate;
    private List<OrderDetail> details; // Danh sách chi tiết siêu xe trong hợp đồng

    public Order() {
        this.details = new ArrayList<OrderDetail>();
        this.status = "PENDING";
        this.paymentMethod = "BANK_TRANSFER";
        this.discountAmount = BigDecimal.ZERO;
    }

    public Order(int orderId, String orderCode, int userId, BigDecimal totalAmount, 
                 BigDecimal depositAmount, String couponCode, BigDecimal discountAmount, 
                 String status, String paymentMethod, String deliveryAddress, String phone, 
                 String note, Timestamp orderDate) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.depositAmount = depositAmount;
        this.couponCode = couponCode;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.note = note;
        this.orderDate = orderDate;
        this.details = new ArrayList<OrderDetail>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    public List<OrderDetail> getOrderDetails() {
        return details;
    }

    public void setOrderDetails(List<OrderDetail> details) {
        this.details = details;
    }

    public void addDetail(OrderDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<OrderDetail>();
        }
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "Order{" + "orderCode=" + orderCode + ", userFullName=" + userFullName 
                + ", totalAmount=" + totalAmount + ", depositAmount=" + depositAmount 
                + ", status=" + status + '}';
    }
}
