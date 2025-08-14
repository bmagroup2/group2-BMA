package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class FoodStock implements Serializable {
    private String stockId;
    private String itemName;
    private int quantity;
    private String unit;
    private Date expiryDate;
    private String supplier;
    private double costPerUnit;
    private String category;

    public FoodStock(String stockId, String itemName, int quantity, String unit, 
                     Date expiryDate, String supplier, double costPerUnit, String category) {
        this.stockId = stockId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.costPerUnit = costPerUnit;
        this.category = category;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "FoodStock{" +
                "stockId='" + stockId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", expiryDate=" + expiryDate +
                ", supplier='" + supplier + '\'' +
                ", costPerUnit=" + costPerUnit +
                ", category='" + category + '\'' +
                '}';
    }
}

