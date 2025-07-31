package bmasec2.bmaapplication.model;

import java.util.Date;


public class AuditLog {
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
}
