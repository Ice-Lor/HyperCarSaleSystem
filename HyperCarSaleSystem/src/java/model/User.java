package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng Users (Khách hàng VIP, Chuyên viên tư vấn & Quản trị viên).
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private int roleId;
    private String roleName; // Thuộc tính bổ trợ khi JOIN với bảng Roles
    private int status; // 1: Active, 0: Locked
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String username, String passwordHash, String fullName, String email, 
                String phone, String address, int roleId, int status, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
        this.status = status;
        this.createdAt = createdAt;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public boolean isActive() {
        return this.status == 1;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.roleName) || this.roleId == 1;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", fullName=" + fullName 
                + ", email=" + email + ", roleName=" + roleName + ", status=" + status + '}';
    }
}
