package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Thực thể ánh xạ bảng ActivityLogs (Nhật ký vết hoạt động hệ thống / Audit Logs).
 */
public class ActivityLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private int logId;
    private Integer userId; // Có thể null nếu là hành động hệ thống
    private String username; // Thuộc tính bổ trợ khi JOIN với bảng Users
    private String action;
    private String details;
    private Timestamp createdAt;

    public ActivityLog() {
    }

    public ActivityLog(int logId, Integer userId, String action, String details, Timestamp createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ActivityLog{" + "logId=" + logId + ", action=" + action + ", details=" + details 
                + ", createdAt=" + createdAt + '}';
    }
}
