package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class issueItemsViewController {
    @javafx.fxml.FXML
    private ComboBox<String> recipientComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> itemComboBox;
    @javafx.fxml.FXML
    private TextField quantityTextField;
    @javafx.fxml.FXML
    private TextArea purposeTextArea;
    @javafx.fxml.FXML
    private Label availableStockLabel;
    @javafx.fxml.FXML
    private TableView<IssueRecord> issueHistoryTableView;
    @javafx.fxml.FXML
    private TableColumn<IssueRecord, String> issueIdColumn;
    @javafx.fxml.FXML
    private TableColumn<IssueRecord, String> recipientColumn;
    @javafx.fxml.FXML
    private TableColumn<IssueRecord, String> itemColumn;
    @javafx.fxml.FXML
    private TableColumn<IssueRecord, Integer> quantityColumn;
    @javafx.fxml.FXML
    private TableColumn<IssueRecord, Date> dateColumn;

    private ObservableList<IssueRecord> issueHistory = FXCollections.observableArrayList();
    private List<InventoryItem> inventoryItems;

    @javafx.fxml.FXML
    public void initialize() {
        // Initialize table columns
        issueIdColumn.setCellValueFactory(new PropertyValueFactory<>("issueId"));
        recipientColumn.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        issueHistoryTableView.setItems(issueHistory);


        recipientComboBox.setItems(FXCollections.observableArrayList(
                "CDT-001 - John Smith", "CDT-002 - Jane Doe", "CDT-003 - Mike Johnson",
                "CDT-004 - Sarah Wilson", "CDT-005 - David Brown", "INST-001 - Training Instructor",
                "STAFF-001 - Administrative Staff", "MAINT-001 - Maintenance Team"
        ));


        loadInventoryItems();


        itemComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateAvailableStock();
                }
        );


        loadIssueHistory();
    }

    private void loadInventoryItems() {
        inventoryItems = DataPersistenceManager.loadObjects("inventory_items.bin");
        ObservableList<String> itemOptions = FXCollections.observableArrayList();

        for (InventoryItem item : inventoryItems) {
            if (item.getQuantity() > 0) {
                itemOptions.add(item.getItemId() + " - " + item.getName() + " (" + item.getQuantity() + " " + item.getUnit() + ")");
            }
        }

        itemComboBox.setItems(itemOptions);
    }

    private void updateAvailableStock() {
        String selectedItem = itemComboBox.getValue();
        if (selectedItem != null) {
            String itemId = selectedItem.split(" - ")[0];

            for (InventoryItem item : inventoryItems) {
                if (item.getItemId().equals(itemId)) {
                    availableStockLabel.setText("Available: " + item.getQuantity() + " " + item.getUnit());

                    if (item.isLowStock()) {
                        availableStockLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        availableStockLabel.setStyle("-fx-text-fill: green;");
                    }
                    return;
                }
            }
        }
        availableStockLabel.setText("Available: -");
        availableStockLabel.setStyle("-fx-text-fill: black;");
    }

    @javafx.fxml.FXML
    public void issueItemButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String recipient = recipientComboBox.getValue();
            String selectedItem = itemComboBox.getValue();
            String itemId = selectedItem.split(" - ")[0];
            int issueQuantity = Integer.parseInt(quantityTextField.getText().trim());
            String purpose = purposeTextArea.getText().trim();


            InventoryItem item = null;
            for (InventoryItem invItem : inventoryItems) {
                if (invItem.getItemId().equals(itemId)) {
                    item = invItem;
                    break;
                }
            }

            if (item == null) {
                showAlert("Error", "Selected item not found in inventory.");
                return;
            }


            if (item.getQuantity() < issueQuantity) {
                showAlert("Error", "Insufficient stock. Available: " + item.getQuantity() + " " + item.getUnit());
                return;
            }

            if (item.issue(recipient, issueQuantity)) {

                DataPersistenceManager.saveObjects(inventoryItems, "inventory_items.bin");


                String issueId = "ISS-" + UUID.randomUUID().toString().substring(0, 8);
                IssueRecord record = new IssueRecord(issueId, recipient, item.getName(), issueQuantity, new Date(), purpose);
                issueHistory.add(0, record); // Add to beginning of list


                saveIssueHistory();

                showAlert("Success", "Item issued successfully!\nIssue ID: " + issueId +
                        "\nRemaining stock: " + item.getQuantity() + " " + item.getUnit());


                if (item.isLowStock()) {
                    showAlert("Low Stock Warning", "Warning: " + item.getName() +
                            " is now below minimum stock level (" + item.getMinStockLevel() + ").\n" +
                            "Current stock: " + item.getQuantity() + " " + item.getUnit());
                }

                clearForm();
                loadInventoryItems(); // Refresh item list
                updateAvailableStock();

            } else {
                showAlert("Error", "Failed to issue item. Please try again.");
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to issue item: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @javafx.fxml.FXML
    public void refreshInventoryButtonOnAction(ActionEvent actionEvent) {
        loadInventoryItems();
        updateAvailableStock();
    }

    @javafx.fxml.FXML
    public void checkStockButtonOnAction(ActionEvent actionEvent) {
        String selectedItem = itemComboBox.getValue();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item first.");
            return;
        }

        String itemId = selectedItem.split(" - ")[0];

        for (InventoryItem item : inventoryItems) {
            if (item.getItemId().equals(itemId)) {
                String stockInfo = "Item: " + item.getName() + "\n" +
                        "Current Stock: " + item.getQuantity() + " " + item.getUnit() + "\n" +
                        "Minimum Stock Level: " + item.getMinStockLevel() + " " + item.getUnit() + "\n" +
                        "Category: " + item.getCategory() + "\n" +
                        "Last Updated: " + item.getLastUpdated();

                if (item.isLowStock()) {
                    stockInfo += "\n\nWARNING: Stock is below minimum level!";
                }

                showAlert("Stock Information", stockInfo);
                return;
            }
        }
    }

    private void loadIssueHistory() {

        issueHistory.clear();
    }

    private void saveIssueHistory() {

    }

    private boolean validateInputs() {
        if (recipientComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a recipient.");
            return false;
        }

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
            showAlert("Validation Error", "Please enter a valid quantity.");
            return false;
        }

        if (purposeTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter the purpose for issuing this item.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        recipientComboBox.setValue(null);
        itemComboBox.setValue(null);
        quantityTextField.clear();
        purposeTextArea.clear();
        availableStockLabel.setText("Available: -");
        availableStockLabel.setStyle("-fx-text-fill: black;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static class IssueRecord {
        private String issueId;
        private String recipient;
        private String itemName;
        private int quantity;
        private Date issueDate;
        private String purpose;

        public IssueRecord(String issueId, String recipient, String itemName, int quantity, Date issueDate, String purpose) {
            this.issueId = issueId;
            this.recipient = recipient;
            this.itemName = itemName;
            this.quantity = quantity;
            this.issueDate = issueDate;
            this.purpose = purpose;
        }

        // Getters
        public String getIssueId() { return issueId; }
        public String getRecipient() { return recipient; }
        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public Date getIssueDate() { return issueDate; }
        public String getPurpose() { return purpose; }
    }
}
