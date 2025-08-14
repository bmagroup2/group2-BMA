package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class markAbsenceReasonViewController {

    @FXML
    private TableView<Attendance> rercentUnexplainedAbsencesTableView;
    @FXML
    private TableColumn<Attendance, LocalDate> dateTableColumn;
    @FXML
    private TableColumn<Attendance, String> cadetNameTableColumn;
    @FXML
    private TableColumn<Attendance, String> sessionTableColumn;
    @FXML
    private Label absenceDetailsLabel;
    @FXML
    private TextArea reasonForAbsenceTextArea;

    private static final String ATTENDANCE_FILE = "attendance.dat";
    private ObservableList<Attendance> unexplainedAbsences;

    @FXML
    public void initialize() {

        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        cadetNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
        sessionTableColumn.setCellValueFactory(new PropertyValueFactory<>("sessionName"));

        loadUnexplainedAbsences();
        rercentUnexplainedAbsencesTableView.setItems(unexplainedAbsences);


        rercentUnexplainedAbsencesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        absenceDetailsLabel.setText(String.format("Absence: %s on %s for %s",
                                newValue.getCadetName(),
                                newValue.getDate().toString(),
                                newValue.getSessionName()));
                        reasonForAbsenceTextArea.setText(newValue.getReason() != null ? newValue.getReason() : "");
                    } else {
                        absenceDetailsLabel.setText("Absence: [Cadet Name] on [Date]");
                        reasonForAbsenceTextArea.clear();
                    }
                });
    }

    private void loadUnexplainedAbsences() {
        List<Attendance> allAttendance = DataPersistenceManager.loadObjects(ATTENDANCE_FILE);
        unexplainedAbsences = allAttendance.stream()
                .filter(a -> "Absent".equalsIgnoreCase(a.getStatus()) && (a.getReason() == null || a.getReason().isEmpty()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @FXML
    void saveReasonButtonOnAction(ActionEvent event) {
        Attendance selectedAbsence = rercentUnexplainedAbsencesTableView.getSelectionModel().getSelectedItem();
        String reason = reasonForAbsenceTextArea.getText();

        if (selectedAbsence == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an absence from the list.");
            return;
        }

        if (reason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please provide a reason for the absence.");
            return;
        }


        selectedAbsence.setReason(reason);
        selectedAbsence.setStatus("Excused");


        List<Attendance> allAttendance = DataPersistenceManager.loadObjects(ATTENDANCE_FILE);

        for (int i = 0; i < allAttendance.size(); i++) {
            if (allAttendance.get(i).getAttendanceId().equals(selectedAbsence.getAttendanceId())) {
                allAttendance.set(i, selectedAbsence);
                break;
            }
        }
        DataPersistenceManager.saveObjects(allAttendance, ATTENDANCE_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Absence reason saved successfully!");

        loadUnexplainedAbsences();
        rercentUnexplainedAbsencesTableView.setItems(unexplainedAbsences);
        reasonForAbsenceTextArea.clear();
        absenceDetailsLabel.setText("Absence: [Cadet Name] on [Date]");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


