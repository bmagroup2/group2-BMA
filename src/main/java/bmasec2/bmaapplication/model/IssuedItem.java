package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IssuedItem implements Serializable {
    private final String issueId;
    private final String itemId;
    private final String itemName;
    private final String issuedToUserId;
    private final String issuedToUserName;
    private final int quantity;
    private final String unit;
    private final String purpose;
    private final LocalDateTime issueDate;

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

    @Override
    public String toString() {
        return "IssuedItem{" +
                "issueId='" + issueId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", issuedToUserId='" + issuedToUserId + '\'' +
                ", issuedToUserName='" + issuedToUserName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", purpose='" + purpose + '\'' +
                ", issueDate=" + issueDate +
                '}';
    }
}


