package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReturnLogEntry implements Serializable {
    private final String logId;
    private final String itemId;
    private final String itemName;
    private final String returnedByUserId;
    private final String returnedByUserName;
    private final String reason;
    private final LocalDateTime logDate;

    public ReturnLogEntry(String logId, String itemId, String itemName, String returnedByUserId, String returnedByUserName, String reason) {
        this.logId = logId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.returnedByUserId = returnedByUserId;
        this.returnedByUserName = returnedByUserName;
        this.reason = reason;
        this.logDate = LocalDateTime.now();
    }

    
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


