package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.List;
import java.util.UUID;

public class addInventoryViewController {
    @javafx.fxml.FXML
    private TextField itemNameTextField;
    @javafx.fxml.FXML
    private TextField quantityTextField;
    @javafx.fxml.FXML
    private ComboBox<String> unitComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> categoryComboBox;
    @javafx.fxml.FXML
    private TextField minStockLevelTextField;
    @javafx.fxml.FXML
    private TextArea descriptionTextArea;
    @javafx.fxml.FXML
    private Label validationLabel;

    @javafx.fxml.FXML
    public void initialize() {
        unitComboBox.setItems(FXCollections.observableArrayList(
                "Pieces", "Boxes", "Kilograms", "Liters", "Meters", "Sets", "Pairs", "Units"
        ));


        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Uniforms", "Equipment", "Weapons", "Medical Supplies", "Food Items",
                "Training Materials", "Office Supplies", "Maintenance Tools", "Electronics"
        ));


        minStockLevelTextField.setText("10");


        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateQuantity();
        });

        minStockLevelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateMinStockLevel();
        });
    }

    @javafx.fxml.FXML
    public void addItemButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {

            String itemId = "ITM-" + UUID.randomUUID().toString().substring(0, 8);


            String itemName = itemNameTextField.getText().trim();
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            String unit = unitComboBox.getValue();
            String category = categoryComboBox.getValue();
            int minStockLevel = Integer.parseInt(minStockLevelTextField.getText().trim());


            InventoryItem item = new InventoryItem(itemId, itemName, quantity, unit, category, minStockLevel);


            List<InventoryItem> inventory = DataPersistenceManager.loadObjects("inventory_items.bin");
            inventory.add(item);
            DataPersistenceManager.saveObjects(inventory, "inventory_items.bin");

            showAlert("Success", "Item added successfully!\nItem ID: " + itemId);
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to add item: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @javafx.fxml.FXML
    public void checkDuplicateButtonOnAction(ActionEvent actionEvent) {
        String itemName = itemNameTextField.getText().trim();
        if (itemName.isEmpty()) {
            showAlert("Error", "Please enter an item name first.");
            return;
        }

        List<InventoryItem> inventory = DataPersistenceManager.loadObjects("inventory_items.bin");
        boolean isDuplicate = false;

        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                isDuplicate = true;
                showAlert("Duplicate Found",
                        "An item with this name already exists:\n" +
                                "Item ID: " + item.getItemId() + "\n" +
                                "Current Quantity: " + item.getQuantity() + " " + item.getUnit() + "\n" +
                                "Category: " + item.getCategory());
                return;
            }
        }

        if (!isDuplicate) {
            showAlert("No Duplicate", "No existing item found with this name. You can proceed to add it.");
        }
    }

    private void validateQuantity() {
        try {
            if (!quantityTextField.getText().trim().isEmpty()) {
                int quantity = Integer.parseInt(quantityTextField.getText().trim());
                if (quantity < 0) {
                    validationLabel.setText("Quantity cannot be negative");
                    validationLabel.setStyle("-fx-text-fill: red;");
                } else {
                    validationLabel.setText("Valid quantity");
                    validationLabel.setStyle("-fx-text-fill: green;");
                }
            } else {
                validationLabel.setText("");
            }
        } catch (NumberFormatException e) {
            validationLabel.setText("Invalid quantity format");
            validationLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void validateMinStockLevel() {
        try {
            if (!minStockLevelTextField.getText().trim().isEmpty()) {
                int minStock = Integer.parseInt(minStockLevelTextField.getText().trim());
                if (minStock < 0) {
                    validationLabel.setText("Minimum stock level cannot be negative");
                    validationLabel.setStyle("-fx-text-fill: red;");
                } else {
                    validationLabel.setText("Valid minimum stock level");
                    validationLabel.setStyle("-fx-text-fill: green;");
                }
            }
        } catch (NumberFormatException e) {
            validationLabel.setText("Invalid minimum stock level format");
            validationLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validateInputs() {
        if (itemNameTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter an item name.");
            return false;
        }

        if (itemNameTextField.getText().trim().length() < 3) {
            showAlert("Validation Error", "Item name must be at least 3 characters long.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            if (quantity < 0) {
                showAlert("Validation Error", "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid quantity (numeric value).");
            return false;
        }

        if (unitComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a unit.");
            return false;
        }

        if (categoryComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a category.");
            return false;
        }

        try {
            int minStockLevel = Integer.parseInt(minStockLevelTextField.getText().trim());
            if (minStockLevel < 0) {
                showAlert("Validation Error", "Minimum stock level cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid minimum stock level (numeric value).");
            return false;
        }

        return true;
    }

    private void clearForm() {
        itemNameTextField.clear();
        quantityTextField.clear();
        unitComboBox.setValue(null);
        categoryComboBox.setValue(null);
        minStockLevelTextField.setText("10");
        descriptionTextArea.clear();
        validationLabel.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}