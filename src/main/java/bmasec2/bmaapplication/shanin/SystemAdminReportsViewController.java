package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.AuditLog;
import bmasec2.bmaapplication.model.Report;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemAdminReportsViewController {

    @FXML private DatePicker endDateDatePicker;
    @FXML private ComboBox<String> chooseAReportComboBox;
    @FXML private DatePicker startDateDatePicker;
    @FXML private TableColumn<Report, String> reportIdColumn;
    @FXML private TableColumn<Report, String> reportTypeColumn;
    @FXML private TableColumn<Report, String> generatedByColumn;
    @FXML private TableColumn<Report, Map<String, Object>> reportContentColumn;

    private ObservableList<Report> masterReportList;
    private static final String REPORTS_FILE = "reports.dat";
    private static final String AUDIT_LOGS_FILE = "auditlogs.dat";
    private static final String USERS_FILE = "users.dat";
    @FXML
    private TableView reportTableView;

    @FXML
    public void initialize() {

        chooseAReportComboBox.getItems().addAll("User Reports", "Audit Logs");
        chooseAReportComboBox.setValue("User Reports");

        reportIdColumn.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        generatedByColumn.setCellValueFactory(new PropertyValueFactory<>("generatedBy"));
        reportContentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));


        masterReportList = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(REPORTS_FILE));
        reportTableView.setItems(masterReportList);
    }

    @FXML
    public void generateAndViewReportBtnOnAction(ActionEvent actionEvent) {
        String selectedReportType = chooseAReportComboBox.getValue();
        LocalDate startDate = startDateDatePicker.getValue();
        LocalDate endDate = endDateDatePicker.getValue();

        if (selectedReportType == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please choose a report type.");
            return;
        }

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Date Range Missing", "Please select both start and end dates.");
            return;
        }

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());

        List<Report> generatedReports = new ArrayList<>();

        switch (selectedReportType) {
            case "User Reports":
                generatedReports = generateUserReports(start, end);
                break;
            case "Audit Logs":
                generatedReports = generateAuditLogReports(start, end);
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Invalid Report Type", "Selected report type is not supported.");
                return;
        }

        if (generatedReports.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No reports found for the selected criteria.");
        }


        masterReportList.addAll(generatedReports);
        DataPersistenceManager.saveObjects(masterReportList.stream().collect(Collectors.toList()), REPORTS_FILE);
        reportTableView.setItems(FXCollections.observableArrayList(generatedReports)); 
    }

    private List<Report> generateUserReports(Date startDate, Date endDate) {
        List<User> users = DataPersistenceManager.loadObjects(USERS_FILE);
        List<Report> reports = new ArrayList<>();


        long totalUsers = users.size();
        long systemAdmins = users.stream().filter(u -> u.getRole().equals("System Admin")).count();
        long cadets = users.stream().filter(u -> u.getRole().equals("Cadet")).count();
        long cadetsupervisers = users.stream().filter(u -> u.getRole().equals("Cadet Supervisor")).count();
        long trainingofficers = users.stream().filter(u -> u.getRole().equals("Training Instructor ")).count();
        long logisticofficers = users.stream().filter(u -> u.getRole().equals("Logistic Officer")).count();
        long medicalofficers = users.stream().filter(u -> u.getRole().equals("Medical Officer")).count();
        long messofficers = users.stream().filter(u -> u.getRole().equals("Mess Officer")).count();

        Map<String, Object> content = Map.of(
                "Total Users", totalUsers,
                "System Admins", systemAdmins,
                "Cadets", cadets,
                "Cadet Supervisors", cadetsupervisers,
                "Training Officers", trainingofficers,
                "Logistic Officers", logisticofficers,
                "Medical Officers", medicalofficers,
                "Mess Officers", messofficers,
                "Report Period Start", startDate,
                "Report Period End", endDate
        );

        reports.add(new Report("USR-" + System.currentTimeMillis(), "User Count Summary", "SystemAdmin", content));
        return reports;
    }

    private List<Report> generateAuditLogReports(Date startDate, Date endDate) {
        List<AuditLog> auditLogs = DataPersistenceManager.loadObjects(AUDIT_LOGS_FILE);
        List<Report> reports = new ArrayList<>();


        List<AuditLog> filteredLogs = auditLogs.stream()
                .filter(log -> !log.getTimestamp().before(startDate) && !log.getTimestamp().after(endDate))
                .collect(Collectors.toList());

        Map<String, Object> content = Map.of(
                "Total Logs in Period", filteredLogs.size(),
                "Report Period Start", startDate,
                "Report Period End", endDate
        );

        reports.add(new Report("AUD-" + System.currentTimeMillis(), "Audit Log Summary", "SystemAdmin", content));
        return reports;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}