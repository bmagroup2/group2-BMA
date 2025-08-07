package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;


public class AuditLog implements Serializable {
    private String logId;
    private String userId;
    private String action;
    private Date timestamp;
    private String details;


    public AuditLog(String logId, String userId, String action, String details) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.timestamp = new Date();
        this.details = details;
    }


    public String getLogId() {
        return logId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setLogId(String logId) { this.logId = logId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setAction(String action) { this.action = action; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public void setDetails(String details) { this.details = details; }

    @Override
    public String toString() {
        return "AuditLog{" +
                "logId='" + logId + '\'' +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                '}';
    }
}