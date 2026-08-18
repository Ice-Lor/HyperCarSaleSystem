package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ActivityLog implements Serializable {
    private int logId;
    private Integer userId;
    private String action;
    private String details;
    private Timestamp createdAt;

    // Join attributes
    private String username;

    public ActivityLog() {}

    public ActivityLog(Integer userId, String action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
