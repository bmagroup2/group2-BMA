package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class markAbsenceReasonViewController {
    @FXML
    private Label absenceDetailsLabel;
    @FXML
    private TextArea reasonForAbsenceTextArea;
    @FXML
    private TableView<Attendance> recentUnexplainedAbsencesTableView;
    @FXML
    private TableColumn<Attendance, String> sessionTableColumn;
    @FXML
    private TableColumn<Attendance, Date> dateTableColumn;
    @FXML
    private TableColumn<Attendance, String> cadetNameTableColumn;
    @FXML
    private TableColumn<Attendance, String> cadetIdTableColumn;

    private ObservableList<Attendance> unexplainedAbsences = FXCollections.observableArrayList();
    private List<Attendance> allAttendanceRecords;

    @FXML
    public void initialize() {
        // Initialize table columns
        cadetIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        cadetNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId")); // Placeholder, would need to fetch cadet name
        sessionTableColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        recentUnexplainedAbsencesTableView.setItems(unexplainedAbsences);

        // Add table selection listener
        recentUnexplainedAbsencesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayAbsenceDetails(newValue);
                    }
                }
        );

        loadUnexplainedAbsences();
    }

    private void loadUnexplainedAbsences() {
        allAttendanceRecords = DataPersistenceManager.loadObjects("attendance.bin");
        unexplainedAbsences.clear();


        unexplainedAbsences.addAll(allAttendanceRecords.stream()
                .filter(a -> "Absent".equalsIgnoreCase(a.getStatus()) && (a.getReason() == null || a.getReason().isEmpty()))
                .collect(Collectors.toList()));

        if (unexplainedAbsences.isEmpty()) {
            showAlert("Information", "No unexplained absences found.");
        }
    }

    private void displayAbsenceDetails(Attendance attendance) {
        absenceDetailsLabel.setText(
                "Cadet ID: " + attendance.getCadetId() + "\n" +
                        "Session ID: " + attendance.getSessionId() + "\n" +
                        "Date: " + attendance.getDate() + "\n" +
                        "Current Status: " + attendance.getStatus()
        );
        reasonForAbsenceTextArea.setText(attendance.getReason());
    }

    @FXML
    public void saveReasonButtonOnAction(ActionEvent actionEvent) {
        Attendance selectedAbsence = recentUnexplainedAbsencesTableView.getSelectionModel().getSelectedItem();
        if (selectedAbsence == null) {
            showAlert("Error", "Please select an absence record first.");
            return;
        }

        String newReason = reasonForAbsenceTextArea.getText().trim();
        if (newReason.isEmpty()) {
            showAlert("Validation Error", "Please enter a reason for absence.");
            return;
        }


        selectedAbsence.setReason(newReason);


        DataPersistenceManager.saveObjects(allAttendanceRecords, "attendance.bin");

        showAlert("Success", "Absence reason saved successfully!");


        loadUnexplainedAbsences();
        reasonForAbsenceTextArea.clear();
        absenceDetailsLabel.setText("");
    }

    @FXML
    public void clearReasonButtonOnAction(ActionEvent actionEvent) {
        reasonForAbsenceTextArea.clear();
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
