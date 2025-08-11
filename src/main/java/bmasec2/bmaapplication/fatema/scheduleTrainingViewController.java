package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class scheduleTrainingViewController {
    @javafx.fxml.FXML
    private TextField topicTextField;
    @javafx.fxml.FXML
    private DatePicker trainingDatePicker;
    @javafx.fxml.FXML
    private TextField locationTextField;
    @javafx.fxml.FXML
    private TextField maxParticipantsTextField;
    @javafx.fxml.FXML
    private ComboBox<String> timeComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> cadetGroupComboBox;
    @javafx.fxml.FXML
    private TextArea descriptionTextArea;

    @javafx.fxml.FXML
    public void initialize() {

        for (int hour = 8; hour <= 18; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                timeComboBox.getItems().add(time);
            }
        }


        cadetGroupComboBox.getItems().addAll(
                "Batch 2024-A", "Batch 2024-B", "Batch 2023-A", "Batch 2023-B",
                "Batch 2022-A", "Batch 2022-B", "All Batches"
        );


        trainingDatePicker.setValue(LocalDate.now().plusDays(1));
        maxParticipantsTextField.setText("30");
    }

    @javafx.fxml.FXML
    public void scheduleTrainingButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {

            String sessionId = "TRN-" + UUID.randomUUID().toString().substring(0, 8);
            String topic = topicTextField.getText().trim();
            String location = locationTextField.getText().trim();
            int maxParticipants = Integer.parseInt(maxParticipantsTextField.getText().trim());


            LocalDate date = trainingDatePicker.getValue();
            LocalTime time = LocalTime.parse(timeComboBox.getValue());
            Date dateTime = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());


            String instructorId = "INST-001";

            Training training = new Training(sessionId, topic, dateTime, location, instructorId, maxParticipants);


            List<Training> trainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
            trainingSessions.add(training);
            DataPersistenceManager.saveObjects(trainingSessions, "training_sessions.bin");

            showAlert("Success", "Training session scheduled successfully!\nSession ID: " + sessionId);
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to schedule training session: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @javafx.fxml.FXML
    public void checkConflictsButtonOnAction(ActionEvent actionEvent) {
        if (trainingDatePicker.getValue() == null || timeComboBox.getValue() == null) {
            showAlert("Error", "Please select date and time first.");
            return;
        }

        LocalDate date = trainingDatePicker.getValue();
        LocalTime time = LocalTime.parse(timeComboBox.getValue());
        Date dateTime = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());

        List<Training> trainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        boolean hasConflict = false;
        StringBuilder conflicts = new StringBuilder();

        for (Training session : trainingSessions) {
            if (session.getDateTime().equals(dateTime) &&
                    ("Scheduled".equals(session.getStatus()) || "Ongoing".equals(session.getStatus()))) {
                hasConflict = true;
                conflicts.append("- ").append(session.getTopic())
                        .append(" at ").append(session.getLocation()).append("\n");
            }
        }

        if (hasConflict) {
            showAlert("Conflict Detected", "The following training sessions are scheduled at the same time:\n\n" + conflicts.toString());
        } else {
            showAlert("No Conflicts", "No scheduling conflicts found for the selected time.");
        }
    }

    private boolean validateInputs() {
        if (topicTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a training topic.");
            return false;
        }

        if (trainingDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select a training date.");
            return false;
        }

        if (trainingDatePicker.getValue().isBefore(LocalDate.now())) {
            showAlert("Validation Error", "Training date cannot be in the past.");
            return false;
        }

        if (timeComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a training time.");
            return false;
        }

        if (locationTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a training location.");
            return false;
        }

        if (cadetGroupComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a cadet group.");
            return false;
        }

        try {
            int maxParticipants = Integer.parseInt(maxParticipantsTextField.getText().trim());
            if (maxParticipants <= 0 || maxParticipants > 100) {
                showAlert("Validation Error", "Maximum participants must be between 1 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid number for maximum participants.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        topicTextField.clear();
        trainingDatePicker.setValue(LocalDate.now().plusDays(1));
        timeComboBox.setValue(null);
        locationTextField.clear();
        cadetGroupComboBox.setValue(null);
        maxParticipantsTextField.setText("30");
        descriptionTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
