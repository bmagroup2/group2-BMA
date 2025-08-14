package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.model.Notification;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class notifyCommandantViewController {

    @FXML
    private ComboBox<InventoryItem> chooseAnItemComboBox;
    @FXML
    private TextArea messsageTextArea;

    private static final String INVENTORY_FILE = "inventory.dat";
    private static final String NOTIFICATIONS_FILE = "notifications.dat";

    private ObservableList<InventoryItem> lowStockItems;

    @FXML
    public void initialize() {
        loadLowStockItems();
        populateComboBox();
    }

    private void loadLowStockItems() {
        List<InventoryItem> allItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        lowStockItems = allItems.stream()
                .filter(InventoryItem::isBelowMinStock)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private void populateComboBox() {
        chooseAnItemComboBox.setItems(lowStockItems);
        chooseAnItemComboBox.setConverter(new javafx.util.StringConverter<InventoryItem>() {
            @Override
            public String toString(InventoryItem item) {
                return item != null ? item.getItemName() + " (Qty: " + item.getQuantity() + ")" : "";
            }

            @Override
            public InventoryItem fromString(String string) {
                return lowStockItems.stream()
                        .filter(item -> (item.getItemName() + " (Qty: " + item.getQuantity() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void sendUrgentAlertButtonOnAction(ActionEvent event) {
        InventoryItem selectedItem = chooseAnItemComboBox.getSelectionModel().getSelectedItem();
        String message = messsageTextArea.getText();

        if (selectedItem == null || message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please select an item and provide a message.");
            return;
        }


        String senderId = "logisticOfficer123"; 
        String senderName = "Logistic Officer"; 
        String recipientRole = "Commandant";
        String subject = "URGENT: Critical Stock Shortage - " + selectedItem.getItemName();

        String notificationId = UUID.randomUUID().toString();
        Notification newNotification = new Notification(
                notificationId, senderId, senderName, recipientRole, subject, message, selectedItem.getItemId());

        List<Notification> notifications = DataPersistenceManager.loadObjects(NOTIFICATIONS_FILE);
        notifications.add(newNotification);
        DataPersistenceManager.saveObjects(notifications, NOTIFICATIONS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Urgent alert sent to Commandant successfully!");


        chooseAnItemComboBox.getSelectionModel().clearSelection();
        messsageTextArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


