package bmasec2.bmaapplication.fatema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IssuedItem implements Serializable {
    private String issueId;
    private String itemId;
    private String itemName;
    private String issuedToUserId;
    private String issuedToUserName;
    private int quantity;
    private String unit;
    private String purpose;
    private LocalDateTime issueDate;

    public IssuedItem(String issueId, String itemId, String itemName, String issuedToUserId, String issuedToUserName, int quantity, String unit, String purpose) {
        this.issueId = issueId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.issuedToUserId = issuedToUserId;
        this.issuedToUserName = issuedToUserName;
        this.quantity = quantity;
        this.unit = unit;
        this.purpose = purpose;
        this.issueDate = LocalDateTime.now();
    }

    // Getters
    public String getIssueId() {
        return issueId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getIssuedToUserId() {
        return issuedToUserId;
    }

    public String getIssuedToUserName() {
        return issuedToUserName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getPurpose() {
        return purpose;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }
}


