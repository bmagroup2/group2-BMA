package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class issueItemsViewController {

    @FXML
    private ComboBox<InventoryItem> selectItemComboBox;
    @FXML
    private ComboBox<User> issueToComboBox;
    @FXML
    private TextField quantityToIssueTextField;
    @FXML
    private TextArea purposeTextArea;

    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String ISSUED_ITEMS_FILE = "issued_items.ser";
    private static final String USERS_FILE = "users.ser"; // Assuming a users file exists

    private ObservableList<InventoryItem> inventoryList;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        loadData();
        populateComboBoxes();
    }

    private void loadData() {
        inventoryList = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(INVENTORY_FILE));
        userList = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(USERS_FILE));
    }

    private void populateComboBoxes() {
        selectItemComboBox.setItems(inventoryList);
        selectItemComboBox.setConverter(new javafx.util.StringConverter<InventoryItem>() {
            @Override
            public String toString(InventoryItem item) {
                return item != null ? item.getItemName() + " (" + item.getQuantity() + " " + item.getUnit() + ")" : "";
            }

            @Override
            public InventoryItem fromString(String string) {
                return inventoryList.stream()
                        .filter(item -> (item.getItemName() + " (" + item.getQuantity() + " " + item.getUnit() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        issueToComboBox.setItems(userList);
        issueToComboBox.setConverter(new javafx.util.StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getName() + " (" + user.getRole() + ")" : "";
            }

            @Override
            public User fromString(String string) {
                return userList.stream()
                        .filter(user -> (user.getName() + " (" + user.getRole() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void confirmAndissueOnAction(ActionEvent event) {
        InventoryItem selectedItem = selectItemComboBox.getSelectionModel().getSelectedItem();
        User selectedUser = issueToComboBox.getSelectionModel().getSelectedItem();
        String quantityStr = quantityToIssueTextField.getText();
        String purpose = purposeTextArea.getText();

        if (selectedItem == null || selectedUser == null || quantityStr.isEmpty() || purpose.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        int quantityToIssue;
        try {
            quantityToIssue = Integer.parseInt(quantityStr);
            if (quantityToIssue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity to issue must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity to issue must be a valid number.");
            return;
        }

        if (selectedItem.getQuantity() < quantityToIssue) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Stock", "Not enough \"" + selectedItem.getItemName() + "\" in stock. Available: " + selectedItem.getQuantity());
            return;
        }

        // Update inventory
        selectedItem.setQuantity(selectedItem.getQuantity() - quantityToIssue);
        DataPersistenceManager.saveObjects(inventoryList.stream().collect(Collectors.toList()), INVENTORY_FILE);

        // Record issued item
        String issueId = UUID.randomUUID().toString();
        bmasec2.bmaapplication.fatema.IssuedItem issuedItem = new bmasec2.bmaapplication.fatema.IssuedItem(issueId, selectedItem.getItemId(), selectedItem.getItemName(),
                selectedUser.getUserId(), selectedUser.getName(), quantityToIssue, selectedItem.getUnit(), purpose);

        List<bmasec2.bmaapplication.fatema.IssuedItem> issuedItems = DataPersistenceManager.loadObjects(ISSUED_ITEMS_FILE);
        issuedItems.add(issuedItem);
        DataPersistenceManager.saveObjects(issuedItems, ISSUED_ITEMS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Item issued successfully!");

        // Refresh UI
        loadData(); // Reload data to update quantities in combo box
        populateComboBoxes();
        quantityToIssueTextField.clear();
        purposeTextArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


