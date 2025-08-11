package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.ComboBoxTableCell;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class takeAttendanceviewController {
    @javafx.fxml.FXML
    private TableColumn<AttendanceRecord, String> statusTableColumn;
    @javafx.fxml.FXML
    private TableView<AttendanceRecord> takeAttendanceTableView;
    @javafx.fxml.FXML
    private TableColumn<AttendanceRecord, String> cadetIdTableColumn;
    @javafx.fxml.FXML
    private TableColumn<AttendanceRecord, String> cadetNameTableColumn;
    @javafx.fxml.FXML
    private ComboBox<String> selectSessionComboBox;

    private ObservableList<AttendanceRecord> attendanceData = FXCollections.observableArrayList();
    private List<Training> trainingSessions;

    @javafx.fxml.FXML
    public void initialize() {

        cadetIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        cadetNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        ObservableList<String> statusOptions = FXCollections.observableArrayList("Present", "Absent", "Late");
        statusTableColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptions));
        statusTableColumn.setOnEditCommit(event -> {
            AttendanceRecord record = event.getRowValue();
            record.setStatus(event.getNewValue());
        });

        takeAttendanceTableView.setEditable(true);
        takeAttendanceTableView.setItems(attendanceData);


        loadTrainingSessions();
    }

    private void loadTrainingSessions() {
        trainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        ObservableList<String> sessionOptions = FXCollections.observableArrayList();

        for (Training session : trainingSessions) {
            if ("Scheduled".equals(session.getStatus()) || "Ongoing".equals(session.getStatus())) {
                sessionOptions.add(session.getSessionId() + " - " + session.getTopic());
            }
        }

        selectSessionComboBox.setItems(sessionOptions);
    }

    @javafx.fxml.FXML
    public void saveAttendanceButtonOnAction(ActionEvent actionEvent) {
        String selectedSession = selectSessionComboBox.getValue();
        if (selectedSession == null || attendanceData.isEmpty()) {
            showAlert("Error", "Please select a session and load cadet list first.");
            return;
        }

        String sessionId = selectedSession.split(" - ")[0];
        List<Attendance> attendanceList = DataPersistenceManager.loadObjects("attendance.bin");

        for (AttendanceRecord record : attendanceData) {
            Attendance attendance = new Attendance(
                    UUID.randomUUID().toString(),
                    record.getCadetId(),
                    sessionId,
                    new Date(),
                    record.getStatus()
            );
            attendanceList.add(attendance);
        }

        DataPersistenceManager.saveObjects(attendanceList, "attendance.bin");
        showAlert("Success", "Attendance saved successfully!");


        attendanceData.clear();
        selectSessionComboBox.setValue(null);
    }

    @javafx.fxml.FXML
    public void loadCadetListButtonOnAction(ActionEvent actionEvent) {
        String selectedSession = selectSessionComboBox.getValue();
        if (selectedSession == null) {
            showAlert("Error", "Please select a training session first.");
            return;
        }

        String sessionId = selectedSession.split(" - ")[0];
        Training selectedTraining = null;

        for (Training session : trainingSessions) {
            if (session.getSessionId().equals(sessionId)) {
                selectedTraining = session;
                break;
            }
        }

        if (selectedTraining == null) {
            showAlert("Error", "Selected training session not found.");
            return;
        }


        attendanceData.clear();


        List<String> registeredCadets = selectedTraining.getRegisteredCadets();

        for (String cadetId : registeredCadets) {

            String cadetName = "Cadet " + cadetId;
            AttendanceRecord record = new AttendanceRecord(cadetId, cadetName, "Present");
            attendanceData.add(record);
        }

        if (attendanceData.isEmpty()) {
            showAlert("Information", "No cadets registered for this training session.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for table data
    public static class AttendanceRecord {
        private String cadetId;
        private String cadetName;
        private String status;

        public AttendanceRecord(String cadetId, String cadetName, String status) {
            this.cadetId = cadetId;
            this.cadetName = cadetName;
            this.status = status;
        }

        public String getCadetId() {
            return cadetId;
        }

        public void setCadetId(String cadetId) {
            this.cadetId = cadetId;
        }

        public String getCadetName() {
            return cadetName;
        }

        public void setCadetName(String cadetName) {
            this.cadetName = cadetName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
