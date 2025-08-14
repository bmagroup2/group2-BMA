package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReturnLogEntry implements Serializable {
    private String logId;
    private String itemId;
    private String itemName;
    private String returnedByUserId;
    private String returnedByUserName;
    private String reason;
    private LocalDateTime logDate;

    public ReturnLogEntry(String logId, String itemId, String itemName, String returnedByUserId, String returnedByUserName, String reason) {
        this.logId = logId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.returnedByUserId = returnedByUserId;
        this.returnedByUserName = returnedByUserName;
        this.reason = reason;
        this.logDate = LocalDateTime.now();
    }

    // Getters
    public String getLogId() {
        return logId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getReturnedByUserId() {
        return returnedByUserId;
    }

    public String getReturnedByUserName() {
        return returnedByUserName;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getLogDate() {
        return logDate;
    }
}


