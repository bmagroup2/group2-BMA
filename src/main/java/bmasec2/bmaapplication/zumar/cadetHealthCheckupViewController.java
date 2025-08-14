package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class cadetHealthCheckupViewController {

    @FXML
    private TextField healthCheckupTimeTextField;
    @FXML
    private DatePicker healthCheckupDateDatePicker;
    @FXML
    private TextField healthCheckupRoomTextField;
    @FXML
    private ListView<String> upcomingScheduledCheckupsListView;
    @FXML
    private ComboBox<String> healthCheckupCadetBatchComboBox;

    @FXML
    public void initialize() {
        loadCadetBatches();
        loadScheduledCheckups();
        healthCheckupDateDatePicker.setValue(LocalDate.now());
    }

    private void loadCadetBatches() {
        List<bmasec2.bmaapplication.User> users = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> batches = FXCollections.observableArrayList();
        users.stream()
                .filter(user -> user instanceof Cadet)
                .map(user -> ((Cadet) user).getBatch())
                .distinct()
                .sorted()
                .forEach(batches::add);
        healthCheckupCadetBatchComboBox.setItems(batches);
        if (!batches.isEmpty()) {
            healthCheckupCadetBatchComboBox.setValue(batches.get(0));
        }
    }

    private void loadScheduledCheckups() {
        List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
        ObservableList<String> scheduledCheckups = FXCollections.observableArrayList();

        medicalRecords.stream()
                .filter(record -> record.getDiagnosis() != null && record.getDiagnosis().startsWith("Scheduled Checkup"))
                .forEach(record -> scheduledCheckups.add(
                        "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(record.getDate()) +
                        ", Cadet: " + record.getCadetId() +
                        ", Details: " + record.getTreatment()
                ));

        if (scheduledCheckups.isEmpty()) {
            scheduledCheckups.add("No upcoming scheduled checkups.");
        }
        upcomingScheduledCheckupsListView.setItems(scheduledCheckups);
    }

    @FXML
    public void healthCheckupSaveScheduleOnActionButton(ActionEvent actionEvent) {
        try {
            if (healthCheckupCadetBatchComboBox.getValue() == null || healthCheckupCadetBatchComboBox.getValue().isEmpty()) {
                showAlert("Error", "Please select a cadet batch.");
                return;
            }
            if (healthCheckupDateDatePicker.getValue() == null) {
                showAlert("Error", "Please select a date.");
                return;
            }
            if (healthCheckupTimeTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter a time.");
                return;
            }
            if (healthCheckupRoomTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter a room.");
                return;
            }

            String batch = healthCheckupCadetBatchComboBox.getValue();
            LocalDate localDate = healthCheckupDateDatePicker.getValue();
            Date checkupDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String time = healthCheckupTimeTextField.getText().trim();
            String room = healthCheckupRoomTextField.getText().trim();


            String recordId = UUID.randomUUID().toString();
            String diagnosis = "Scheduled Checkup for Batch " + batch;
            String treatment = "Time: " + time + ", Room: " + room;
            String notes = "Health checkup scheduled for cadets in batch " + batch;

            MedicalRecord newSchedule = new MedicalRecord(
                    recordId,
                    "Batch-" + batch,
                    checkupDate,
                    diagnosis,
                    treatment,
                    notes
            );

            DataPersistenceManager.addMedicalRecordAndSave(newSchedule);
            showAlert("Success", "Health checkup scheduled successfully for batch " + batch + "!");
            clearForm();
            loadScheduledCheckups();

        } catch (Exception e) {
            showAlert("Error", "Failed to schedule health checkup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        healthCheckupDateDatePicker.setValue(LocalDate.now());
        healthCheckupTimeTextField.clear();
        healthCheckupRoomTextField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}