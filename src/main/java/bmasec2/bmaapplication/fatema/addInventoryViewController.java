package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.model.InventoryItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.util.List;
import java.util.UUID;

public class addInventoryViewController {

    @FXML
    private TextField itemNameTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField initialQuantityTextField;
    @FXML
    private TextField unitOfMeasurementTextField;

    private static final String INVENTORY_FILE = "inventory.dat";

    @FXML
    public void initialize() {

        categoryComboBox.getItems().addAll("Uniform", "Equipment", "Ration", "Medical Supplies", "Ammunition", "Vehicle Parts", "Other");
    }

    @FXML
    void addItemToInventoryOnAction(ActionEvent event) {
        String itemName = itemNameTextField.getText();
        String category = categoryComboBox.getValue();
        String initialQuantityStr = initialQuantityTextField.getText();
        String unitOfMeasurement = unitOfMeasurementTextField.getText();

        if (itemName.isEmpty() || category == null || initialQuantityStr.isEmpty() || unitOfMeasurement.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        int initialQuantity;
        try {
            initialQuantity = Integer.parseInt(initialQuantityStr);
            if (initialQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Initial quantity must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Initial quantity must be a valid number.");
            return;
        }

        List<InventoryItem> inventoryItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);


        boolean itemExists = false;
        for (InventoryItem item : inventoryItems) {
            if (item.getItemName().equalsIgnoreCase(itemName) && item.getCategory().equalsIgnoreCase(category)) {
                itemExists = true;
                showAlert(Alert.AlertType.WARNING, "Duplicate Item", "An item with this name and category already exists. Consider updating existing stock.");
                return;
            }
        }

        String itemId = UUID.randomUUID().toString();
        InventoryItem newItem = new InventoryItem(itemId, itemName, initialQuantity, unitOfMeasurement, category, 0); // Assuming minStockLevel is 0 initially
        inventoryItems.add(newItem);
        DataPersistenceManager.saveObjects(inventoryItems, INVENTORY_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Inventory item added successfully!");


        itemNameTextField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        initialQuantityTextField.clear();
        unitOfMeasurementTextField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


