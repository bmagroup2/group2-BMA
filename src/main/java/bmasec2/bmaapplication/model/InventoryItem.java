package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class InventoryItem implements Serializable {
    private String itemId;
    private String name;
    private int quantity;
    private String unit;
    private String category;
    private int minStockLevel;
    private Date lastUpdated;
    private String issuedTo;
    private Date issueDate;
    private String type;

    public InventoryItem(String itemId, String name, int quantity, String unit, String category) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.minStockLevel = 10; // Default minimum stock level
        this.lastUpdated = new Date();
        this.type = "General";
    }

    public InventoryItem(String itemId, String name, int quantity, String unit, String category, int minStockLevel) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.minStockLevel = minStockLevel;
        this.lastUpdated = new Date();
        this.type = "General";
    }

    // Business methods
    public boolean addItem(int additionalQuantity) {
        if (additionalQuantity > 0) {
            this.quantity += additionalQuantity;
            this.lastUpdated = new Date();
            return true;
        }
        return false;
    }

    public boolean updateStock(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
            this.lastUpdated = new Date();
            return true;
        }
        return false;
    }

    public int checkStock() {
        return this.quantity;
    }

    public boolean issue(String recipient, int issueQuantity) {
        if (issueQuantity > 0 && this.quantity >= issueQuantity) {
            this.quantity -= issueQuantity;
            this.issuedTo = recipient;
            this.issueDate = new Date();
            this.lastUpdated = new Date();
            return true;
        }
        return false;
    }

    public boolean returnItem(int returnQuantity) {
        if (returnQuantity > 0) {
            this.quantity += returnQuantity;
            this.lastUpdated = new Date();
            return true;
        }
        return false;
    }

    public boolean isLowStock() {
        return this.quantity <= this.minStockLevel;
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", category='" + category + '\'' +
                ", minStockLevel=" + minStockLevel +
                ", lastUpdated=" + lastUpdated +
                ", isLowStock=" + isLowStock() +
                '}';
    }

    public String getItemName() {
        return "";
    }

    public boolean isBelowMinStock() {
        return false;
    }
}