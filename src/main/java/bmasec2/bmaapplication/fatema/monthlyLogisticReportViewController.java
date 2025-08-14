package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.model.IssuedItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class monthlyLogisticReportViewController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private DatePicker monthDatePicker;
    @FXML
    private Label reportSummaryLabel;
    @FXML
    private PieChart itemUsageBycategoryPieChart;

    private static final String INVENTORY_FILE = "inventory.dat";
    private static final String ISSUED_ITEMS_FILE = "issued_items.dat";

    @FXML
    public void initialize() {
        reportTypeComboBox.getItems().addAll("Usage", "Stock", "Issuance");
        reportTypeComboBox.getSelectionModel().selectFirst();
        reportSummaryLabel.setText("Report Summary");
    }

    @FXML
    void generateReportButtonOnAction(ActionEvent event) {
        String reportType = reportTypeComboBox.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = monthDatePicker.getValue();

        if (selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a month for the report.");
            return;
        }

        int month = selectedDate.getMonthValue();
        int year = selectedDate.getYear();

        reportSummaryLabel.setText(String.format("Report Summary for %s %d", selectedDate.getMonth().name(), year));

        switch (reportType) {
            case "Usage":
                generateUsageReport(month, year);
                break;
            case "Stock":
                generateStockReport();
                break;
            case "Issuance":
                generateIssuanceReport(month, year);
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Invalid Report Type", "Please select a valid report type.");
        }
    }

    private void generateUsageReport(int month, int year) {
        List<IssuedItem> issuedItems = DataPersistenceManager.loadObjects(ISSUED_ITEMS_FILE);
        Map<String, Integer> usageByCategory = issuedItems.stream()
                .filter(item -> item.getIssueDate().getMonthValue() == month && item.getIssueDate().getYear() == year)
                .collect(Collectors.groupingBy(IssuedItem::getItemName, Collectors.summingInt(IssuedItem::getQuantity)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        usageByCategory.forEach((itemName, totalQuantity) -> {
            pieChartData.add(new PieChart.Data(itemName, totalQuantity));
        });
        itemUsageBycategoryPieChart.setData(pieChartData);
        itemUsageBycategoryPieChart.setTitle("Item Usage by Item Name");
    }

    private void generateStockReport() {
        List<InventoryItem> inventoryItems = DataPersistenceManager.loadObjects(INVENTORY_FILE);
        Map<String, Integer> stockByCategory = inventoryItems.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory, Collectors.summingInt(InventoryItem::getQuantity)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        stockByCategory.forEach((category, totalQuantity) -> {
            pieChartData.add(new PieChart.Data(category, totalQuantity));
        });
        itemUsageBycategoryPieChart.setData(pieChartData);
        itemUsageBycategoryPieChart.setTitle("Current Stock by Category");
    }

    private void generateIssuanceReport(int month, int year) {
        List<IssuedItem> issuedItems = DataPersistenceManager.loadObjects(ISSUED_ITEMS_FILE);
        Map<String, Long> issuanceByRecipient = issuedItems.stream()
                .filter(item -> item.getIssueDate().getMonthValue() == month && item.getIssueDate().getYear() == year)
                .collect(Collectors.groupingBy(IssuedItem::getIssuedToUserName, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        issuanceByRecipient.forEach((recipient, count) -> {
            pieChartData.add(new PieChart.Data(recipient, count));
        });
        itemUsageBycategoryPieChart.setData(pieChartData);
        itemUsageBycategoryPieChart.setTitle("Issuance by Recipient");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


