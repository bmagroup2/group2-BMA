package bmasec2.bmaapplication.fatema;

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
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class manageInventoryViewController {

    @FXML
    private TextField selectItemNameTextField;
    @FXML
    private ComboBox<String> filterByCategoryComboBox;
    @FXML
    private TableView<InventoryItem> manageInventoryTableView;
    @FXML
    private TableColumn<InventoryItem, String> itemIdTableColumn;
    @FXML
    private TableColumn<InventoryItem, String> itemNameTableColumn;
    @FXML
    private TableColumn<InventoryItem, String> categoryTableColumn;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityTableColumn;
    @FXML
    private TableColumn<InventoryItem, String> statusTableColumn;

    private static final String INVENTORY_FILE = "inventory.dat";
    private ObservableList<InventoryItem> masterInventoryList;

    @FXML
    public void initialize() {
        
        itemIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusTableColumn.setCellValueFactory(cellData -> {
            InventoryItem item = cellData.getValue();
            String status = item.getQuantity() <= item.getMinStockLevel() ? "Low Stock" : "In Stock";
            return new javafx.beans.binding.StringBinding() {
                @Override
                protected String computeValue() {
                    return status;
                }
            };
        });

        loadInventoryData();
        populateCategoriesComboBox();
        populateTableView(masterInventoryList);
    }

    private void loadInventoryData() {
        List<InventoryItem> loadedItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        masterInventoryList = FXCollections.observableArrayList(loadedItems);
    }

    private void populateCategoriesComboBox() {
        filterByCategoryComboBox.getItems().clear();
        filterByCategoryComboBox.getItems().add("All Categories");
        masterInventoryList.stream()
                .map(InventoryItem::getCategory)
                .distinct()
                .sorted()
                .forEach(category -> filterByCategoryComboBox.getItems().add(category));
        filterByCategoryComboBox.getSelectionModel().selectFirst();
    }

    private void populateTableView(ObservableList<InventoryItem> items) {
        manageInventoryTableView.setItems(items);
    }

    @FXML
    void searchButtonOnAction(ActionEvent event) {
        String searchText = selectItemNameTextField.getText().toLowerCase();
        String selectedCategory = filterByCategoryComboBox.getSelectionModel().getSelectedItem();

        ObservableList<InventoryItem> filteredList = masterInventoryList.stream()
                .filter(item -> item.getItemName().toLowerCase().contains(searchText))
                .filter(item -> selectedCategory == null || selectedCategory.equals("All Categories") || item.getCategory().equals(selectedCategory))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        populateTableView(filteredList);
    }

    @FXML
    void updateSelectedStockOnAction(ActionEvent event) {
        InventoryItem selectedItem = manageInventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            showAlert(Alert.AlertType.INFORMATION, "Update Stock", "Simulating update for: " + selectedItem.getItemName() + ".\nIn a full application, a new window would open to modify stock.");

             DataPersistenceManager.saveObjects(new ArrayList<>(masterInventoryList), INVENTORY_FILE);
             populateTableView(masterInventoryList);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


