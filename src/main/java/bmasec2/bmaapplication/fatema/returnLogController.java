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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;
//
public class returnLogController {

    @FXML
    private TableView<bmasec2.bmaapplication.fatema.ReturnLogEntry> logHistoryTableView;
    @FXML
    private TableColumn<bmasec2.bmaapplication.fatema.ReturnLogEntry, String> dateTableColumn;
    @FXML
    private TableColumn<bmasec2.bmaapplication.fatema.ReturnLogEntry, String> itemNameTableColumn;
    @FXML
    private TableColumn<bmasec2.bmaapplication.fatema.ReturnLogEntry, String> returnedbyTableColumn;
    @FXML
    private ComboBox<InventoryItem> selecteditemComboBox;
    @FXML
    private TextArea reasonForDamageTextArea;

    private static final String RETURN_LOG_FILE = "return_log.ser";
    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String USERS_FILE = "users.ser"; // Assuming a users file exists

    private ObservableList<bmasec2.bmaapplication.fatema.ReturnLogEntry> returnLogEntries;
    private ObservableList<InventoryItem> inventoryItems;
    private ObservableList<User> users;
//
//    @FXML
//    public void initialize() {
//        // Initialize table columns
//        dateTableColumn.setCellValueFactory(cellData -> {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//            return javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getLogDate().format(formatter));
//        });
//        itemNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
//        returnedbyTableColumn.setCellValueFactory(new PropertyValueFactory<>("returnedByUserName"));
//
//        loadData();
//        populateComboBoxes();
//        populateTableView();
//    }
//
//    private void loadData() {
//        returnLogEntries = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(RETURN_LOG_FILE));
//        inventoryItems = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(INVENTORY_FILE));
//        users = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(USERS_FILE));
//    }
//
//    private void populateComboBoxes() {
//        selecteditemComboBox.setItems(inventoryItems);
//        selecteditemComboBox.setConverter(new javafx.util.StringConverter<InventoryItem>() {
//            @Override
//            public String toString(InventoryItem item) {
//                return item != null ? item.getItemName() : "";
//            }
//
//            @Override
//            public InventoryItem fromString(String string) {
//                return inventoryItems.stream()
//                        .filter(item -> item.getItemName().equals(string))
//                        .findFirst()
//                        .orElse(null);
//            }
//        });
//    }
//
//    private void populateTableView() {
//        logHistoryTableView.setItems(returnLogEntries);
//    }
//
//    @FXML
//    void saveToLogOnAction(ActionEvent event) {
//        InventoryItem selectedItem = selecteditemComboBox.getSelectionModel().getSelectedItem();
//        String reason = reasonForDamageTextArea.getText();
//
//        // For simplicity, let's assume a dummy user for now. In a real app, this would be the logged-in user.
//        // You might need to pass the logged-in user from the main application to this controller.
////        User currentUser = users.isEmpty() ? new User("dummyId", "Dummy User", "dummy@example.com", "Unknown", "password", "Active") { // Anonymous inner class for abstract User
////            @Override
////            public boolean login(String username, String password) { return false; }
////            @Override
////            public void logout() {}
////            @Override
////            public boolean updateProfile(java.util.Map<String, String> updateData) { return false; }
////            @Override
////            public void resetPassword() {}
////            @Override
////            public boolean verifyPassword(String password) { return false; }
////        } : users.get(0); // Get the first user as a placeholder
//
//        if (selectedItem == null || reason.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Form Error", "Please select an item and provide a reason.");
//            return;
//        }
//
//        String logId = UUID.randomUUID().toString();
//        bmasec2.bmaapplication.fatema.ReturnLogEntry newEntry = new bmasec2.bmaapplication.fatema.ReturnLogEntry(logId, selectedItem.getItemId(), selectedItem.getItemName(),
////                currentUser.getUserId(), currentUser.getName(), reason);
//
////        returnLogEntries.add(newEntry);
////        DataPersistenceManager.saveObjects(returnLogEntries.stream().collect(Collectors.toList()), RETURN_LOG_FILE);
//
////        showAlert(Alert.AlertType.INFORMATION, "Success", "Entry saved to log successfully!");
//
//        // Clear form and refresh table
//        selecteditemComboBox.getSelectionModel().clearSelection();
//        reasonForDamageTextArea.clear();
//        populateTableView();
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
}
//
//
