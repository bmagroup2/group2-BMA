package bmasec2.bmaapplication.fatema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RestockRequest implements Serializable {
    private String requestId;
    private String itemId;
    private String itemName;
    private int requestedQuantity;
    private String reason;
    private LocalDateTime requestDate;
    private String status; // e.g., Pending, Approved, Rejected, Fulfilled

    public RestockRequest(String requestId, String itemId, String itemName, int requestedQuantity, String reason) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.requestedQuantity = requestedQuantity;
        this.reason = reason;
        this.requestDate = LocalDateTime.now();
        this.status = "Pending";
    }

    // Getters
    public String getRequestId() {
        return requestId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


