package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class foodStockSummaryViewController {

    @FXML
    private TableColumn<InventoryItem, String> statusColn;
    @FXML
    private TableColumn<InventoryItem, Integer> currentStockColn;
    @FXML
    private ComboBox<String> filterByCategoryComboBox;
    @FXML
    private TextField searchItemTextField;
    @FXML
    private TableColumn<InventoryItem, String> categoryColn;
    @FXML
    private TableView<InventoryItem> foodStockInventoryTableView;
    @FXML
    private TableColumn<InventoryItem, String> itemNameColn;

    private ObservableList<InventoryItem> masterInventoryList;

    @FXML
    public void initialize() {
        // Initialize table columns
        itemNameColn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        currentStockColn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColn.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusColn.setCellValueFactory(cellData -> {
            int quantity = cellData.getValue().getQuantity();
            int minStock = cellData.getValue().getMinStockLevel();
            if (quantity <= minStock) {
                return new javafx.beans.property.SimpleStringProperty("Low Stock");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Sufficient");
            }
        });

        // Load all inventory items
        masterInventoryList = FXCollections.observableArrayList(DataPersistenceManager.loadObjects("inventory_items.dat"));
        foodStockInventoryTableView.setItems(masterInventoryList);

        // Populate category filter combo box
        populateCategoryFilter();

        // Add listener for search text field
        searchItemTextField.textProperty().addListener((observable, oldValue, newValue) -> filterItems());

        // Add listener for category combo box
        filterByCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterItems());
    }

    private void populateCategoryFilter() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add("All Categories"); // Option to view all
        masterInventoryList.stream()
                .map(InventoryItem::getCategory)
                .distinct()
                .sorted()
                .forEach(categories::add);
        filterByCategoryComboBox.setItems(categories);
        filterByCategoryComboBox.setValue("All Categories"); // Default selection
    }

    private void filterItems() {
        String searchText = searchItemTextField.getText().toLowerCase();
        String selectedCategory = filterByCategoryComboBox.getValue();

        List<InventoryItem> filteredList = masterInventoryList.stream()
                .filter(item -> {
                    boolean matchesSearch = searchText.isEmpty() || item.getItemName().toLowerCase().contains(searchText);
                    boolean matchesCategory = selectedCategory == null || selectedCategory.equals("All Categories") || item.getCategory().equals(selectedCategory);
                    return matchesSearch && matchesCategory;
                })
                .collect(Collectors.toList());

        foodStockInventoryTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void SearchOnActionButton(ActionEvent actionEvent) {
        filterItems();
    }
}