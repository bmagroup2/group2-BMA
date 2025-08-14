package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.InventoryItem;

import java.util.List;
import java.util.Map;

public class LogisticOfficer extends User {
    private String officerId;
    private String department;
    private String contactNumber;

    public LogisticOfficer(String userId, String name, String email, String password, String officerId, String contactNumber) {
        super(userId, name, email, "Logistic Officer", password);
        this.officerId = officerId;
        this.contactNumber = contactNumber;
        this.department = "Logistics";
    }

    public boolean manageInventory(InventoryItem item, String action) {
        switch (action.toLowerCase()) {
            case "add":
                return item.addItem(1);
            case "update":
                return item.updateStock(item.getQuantity());
            case "remove":
                return item.updateStock(0);
            default:
                return false;
        }
    }

    public List<InventoryItem> viewStock(String category) {

        return null;
    }

    public boolean issueItems(String cadetId, List<InventoryItem> items) {

        for (InventoryItem item : items) {
            if (!item.issue(cadetId, 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean requestRestock(String itemId, int quantity) {

        System.out.println("Restock request submitted for item " + itemId + ", quantity: " + quantity);
        return true;
    }

    public boolean trackDamages(String itemId, String details) {

        System.out.println("Damage tracked for item " + itemId + ": " + details);
        return true;
    }

    public String generateReport(int month, int year) {

        return "Logistics Report for " + month + "/" + year;
    }


    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "LogisticOfficer{" +
                "officerId=\'" + officerId + '\'' +
                ", department=\'" + department + '\'' +
                ", contactNumber=\'" + contactNumber + '\'' +
                ", userId=\'" + getUserId() + '\'' +
                ", name=\'" + getName() + '\'' +
                ", role=\'" + getRole() + '\'' +
                '}' +
                '\n';
    }
}

