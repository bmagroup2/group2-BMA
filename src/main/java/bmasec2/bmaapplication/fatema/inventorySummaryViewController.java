package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class inventorySummaryViewController {

    @FXML
    private ComboBox<String> allCategoriesComboBox;
    @FXML
    private TableView<InventoryItem> inventorySummaryTableView;
    @FXML
    private TableColumn<InventoryItem, String> itemNameColumn;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML
    private TableColumn<InventoryItem, String> lastUpdateColumn;
    @FXML
    private PieChart inventoryCategoryPieChart;

    private static final String INVENTORY_FILE = "inventory.ser";
    private ObservableList<InventoryItem> masterInventoryList;

    @FXML
    public void initialize() {
        // Initialize table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lastUpdateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//            return javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getLastUpdated().format(formatter));
            return null;
        });

        // Load data and populate UI
        loadInventoryData();
        populateCategoriesComboBox();
        populateTableView(masterInventoryList);
        updatePieChart();
    }

    private void loadInventoryData() {
        List<InventoryItem> loadedItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        masterInventoryList = FXCollections.observableArrayList(loadedItems);
    }

    private void populateCategoriesComboBox() {
        allCategoriesComboBox.getItems().clear();
        allCategoriesComboBox.getItems().add("All Categories"); // Option to view all
        masterInventoryList.stream()
                .map(InventoryItem::getCategory)
                .distinct()
                .sorted()
                .forEach(category -> allCategoriesComboBox.getItems().add(category));
        allCategoriesComboBox.getSelectionModel().selectFirst(); // Select 'All Categories' by default
    }

    private void populateTableView(ObservableList<InventoryItem> items) {
        inventorySummaryTableView.setItems(items);
    }

    private void updatePieChart() {
        Map<String, Integer> categoryQuantities = masterInventoryList.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory, Collectors.summingInt(InventoryItem::getQuantity)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        categoryQuantities.forEach((category, totalQuantity) -> {
            pieChartData.add(new PieChart.Data(category, totalQuantity));
        });

        inventoryCategoryPieChart.setData(pieChartData);
    }

    @FXML
    void viewButtonOnAction(ActionEvent event) {
        String selectedCategory = allCategoriesComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null || selectedCategory.equals("All Categories")) {
            populateTableView(masterInventoryList);
        } else {
            ObservableList<InventoryItem> filteredList = masterInventoryList.stream()
                    .filter(item -> item.getCategory().equals(selectedCategory))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            populateTableView(filteredList);
        }
    }
}


