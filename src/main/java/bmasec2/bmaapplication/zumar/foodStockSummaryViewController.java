package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.FoodStock;
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
    private TableColumn<FoodStock, String> statusColn;
    @FXML
    private TableColumn<FoodStock, Integer> currentStockColn;
    @FXML
    private ComboBox<String> filterByCategoryComboBox;
    @FXML
    private TextField searchItemTextField;
    @FXML
    private TableColumn<FoodStock, String> categoryColn;
    @FXML
    private TableView<FoodStock> foodStockInventoryTableView;
    @FXML
    private TableColumn<FoodStock, String> itemNameColn;

    private ObservableList<FoodStock> masterInventoryList;

    @FXML
    public void initialize() {

        itemNameColn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        currentStockColn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColn.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusColn.setCellValueFactory(cellData -> {
            int quantity = cellData.getValue().getQuantity();
            if (quantity <= 10) {
                return new javafx.beans.property.SimpleStringProperty("Low Stock");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Sufficient");
            }
        });

        masterInventoryList = FXCollections.observableArrayList(DataPersistenceManager.loadObjects("food_stocks.dat"));
        foodStockInventoryTableView.setItems(masterInventoryList);


        populateCategoryFilter();


        searchItemTextField.textProperty().addListener((observable, oldValue, newValue) -> filterItems());


        filterByCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterItems());
    }

    private void populateCategoryFilter() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add("All Categories");
        masterInventoryList.stream()
                .map(FoodStock::getCategory)
                .distinct()
                .sorted()
                .forEach(categories::add);
        filterByCategoryComboBox.setItems(categories);
        filterByCategoryComboBox.setValue("All Categories");
    }

    private void filterItems() {
        String searchText = searchItemTextField.getText().toLowerCase();
        String selectedCategory = filterByCategoryComboBox.getValue();

        List<FoodStock> filteredList = masterInventoryList.stream()
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