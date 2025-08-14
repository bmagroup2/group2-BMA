package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class monthlyFoodUsageReportViewController {

    @FXML
    private BarChart<String, Number> foodUsageBarChart;
    @FXML
    private DatePicker selectMonthDatePicker;

    @FXML
    public void initialize() {
        selectMonthDatePicker.setValue(LocalDate.now());

        generateReport();
    }

    @FXML
    public void generateReportOnActionButton(ActionEvent actionEvent) {
        generateReport();
    }

    private void generateReport() {
        try {
            if (selectMonthDatePicker.getValue() == null) {
                showAlert("Error", "Please select a month.");
                return;
            }

            LocalDate selectedMonth = selectMonthDatePicker.getValue();
            
            

            List<InventoryItem> allItems = DataPersistenceManager.loadObjects("inventory_items.dat");

            
            
            Map<String, Double> usageByCategory = allItems.stream()
                    .collect(Collectors.groupingBy(InventoryItem::getCategory,
                            Collectors.summingDouble(item -> item.getInitialQuantity() - item.getQuantity())));

            foodUsageBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Monthly Food Usage");

            if (usageByCategory.isEmpty()) {
                showAlert("Info", "No food usage data available for the selected month.");
                return;
            }

            usageByCategory.forEach((category, usage) -> {
                series.getData().add(new XYChart.Data<>(category, usage));
            });

            foodUsageBarChart.getData().add(series);

        } catch (Exception e) {
            showAlert("Error", "Failed to generate report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}