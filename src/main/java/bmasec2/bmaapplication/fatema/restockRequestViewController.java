package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class restockRequestViewController {
    @FXML
    private ComboBox<String> itemComboBox;
    @FXML
    private TextField requestedQuantityTextField;
    @FXML
    private TextArea remarksTextArea;
    @FXML
    private Label currentStockLabel;
    @FXML
    private Label minStockLevelLabel;

    private List<InventoryItem> inventoryItems;

    @FXML
    public void initialize() {
        loadInventoryItems();

        itemComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateStockInfo();
                }
        );
    }

    private void loadInventoryItems() {
        inventoryItems = DataPersistenceManager.loadObjects("inventory_items.bin");
        ObservableList<String> itemOptions = FXCollections.observableArrayList();

        for (InventoryItem item : inventoryItems) {
            if (item.isLowStock() || item.getQuantity() == 0) {
                itemOptions.add(item.getItemId() + " - " + item.getName() + " (Current: " + item.getQuantity() + ")");
            }
        }

        itemComboBox.setItems(itemOptions);
    }

    private void updateStockInfo() {
        String selectedItem = itemComboBox.getValue();
        if (selectedItem != null) {
            String itemId = selectedItem.split(" - ")[0];

            for (InventoryItem item : inventoryItems) {
                if (item.getItemId().equals(itemId)) {
                    currentStockLabel.setText("Current Stock: " + item.getQuantity() + " " + item.getUnit());
                    minStockLevelLabel.setText("Min Stock Level: " + item.getMinStockLevel() + " " + item.getUnit());
                    return;
                }
            }
        }
        currentStockLabel.setText("Current Stock: N/A");
        minStockLevelLabel.setText("Min Stock Level: N/A");
    }

    @FXML
    public void submitRequestButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String selectedItem = itemComboBox.getValue();
            String itemId = selectedItem.split(" - ")[0];
            int requestedQuantity = Integer.parseInt(requestedQuantityTextField.getText().trim());
            String remarks = remarksTextArea.getText().trim();


            InventoryItem itemToRestock = null;
            for (InventoryItem item : inventoryItems) {
                if (item.getItemId().equals(itemId)) {
                    itemToRestock = item;
                    break;
                }
            }

            if (itemToRestock == null) {
                showAlert("Error", "Selected item not found.");
                return;
            }


            itemToRestock.setQuantity(itemToRestock.getQuantity() + requestedQuantity);
            DataPersistenceManager.saveObjects(inventoryItems, "inventory_items.bin");

            showAlert("Success", "Restock request submitted and stock updated for " + itemToRestock.getName() +
                    "\nNew Quantity: " + itemToRestock.getQuantity() + " " + itemToRestock.getUnit());
            clearForm();
            loadInventoryItems();

        } catch (Exception e) {
            showAlert("Error", "Failed to submit restock request: " + e.getMessage());
        }
    }

    @FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    private boolean validateInputs() {
        if (itemComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select an item to restock.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(requestedQuantityTextField.getText().trim());
            if (quantity <= 0) {
                showAlert("Validation Error", "Requested quantity must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid numeric quantity.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        itemComboBox.setValue(null);
        requestedQuantityTextField.clear();
        remarksTextArea.clear();
        currentStockLabel.setText("Current Stock: N/A");
        minStockLevelLabel.setText("Min Stock Level: N/A");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
