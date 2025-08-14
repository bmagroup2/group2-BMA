package bmasec2.bmaapplication.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class InvoiceItem {
    private final SimpleIntegerProperty serialNo;
    private final SimpleStringProperty itemName;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;

    public InvoiceItem(int serialNo, String itemName, int quantity, double price) {
        this.serialNo = new SimpleIntegerProperty(serialNo);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    
    public int getSerialNo() {
        return serialNo.get();
    }

    public String getItemName() {
        return itemName.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getPrice() {
        return price.get();
    }

    
    public SimpleIntegerProperty serialNoProperty() {
        return serialNo;
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    
    public void setSerialNo(int serialNo) {
        this.serialNo.set(serialNo);
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "serialNo=" + serialNo +
                ", itemName=" + itemName +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}