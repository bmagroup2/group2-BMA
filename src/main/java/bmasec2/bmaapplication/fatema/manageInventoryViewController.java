package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class manageInventoryViewController {
    @javafx.fxml.FXML
    private TableView<InventoryItem> inventoryTableView;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, String> itemIdColumn;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, String> itemNameColumn;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, String> unitColumn;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, String> categoryColumn;
    @javafx.fxml.FXML
    private TableColumn<InventoryItem, Integer> minStockColumn;

    @javafx.fxml.FXML
    private TextField itemIdTextField;
    @javafx.fxml.FXML
    private TextField itemNameTextField;
    @javafx.fxml.FXML
    private TextField quantityTextField;
    @javafx.fxml.FXML
    private ComboBox<String> unitComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> categoryComboBox;
    @javafx.fxml.FXML
    private TextField minStockTextField;
    @javafx.fxml.FXML
    private ComboBox<String> filterCategoryComboBox;
    @javafx.fxml.FXML
    private CheckBox lowStockOnlyCheckBox;

    private ObservableList<InventoryItem> inventoryData = FXCollections.observableArrayList();
    private ObservableList<InventoryItem> allInventoryData = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    public void initialize() {

        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minStockLevel"));

        inventoryTableView.setItems(inventoryData);


        unitComboBox.setItems(FXCollections.observableArrayList(
                "Pieces", "Boxes", "Kilograms", "Liters", "Meters", "Sets", "Pairs", "Units"
        ));

        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Uniforms", "Equipment", "Weapons", "Medical Supplies", "Food Items",
                "Training Materials", "Office Supplies", "Maintenance Tools", "Electronics"
        ));

        filterCategoryComboBox.setItems(FXCollections.observableArrayList(
                "All Categories", "Uniforms", "Equipment", "Weapons", "Medical Supplies",
                "Food Items", "Training Materials", "Office Supplies", "Maintenance Tools", "Electronics"
        ));
        filterCategoryComboBox.setValue("All Categories");


        inventoryTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );


        loadInventoryData();
    }

    private void loadInventoryData() {
        List<InventoryItem> inventory = DataPersistenceManager.loadObjects("inventory_items.bin");
        allInventoryData.clear();
        allInventoryData.addAll(inventory);
        applyFilters();
    }

    private void populateFields(InventoryItem item) {
        itemIdTextField.setText(item.getItemId());
        itemNameTextField.setText(item.getName());
        quantityTextField.setText(String.valueOf(item.getQuantity()));
        unitComboBox.setValue(item.getUnit());
        categoryComboBox.setValue(item.getCategory());
        minStockTextField.setText(String.valueOf(item.getMinStockLevel()));
    }

    @javafx.fxml.FXML
    public void updateItemButtonOnAction(ActionEvent actionEvent) {
        InventoryItem selectedItem = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to update.");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {

            selectedItem.setName(itemNameTextField.getText().trim());
            selectedItem.setQuantity(Integer.parseInt(quantityTextField.getText().trim()));
            selectedItem.setUnit(unitComboBox.getValue());
            selectedItem.setCategory(categoryComboBox.getValue());
            selectedItem.setMinStockLevel(Integer.parseInt(minStockTextField.getText().trim()));


            DataPersistenceManager.saveObjects(allInventoryData, "inventory_items.bin");


            inventoryTableView.refresh();

            showAlert("Success", "Item updated successfully!");
            clearFields();

        } catch (Exception e) {
            showAlert("Error", "Failed to update item: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void deleteItemButtonOnAction(ActionEvent actionEvent) {
        InventoryItem selectedItem = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Inventory Item");
        confirmAlert.setContentText("Are you sure you want to delete this item?\n\n" +
                "Item: " + selectedItem.getName() + "\n" +
                "ID: " + selectedItem.getItemId());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                allInventoryData.remove(selectedItem);
                DataPersistenceManager.saveObjects(allInventoryData, "inventory_items.bin");

                applyFilters();
                clearFields();

                showAlert("Success", "Item deleted successfully!");

            } catch (Exception e) {
                showAlert("Error", "Failed to delete item: " + e.getMessage());
            }
        }
    }

    @javafx.fxml.FXML
    public void refreshTableButtonOnAction(ActionEvent actionEvent) {
        loadInventoryData();
        clearFields();
    }

    @javafx.fxml.FXML
    public void applyFiltersButtonOnAction(ActionEvent actionEvent) {
        applyFilters();
    }

    @javafx.fxml.FXML
    public void clearFieldsButtonOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    @javafx.fxml.FXML
    public void adjustStockButtonOnAction(ActionEvent actionEvent) {
        InventoryItem selectedItem = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to adjust stock.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adjust Stock");
        dialog.setHeaderText("Adjust Stock for: " + selectedItem.getName());
        dialog.setContentText("Enter adjustment amount (positive to add, negative to subtract):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int adjustment = Integer.parseInt(result.get());
                int newQuantity = selectedItem.getQuantity() + adjustment;

                if (newQuantity < 0) {
                    showAlert("Error", "Adjustment would result in negative stock. Current stock: " +
                            selectedItem.getQuantity());
                    return;
                }

                selectedItem.setQuantity(newQuantity);
                DataPersistenceManager.saveObjects(allInventoryData, "inventory_items.bin");

                inventoryTableView.refresh();
                populateFields(selectedItem);

                showAlert("Success", "Stock adjusted successfully!\nNew quantity: " + newQuantity);

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid numeric adjustment amount.");
            }
        }
    }

    private void applyFilters() {
        inventoryData.clear();

        String selectedCategory = filterCategoryComboBox.getValue();
        boolean lowStockOnly = lowStockOnlyCheckBox.isSelected();

        for (InventoryItem item : allInventoryData) {
            boolean matchesCategory = "All Categories".equals(selectedCategory) ||
                    item.getCategory().equals(selectedCategory);
            boolean matchesLowStock = !lowStockOnly || item.isLowStock();

            if (matchesCategory && matchesLowStock) {
                inventoryData.add(item);
            }
        }
    }

    private boolean validateInputs() {
        if (itemNameTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter an item name.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            if (quantity < 0) {
                showAlert("Validation Error", "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid quantity.");
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
            int minStock = Integer.parseInt(minStockTextField.getText().trim());
            if (minStock < 0) {
                showAlert("Validation Error", "Minimum stock level cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid minimum stock level.");
            return false;
        }

        return true;
    }

    private void clearFields() {
        itemIdTextField.clear();
        itemNameTextField.clear();
        quantityTextField.clear();
        unitComboBox.setValue(null);
        categoryComboBox.setValue(null);
        minStockTextField.clear();
        inventoryTableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
