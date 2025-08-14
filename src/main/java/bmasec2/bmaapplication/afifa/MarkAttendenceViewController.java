package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.time.ZoneId;

public class MarkAttendenceViewController {

    @FXML
    private Label marktraningtypelabel;
    @FXML
    private Label markstatuslabel;

    private Cadet loggedInCadet;
    private String currentSessionId;
    private Training currentTrainingSession;

    @FXML
    public void initialize() {

        markstatuslabel.setText("Ready to mark attendance.");
    }

    public void initData(Cadet cadet, String sessionId) {
        this.loggedInCadet = cadet;
        this.currentSessionId = sessionId;
        loadTrainingSessionDetails();
    }

    private void loadTrainingSessionDetails() {
        if (currentSessionId != null) {
            List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.bin");
            currentTrainingSession = allTrainings.stream()
                    .filter(t -> t.getSessionId().equals(currentSessionId))
                    .findFirst()
                    .orElse(null);

            if (currentTrainingSession != null) {
                marktraningtypelabel.setText("Training: " + currentTrainingSession.getTopic() + " (" + currentTrainingSession.getLocation() + ")");
            } else {
                marktraningtypelabel.setText("Training session not found.");
            }
        } else {
            marktraningtypelabel.setText("No training session selected.");
        }
    }

    @FXML
    public void markaspresentonaction(ActionEvent actionEvent) {
        markAttendance("Present");
    }

    @FXML
    public void markaslateonaction(ActionEvent actionEvent) {
        markAttendance("Late");
    }

    @FXML
    public void markasabsentonaction(ActionEvent actionEvent) {
        markAttendance("Absent");
    }

    private void markAttendance(String status) {
        if (loggedInCadet == null || currentTrainingSession == null) {
            showAlert(AlertType.ERROR, "Error", "Cadet or training session not loaded.");
            return;
        }


        LocalDate today = LocalDate.now();
        LocalDate sessionDate = currentTrainingSession.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!sessionDate.isEqual(today)) {
            showAlert(AlertType.WARNING, "Attendance Error", "Attendance can only be marked for today's sessions.");
            return;
        }

        List<Attendance> attendances = DataPersistenceManager.loadObjects("attendance.bin");


        boolean alreadyMarked = attendances.stream()
                .anyMatch(a -> a.getCadetId().equals(loggedInCadet.getUserId()) && a.getSessionId().equals(currentSessionId));

        if (alreadyMarked) {
            showAlert(AlertType.INFORMATION, "Already Marked", "Your attendance for this session has already been recorded.");
            return;
        }

        String attendanceId = UUID.randomUUID().toString();
        Attendance newAttendance = new Attendance(
                attendanceId,
                loggedInCadet.getUserId(),
                currentSessionId,
                new Date(),
                status
        );

        attendances.add(newAttendance);
        DataPersistenceManager.saveObjects(attendances, "attendance.bin");

        markstatuslabel.setText("Attendance marked as " + status + ".");
        showAlert(AlertType.INFORMATION, "Success", "Attendance recorded successfully as " + status + ".");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


