package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.AuditLog;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AuditLogsViewController {

    @FXML private TextField filterByUserTextfield;
    @FXML private DatePicker endDateDatePicker;
    @FXML private TableView<AuditLog> auditLogsTableView;
    @FXML private TableColumn<AuditLog, String> actionPerformedColumn;
    @FXML private DatePicker startDateDatePicker;
    @FXML private TableColumn<AuditLog, String> userColumn;
    @FXML private TableColumn<AuditLog, Date> timestampColumn;

    private ObservableList<AuditLog> masterAuditLogList;
    private static final String AUDIT_LOGS_FILE = "auditlogs.dat";

    @FXML
    public void initialize() {

        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        actionPerformedColumn.setCellValueFactory(new PropertyValueFactory<>("action"));


        loadAuditLogs();

        filterByUserTextfield.textProperty().addListener((obs, oldVal, newVal) -> filterLogs());
    }

    private void loadAuditLogs() {
        List<AuditLog> loadedLogs = DataPersistenceManager.loadObjects(AUDIT_LOGS_FILE);
        masterAuditLogList = FXCollections.observableArrayList(loadedLogs);

        filterLogs();
    }

    private void filterLogs() {
        String userFilter = filterByUserTextfield.getText().toLowerCase();
        LocalDate startDate = startDateDatePicker.getValue();
        LocalDate endDate = endDateDatePicker.getValue();

        List<AuditLog> filteredList = masterAuditLogList.stream()
                .filter(log -> log.getUserId().toLowerCase().contains(userFilter) || log.getAction().toLowerCase().contains(userFilter))
                .filter(log -> {
                    if (startDate == null && endDate == null) return true;
                    Date logDate = log.getTimestamp();
                    boolean afterStart = (startDate == null) || !logDate.before(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    boolean beforeEnd = (endDate == null) || !logDate.after(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant()));
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());

        auditLogsTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void filterLogsBtnOnAction(ActionEvent actionEvent) {
        filterLogs();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

