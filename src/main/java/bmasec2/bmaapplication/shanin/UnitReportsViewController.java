package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.Report;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UnitReportsViewController {

    @FXML private DatePicker endDateDatePicker;
    @FXML private TextArea reportContentDisplayTextArea;
    @FXML private ComboBox<String> chooseUnitComboBox;
    @FXML private DatePicker startDateDatePicker;

    private static final String REPORTS_FILE = "reports.dat";

    @FXML
    public void initialize() {

        chooseUnitComboBox.getItems().addAll("All Units", "Alpha Unit", "Bravo Unit", "Charlie Unit");
        chooseUnitComboBox.setValue("All Units");


        endDateDatePicker.setValue(LocalDate.now());
        startDateDatePicker.setValue(LocalDate.now().minusMonths(1));
    }

    @FXML
    public void generateReportBtnOnAction(ActionEvent actionEvent) {
        String selectedUnit = chooseUnitComboBox.getValue();
        LocalDate startDate = startDateDatePicker.getValue();
        LocalDate endDate = endDateDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Dates", "Please select both start and end dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date Range", "Start date cannot be after end date.");
            return;
        }


        List<Report> allReports = DataPersistenceManager.loadObjects(REPORTS_FILE);


        StringBuilder reportContent = new StringBuilder();
        reportContent.append("--- Unit Report for ").append(selectedUnit).append(" --- ");
                reportContent.append("From: ").append(startDate).append(" To: ").append(endDate).append(" ");

                        List<Report> filteredReports = allReports.stream()
                                .filter(report -> {
                                    boolean unitMatch = selectedUnit.equals("All Units") || report.getUnit().equals(selectedUnit);

                                    // Convert Date to LocalDate for comparison
                                    LocalDate reportLocalDate = report.getReportDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    boolean dateMatch = !reportLocalDate.isBefore(startDate) && !reportLocalDate.isAfter(endDate);

                                    return unitMatch && dateMatch;
                                })
                                .collect(Collectors.toList());

        if (filteredReports.isEmpty()) {
            reportContent.append("No reports found for the selected criteria.");
        } else {
            for (Report report : filteredReports) {
                reportContent.append("Report ID: ").append(report.getReportId()).append("\n");
                reportContent.append("Date: ").append(report.getReportDate()).append("\n");
                reportContent.append("Unit: ").append(report.getUnit()).append("\n");
                reportContent.append("Title: ").append(report.getTitle()).append("\n");
                reportContent.append("Content: ").append(report.getContentString()).append("\n"); // Use getContentString()
                reportContent.append("-----------------------------------\n");
            }
        }

        reportContentDisplayTextArea.setText(reportContent.toString());

        // Example: Add some dummy reports if the file is empty for testing
        if (allReports.isEmpty()) {
            allReports.add(new Report("RPT-001", "Alpha Unit", new Date(), "Monthly Performance", "Alpha Unit performed well this month."));
            allReports.add(new Report("RPT-002", "Bravo Unit", Date.from(LocalDate.now().minusWeeks(2).atStartOfDay(ZoneId.systemDefault()).toInstant()), "Weekly Attendance", "Bravo Unit had 95% attendance."));
            allReports.add(new Report("RPT-003", "Alpha Unit", Date.from(LocalDate.now().minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), "Incident Report", "Minor incident reported in Alpha Unit."));
            DataPersistenceManager.saveObjects(allReports, REPORTS_FILE);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}