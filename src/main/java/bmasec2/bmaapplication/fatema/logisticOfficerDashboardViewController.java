package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.model.IssuedItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class logisticOfficerDashboardViewController {

    @FXML
    private Label itemsLowStockLabel;
    @FXML
    private Label pendingRequestLabel;
    @FXML
    private ListView<String> recentInventoryActivityListView;

    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String RESTOCK_REQUESTS_FILE = "restock_requests.ser";
    private static final String ISSUED_ITEMS_FILE = "issued_items.ser";

    @FXML
    public void initialize() {
        updateDashboard();
    }

    private void updateDashboard() {

        List<InventoryItem> inventoryItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        long lowStockCount = inventoryItems.stream()
                .filter(InventoryItem::isBelowMinStock)
                .count();
        itemsLowStockLabel.setText(lowStockCount + " Items");


        List<bmasec2.bmaapplication.fatema.RestockRequest> restockRequests = DataPersistenceManager.loadObjects(RESTOCK_REQUESTS_FILE);
        long pendingRequestsCount = restockRequests.stream()
                .filter(request -> request.getStatus().equals("Pending"))
                .count();
        pendingRequestLabel.setText(pendingRequestsCount + " Requests");


        List<IssuedItem> issuedItems = DataPersistenceManager.loadObjects(ISSUED_ITEMS_FILE);
        ObservableList<String> activityList = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        issuedItems.stream()
                .sorted((i1, i2) -> i2.getIssueDate().compareTo(i1.getIssueDate()))
                .limit(10)
                .forEach(item -> activityList.add(String.format("%s issued %d %s of %s to %s",
                        item.getIssueDate().format(formatter),
                        item.getQuantity(),
                        item.getUnit(),
                        item.getItemName(),
                        item.getIssuedToUserName()))
                );
        recentInventoryActivityListView.setItems(activityList);
    }
}


