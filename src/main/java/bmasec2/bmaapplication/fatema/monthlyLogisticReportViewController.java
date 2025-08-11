package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.InventoryItem;
import bmasec2.bmaapplication.model.Report;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class monthlyLogisticReportViewController {
    @javafx.fxml.FXML
    private ComboBox<String> monthComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> yearComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> reportTypeComboBox;
    @javafx.fxml.FXML
    private TextArea reportContentTextArea;
    @javafx.fxml.FXML
    private Label reportStatusLabel;
    @javafx.fxml.FXML
    private ProgressBar reportProgressBar;

    @javafx.fxml.FXML
    public void initialize() {
        // Initialize month options
        monthComboBox.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        monthComboBox.setValue(Month.of(LocalDate.now().getMonthValue()).name());

        // Initialize year options
        int currentYear = Year.now().getValue();
        ObservableList<String> yearOptions = FXCollections.observableArrayList();
        for (int year = currentYear - 2; year <= currentYear + 1; year++) {
            yearOptions.add(String.valueOf(year));
        }
        yearComboBox.setItems(yearOptions);
        yearComboBox.setValue(String.valueOf(currentYear));

        // Initialize report type options
        reportTypeComboBox.setItems(FXCollections.observableArrayList(
                "Inventory Summary", "Stock Movement", "Low Stock Items",
                "Issue History", "Restock Requests", "Complete Report"
        ));
        reportTypeComboBox.setValue("Complete Report");

        // Initialize progress bar
        reportProgressBar.setVisible(false);
    }

    @javafx.fxml.FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        String month = monthComboBox.getValue();
        String year = yearComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();

        reportProgressBar.setVisible(true);
        reportProgressBar.setProgress(0.0);
        reportStatusLabel.setText("Generating report...");

        try {
            String reportContent = generateReport(month, year, reportType);
            reportContentTextArea.setText(reportContent);

            reportProgressBar.setProgress(1.0);
            reportStatusLabel.setText("Report generated successfully");
            reportStatusLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            reportStatusLabel.setText("Error generating report: " + e.getMessage());
            reportStatusLabel.setStyle("-fx-text-fill: red;");
            showAlert("Error", "Failed to generate report: " + e.getMessage());
        } finally {

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        reportProgressBar.setVisible(false);
                    });
                }
            }, 2000);
        }
    }

    @javafx.fxml.FXML
    public void saveReportButtonOnAction(ActionEvent actionEvent) {
        if (reportContentTextArea.getText().trim().isEmpty()) {
            showAlert("Error", "No report content to save. Please generate a report first.");
            return;
        }

        try {
            String month = monthComboBox.getValue();
            String year = yearComboBox.getValue();
            String reportType = reportTypeComboBox.getValue();

            String reportId = "LOG-" + UUID.randomUUID().toString().substring(0, 8);
            String title = reportType + " - " + month + " " + year;

            Report report = new Report(reportId, "Logistics", "LOG-OFFICER-001",
                    reportContentTextArea.getText());

            List<Report> reports = DataPersistenceManager.loadObjects("reports.bin");
            reports.add(report);
            DataPersistenceManager.saveObjects(reports, "reports.bin");

            showAlert("Success", "Report saved successfully!\nReport ID: " + reportId);
            reportStatusLabel.setText("Report saved with ID: " + reportId);
            reportStatusLabel.setStyle("-fx-text-fill: blue;");

        } catch (Exception e) {
            showAlert("Error", "Failed to save report: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearReportButtonOnAction(ActionEvent actionEvent) {
        reportContentTextArea.clear();
        reportStatusLabel.setText("Ready to generate report");
        reportStatusLabel.setStyle("-fx-text-fill: black;");
        reportProgressBar.setVisible(false);
    }

    @javafx.fxml.FXML
    public void exportReportButtonOnAction(ActionEvent actionEvent) {
        if (reportContentTextArea.getText().trim().isEmpty()) {
            showAlert("Error", "No report content to export. Please generate a report first.");
            return;
        }


        showAlert("Export", "Report export functionality would be implemented here.\n\n" +
                "The report content is ready for export to PDF, Excel, or other formats.");
    }

    private String generateReport(String month, String year, String reportType) {
        StringBuilder report = new StringBuilder();


        report.append("BANGLADESH MILITARY ACADEMY\n");
        report.append("LOGISTICS DEPARTMENT\n");
        report.append("MONTHLY REPORT\n");
        report.append("=".repeat(50)).append("\n\n");
        report.append("Report Type: ").append(reportType).append("\n");
        report.append("Period: ").append(month).append(" ").append(year).append("\n");
        report.append("Generated on: ").append(new Date()).append("\n");
        report.append("Generated by: Logistics Officer\n\n");


        updateProgress(0.2);


        List<InventoryItem> inventory = DataPersistenceManager.loadObjects("inventory_items.bin");

        switch (reportType) {
            case "Inventory Summary":
                generateInventorySummary(report, inventory);
                break;
            case "Stock Movement":
                generateStockMovementReport(report, inventory);
                break;
            case "Low Stock Items":
                generateLowStockReport(report, inventory);
                break;
            case "Issue History":
                generateIssueHistoryReport(report);
                break;
            case "Restock Requests":
                generateRestockRequestsReport(report);
                break;
            case "Complete Report":
                generateCompleteReport(report, inventory);
                break;
        }

        updateProgress(1.0);
        return report.toString();
    }

    private void generateInventorySummary(StringBuilder report, List<InventoryItem> inventory) {
        report.append("INVENTORY SUMMARY\n");
        report.append("-".repeat(30)).append("\n\n");

        Map<String, List<InventoryItem>> categoryGroups = inventory.stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory));

        int totalItems = inventory.size();
        int totalQuantity = inventory.stream().mapToInt(InventoryItem::getQuantity).sum();
        long lowStockItems = inventory.stream().filter(InventoryItem::isLowStock).count();

        report.append("Total Item Types: ").append(totalItems).append("\n");
        report.append("Total Quantity: ").append(totalQuantity).append(" units\n");
        report.append("Low Stock Items: ").append(lowStockItems).append("\n");
        report.append("Categories: ").append(categoryGroups.size()).append("\n\n");

        report.append("CATEGORY BREAKDOWN:\n");
        for (Map.Entry<String, List<InventoryItem>> entry : categoryGroups.entrySet()) {
            String category = entry.getKey();
            List<InventoryItem> items = entry.getValue();
            int categoryTotal = items.stream().mapToInt(InventoryItem::getQuantity).sum();

            report.append("- ").append(category).append(": ")
                    .append(items.size()).append(" types, ")
                    .append(categoryTotal).append(" units\n");
        }

        updateProgress(0.6);
    }

    private void generateLowStockReport(StringBuilder report, List<InventoryItem> inventory) {
        report.append("LOW STOCK ITEMS REPORT\n");
        report.append("-".repeat(30)).append("\n\n");

        List<InventoryItem> lowStockItems = inventory.stream()
                .filter(InventoryItem::isLowStock)
                .sorted(Comparator.comparing(item -> item.getQuantity() - item.getMinStockLevel()))
                .collect(Collectors.toList());

        if (lowStockItems.isEmpty()) {
            report.append("No items are currently below minimum stock levels.\n");
        } else {
            report.append("Items requiring immediate attention:\n\n");

            for (InventoryItem item : lowStockItems) {
                int shortage = item.getMinStockLevel() - item.getQuantity();
                report.append("Item: ").append(item.getName()).append("\n");
                report.append("  Current Stock: ").append(item.getQuantity()).append(" ").append(item.getUnit()).append("\n");
                report.append("  Minimum Required: ").append(item.getMinStockLevel()).append(" ").append(item.getUnit()).append("\n");
                report.append("  Shortage: ").append(shortage).append(" ").append(item.getUnit()).append("\n");
                report.append("  Category: ").append(item.getCategory()).append("\n");
                report.append("  Last Updated: ").append(item.getLastUpdated()).append("\n\n");
            }
        }

        updateProgress(0.8);
    }

    private void generateStockMovementReport(StringBuilder report, List<InventoryItem> inventory) {
        report.append("STOCK MOVEMENT REPORT\n");
        report.append("-".repeat(30)).append("\n\n");

        report.append("This section would contain detailed stock movement data\n");
        report.append("including items received, issued, and transferred during the period.\n\n");


        report.append("Summary of movements would be displayed here based on\n");
        report.append("transaction logs and issue/receipt records.\n");
    }

    private void generateIssueHistoryReport(StringBuilder report) {
        report.append("ISSUE HISTORY REPORT\n");
        report.append("-".repeat(30)).append("\n\n");

        report.append("This section would contain detailed issue history\n");
        report.append("for the selected period, including:\n");
        report.append("- Items issued to cadets and staff\n");
        report.append("- Issue dates and quantities\n");
        report.append("- Purpose of each issue\n");
        report.append("- Return status where applicable\n");
    }

    private void generateRestockRequestsReport(StringBuilder report) {
        report.append("RESTOCK REQUESTS REPORT\n");
        report.append("-".repeat(30)).append("\n\n");

        report.append("This section would contain all restock requests\n");
        report.append("made during the selected period, including:\n");
        report.append("- Requested items and quantities\n");
        report.append("- Request dates and status\n");
        report.append("- Approval status\n");
        report.append("- Delivery status\n");
    }

    private void generateCompleteReport(StringBuilder report, List<InventoryItem> inventory) {
        generateInventorySummary(report, inventory);
        report.append("\n").append("=".repeat(50)).append("\n\n");

        generateLowStockReport(report, inventory);
        report.append("\n").append("=".repeat(50)).append("\n\n");

        generateStockMovementReport(report, inventory);
        report.append("\n").append("=".repeat(50)).append("\n\n");

        generateIssueHistoryReport(report);
        report.append("\n").append("=".repeat(50)).append("\n\n");

        generateRestockRequestsReport(report);

        report.append("\n\nRECOMMENDations:\n");
        report.append("-".repeat(20)).append("\n");
        report.append("1. Review and restock low inventory items\n");
        report.append("2. Update minimum stock levels as needed\n");
        report.append("3. Implement preventive maintenance schedules\n");
        report.append("4. Consider bulk purchasing for frequently used items\n");
    }

    private void updateProgress(double progress) {
        javafx.application.Platform.runLater(() -> {
            reportProgressBar.setProgress(progress);
        });
    }

    private boolean validateInputs() {
        if (monthComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a month.");
            return false;
        }

        if (yearComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a year.");
            return false;
        }

        if (reportTypeComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a report type.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
