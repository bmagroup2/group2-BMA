package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.stream.Collectors;

public class logisticOfficerDashboardViewController {
    @FXML
    private Label pendingRequestLabel;
    @FXML
    private Label itemsLowStockLabel;
    @FXML
    private ListView<String> recentInventoryActivityListView;
    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label totalCategoriesLabel;
    @FXML
    private Label criticalStockLabel;

    private ObservableList<String> recentActivitiesData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupListView();
        loadDashboardData();
    }

    private void setupListView() {
        recentInventoryActivityListView.setItems(recentActivitiesData);
    }

    private void loadDashboardData() {
        loadInventoryStatistics();
        loadRecentActivities();
    }

    private void loadInventoryStatistics() {
        List<InventoryItem> inventory = DataPersistenceManager.loadObjects("inventory_items.bin");


        totalItemsLabel.setText(String.valueOf(inventory.size()));


        long categoriesCount = inventory.stream()
                .map(InventoryItem::getCategory)
                .distinct()
                .count();
        totalCategoriesLabel.setText(String.valueOf(categoriesCount));


        long lowStockCount = inventory.stream()
                .filter(InventoryItem::isLowStock)
                .count();
        itemsLowStockLabel.setText(String.valueOf(lowStockCount));


        if (lowStockCount > 0) {
            itemsLowStockLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            itemsLowStockLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }


        long criticalStockCount = inventory.stream()
                .filter(item -> item.getQuantity() == 0)
                .count();
        criticalStockLabel.setText(String.valueOf(criticalStockCount));

        if (criticalStockCount > 0) {
            criticalStockLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            criticalStockLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }


        pendingRequestLabel.setText("3");
    }

    private void loadRecentActivities() {
        recentActivitiesData.clear();


        recentActivitiesData.addAll(
                "Added new item: Combat Boots (50 pairs)",
                "Issued 20 Uniforms to Batch 2024-A",
                "Restocked Medical Supplies (100 units)",
                "Updated minimum stock level for Weapons",
                "Generated monthly logistics report",
                "Processed return: Damaged Equipment (5 items)",
                "Notified Commandant about critical shortage",
                "Approved restock request for Training Materials"
        );
    }

    @FXML
    public void refreshDashboardButtonOnAction() {
        loadDashboardData();
        showNotification("Dashboard refreshed successfully!");
    }

    @FXML
    public void viewLowStockButtonOnAction() {

        showNotification("This would open the Low Stock Items view.");
    }

    @FXML
    public void quickAddItemButtonOnAction() {

        showNotification("This would open the Quick Add Item dialog.");
    }

    @FXML
    public void generateReportButtonOnAction() {

        showNotification("This would open the Report Generation view.");
    }

    private void showNotification(String message) {

        System.out.println("Notification: " + message);
    }
}
