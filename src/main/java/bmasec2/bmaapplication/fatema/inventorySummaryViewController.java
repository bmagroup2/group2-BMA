package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class inventorySummaryViewController {
    @FXML
    private TableColumn<InventoryItem, String> itemNameColumn;
    @FXML
    private ComboBox<String> allCategoriesComboBox;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML
    private TableColumn<InventoryItem, Date> lastUpdateColumn;
    @FXML
    private TableView<InventoryItem> inventorySummaryTableView;
    @FXML
    private PieChart inventoryCategoryPieChart;

    private ObservableList<InventoryItem> inventoryData = FXCollections.observableArrayList();
    private List<InventoryItem> allInventoryItems;

    @FXML
    public void initialize() {
        // Initialize table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        inventorySummaryTableView.setItems(inventoryData);


        allCategoriesComboBox.setItems(FXCollections.observableArrayList(
                "All Categories", "Uniforms", "Equipment", "Weapons", "Medical Supplies",
                "Food Items", "Training Materials", "Office Supplies", "Maintenance Tools", "Electronics"
        ));
        allCategoriesComboBox.setValue("All Categories");


        loadInventoryData();
    }

    private void loadInventoryData() {
        allInventoryItems = DataPersistenceManager.loadObjects("inventory_items.bin");
        applyFilterAndRefresh();
        updatePieChart();
    }

    @FXML
    public void viewButtonOnAction(ActionEvent actionEvent) {
        applyFilterAndRefresh();
        updatePieChart();
    }

    private void applyFilterAndRefresh() {
        inventoryData.clear();
        String selectedCategory = allCategoriesComboBox.getValue();

        if ("All Categories".equals(selectedCategory)) {
            inventoryData.addAll(allInventoryItems);
        } else {
            inventoryData.addAll(allInventoryItems.stream()
                    .filter(item -> item.getCategory().equals(selectedCategory))
                    .collect(Collectors.toList()));
        }
    }

    private void updatePieChart() {
        inventoryCategoryPieChart.getData().clear();

        Map<String, Integer> categoryQuantities = allInventoryItems.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory, Collectors.summingInt(InventoryItem::getQuantity)));

        for (Map.Entry<String, Integer> entry : categoryQuantities.entrySet()) {
            inventoryCategoryPieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
