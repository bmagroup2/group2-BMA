package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class monthlyMedicalReportViewController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private BarChart<String, Number> medicalReportSummaryBarChart;
    @FXML
    private DatePicker monthDatePicker;

    @FXML
    public void initialize() {
        populateReportTypes();
        monthDatePicker.setValue(LocalDate.now());

        generateReport();
    }

    private void populateReportTypes() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Cases by Diagnosis",
                "Cases by Treatment",
                "Cases by Month"
        );
        reportTypeComboBox.setItems(reportTypes);
        reportTypeComboBox.setValue("Cases by Diagnosis");
    }

    @FXML
    public void generateReportOnActionButton(ActionEvent actionEvent) {
        generateReport();
    }

    private void generateReport() {
        try {
            if (monthDatePicker.getValue() == null) {
                showAlert("Error", "Please select a month.");
                return;
            }
            if (reportTypeComboBox.getValue() == null || reportTypeComboBox.getValue().isEmpty()) {
                showAlert("Error", "Please select a report type.");
                return;
            }

            LocalDate selectedMonth = monthDatePicker.getValue();
            Date startDate = Date.from(selectedMonth.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(selectedMonth.withDayOfMonth(selectedMonth.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());

            List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");

            List<MedicalRecord> filteredRecords = medicalRecords.stream()
                    .filter(record -> !record.getDate().before(startDate) && !record.getDate().after(endDate))
                    .collect(Collectors.toList());

            medicalReportSummaryBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            String reportType = reportTypeComboBox.getValue();

            switch (reportType) {
                case "Cases by Diagnosis":
                    series.setName("Cases by Diagnosis");
                    Map<String, Long> diagnosisCounts = filteredRecords.stream()
                            .collect(Collectors.groupingBy(MedicalRecord::getDiagnosis, Collectors.counting()));
                    diagnosisCounts.forEach((diagnosis, count) -> series.getData().add(new XYChart.Data<>(diagnosis, count)));
                    break;
                case "Cases by Treatment":
                    series.setName("Cases by Treatment");
                    Map<String, Long> treatmentCounts = filteredRecords.stream()
                            .collect(Collectors.groupingBy(MedicalRecord::getTreatment, Collectors.counting()));
                    treatmentCounts.forEach((treatment, count) -> series.getData().add(new XYChart.Data<>(treatment, count)));
                    break;
                case "Cases by Month":
                    series.setName("Cases by Month");
                    Map<Integer, Long> monthlyCounts = filteredRecords.stream()
                            .collect(Collectors.groupingBy(record -> record.getDate().getMonth() + 1, Collectors.counting()));
                    monthlyCounts.forEach((month, count) -> series.getData().add(new XYChart.Data<>(String.valueOf(month), count)));
                    break;
            }

            if (series.getData().isEmpty()) {
                showAlert("Info", "No data available for the selected month and report type.");
                return;
            }

            medicalReportSummaryBarChart.getData().add(series);

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