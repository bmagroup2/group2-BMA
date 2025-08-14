package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class restockRequestViewController {

    @FXML
    private ListView<InventoryItem> itemsWithLowStockListView;
    @FXML
    private Label itemNameLabel;
    @FXML
    private TextField quantityToRequestTextField;
    @FXML
    private TextArea remarksTextArea;

    private static final String INVENTORY_FILE = "inventory.dat";
    private static final String RESTOCK_REQUESTS_FILE = "restock_requests.dat";

    private ObservableList<InventoryItem> lowStockItems;

    @FXML
    public void initialize() {
        loadLowStockItems();
        itemsWithLowStockListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        itemNameLabel.setText("Item: " + newValue.getItemName());
                    } else {
                        itemNameLabel.setText("Item: [Selected Item Name]");
                    }
                });
    }

    private void loadLowStockItems() {
        List<InventoryItem> allItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        lowStockItems = allItems.stream()
                .filter(InventoryItem::isBelowMinStock)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        itemsWithLowStockListView.setItems(lowStockItems);
    }

    @FXML
    void submitRestockRequestButtonOnAction(ActionEvent event) {
        InventoryItem selectedItem = itemsWithLowStockListView.getSelectionModel().getSelectedItem();
        String quantityStr = quantityToRequestTextField.getText();
        String remarks = remarksTextArea.getText();

        if (selectedItem == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an item from the low stock list.");
            return;
        }

        if (quantityStr.isEmpty() || remarks.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        int requestedQuantity;
        try {
            requestedQuantity = Integer.parseInt(quantityStr);
            if (requestedQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity to request must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity to request must be a valid number.");
            return;
        }

        List<bmasec2.bmaapplication.fatema.RestockRequest> restockRequests = DataPersistenceManager.loadObjects(RESTOCK_REQUESTS_FILE);


        boolean existingRequest = restockRequests.stream()
                .anyMatch(req -> req.getItemId().equals(selectedItem.getItemId()) && req.getStatus().equals("Pending"));

        if (existingRequest) {
            showAlert(Alert.AlertType.WARNING, "Existing Request", "A pending restock request for this item already exists.");
            return;
        }

        String requestId = UUID.randomUUID().toString();
        bmasec2.bmaapplication.fatema.RestockRequest newRequest = new bmasec2.bmaapplication.fatema.RestockRequest(requestId, selectedItem.getItemId(), selectedItem.getItemName(), requestedQuantity, remarks);
        restockRequests.add(newRequest);
        DataPersistenceManager.saveObjects(restockRequests, RESTOCK_REQUESTS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Restock request submitted successfully!");

        // Clear form and refresh list
        quantityToRequestTextField.clear();
        remarksTextArea.clear();
        itemNameLabel.setText("Item: [Selected Item Name]");
        loadLowStockItems();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


