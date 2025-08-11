package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class returnLogController {
    @FXML
    private ComboBox<String> itemComboBox;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextArea reasonTextArea;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TableView<ReturnLogEntry> returnLogTableView;
    @FXML
    private TableColumn<ReturnLogEntry, String> logIdColumn;
    @FXML
    private TableColumn<ReturnLogEntry, String> itemNameColumn;
    @FXML
    private TableColumn<ReturnLogEntry, Integer> quantityColumn;
    @FXML
    private TableColumn<ReturnLogEntry, String> typeColumn;
    @FXML
    private TableColumn<ReturnLogEntry, String> reasonColumn;
    @FXML
    private TableColumn<ReturnLogEntry, Date> dateColumn;

    private ObservableList<ReturnLogEntry> returnLogData = FXCollections.observableArrayList();
    private List<InventoryItem> inventoryItems;
    private List<ReturnLogEntry> allReturnLogEntries;

    @FXML
    public void initialize() {

        logIdColumn.setCellValueFactory(new PropertyValueFactory<>("logId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        returnLogTableView.setItems(returnLogData);


        typeComboBox.setItems(FXCollections.observableArrayList("Returned", "Damaged"));
        typeComboBox.setValue("Returned");


        loadInventoryItems();

        loadReturnLog();
    }

    private void loadInventoryItems() {
        inventoryItems = DataPersistenceManager.loadObjects("inventory_items.bin");
        ObservableList<String> itemOptions = FXCollections.observableArrayList();
        for (InventoryItem item : inventoryItems) {
            itemOptions.add(item.getItemId() + " - " + item.getName());
        }
        itemComboBox.setItems(itemOptions);
    }

    private void loadReturnLog() {
        allReturnLogEntries = DataPersistenceManager.loadObjects("return_log.bin");
        returnLogData.clear();
        returnLogData.addAll(allReturnLogEntries);
    }

    @FXML
    public void addLogButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String selectedItem = itemComboBox.getValue();
            String itemId = selectedItem.split(" - ")[0];
            String itemName = selectedItem.split(" - ")[1];
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            String type = typeComboBox.getValue();
            String reason = reasonTextArea.getText().trim();


            InventoryItem itemToUpdate = null;
            for (InventoryItem item : inventoryItems) {
                if (item.getItemId().equals(itemId)) {
                    itemToUpdate = item;
                    break;
                }
            }

            if (itemToUpdate == null) {
                showAlert("Error", "Selected item not found in inventory.");
                return;
            }

            if ("Returned".equals(type)) {
                itemToUpdate.returnItem(quantity);
            } else if ("Damaged".equals(type)) {

                if (itemToUpdate.getQuantity() < quantity) {
                    showAlert("Error", "Cannot mark more damaged items than available in stock.");
                    return;
                }
                itemToUpdate.setQuantity(itemToUpdate.getQuantity() - quantity);
            }

            DataPersistenceManager.saveObjects(inventoryItems, "inventory_items.bin");


            String logId = "LOG-" + UUID.randomUUID().toString().substring(0, 8);
            ReturnLogEntry newEntry = new ReturnLogEntry(logId, itemId, itemName, quantity, type, reason, new Date());
            allReturnLogEntries.add(0, newEntry); // Add to the beginning
            DataPersistenceManager.saveObjects(allReturnLogEntries, "return_log.bin");

            showAlert("Success", type + " log added successfully!\nLog ID: " + logId);
            clearForm();
            loadReturnLog();

        } catch (Exception e) {
            showAlert("Error", "Failed to add log entry: " + e.getMessage());
        }
    }

    @FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @FXML
    public void refreshLogButtonOnAction(ActionEvent actionEvent) {
        loadReturnLog();
    }

    private boolean validateInputs() {
        if (itemComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select an item.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid numeric quantity.");
            return false;
        }

        if (typeComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a type (Returned/Damaged).");
            return false;
        }

        if (reasonTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please provide a reason.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        itemComboBox.setValue(null);
        quantityTextField.clear();
        reasonTextArea.clear();
        typeComboBox.setValue("Returned");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ReturnLogEntry implements java.io.Serializable {
        private String logId;
        private String itemId;
        private String itemName;
        private int quantity;
        private String type; // Returned or Damaged
        private String reason;
        private Date date;

        public ReturnLogEntry(String logId, String itemId, String itemName, int quantity, String type, String reason, Date date) {
            this.logId = logId;
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.type = type;
            this.reason = reason;
            this.date = date;
        }

        // Getters
        public String getLogId() { return logId; }
        public String getItemId() { return itemId; }
        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public String getType() { return type; }
        public String getReason() { return reason; }
        public Date getDate() { return date; }
    }
}
