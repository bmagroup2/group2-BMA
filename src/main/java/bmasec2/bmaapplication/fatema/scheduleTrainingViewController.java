package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class scheduleTrainingViewController {

    @FXML
    private ListView<bmasec2.bmaapplication.fatema.TrainingSession> upcomingschedulesessionListView;
    @FXML
    private ComboBox<String> cadetgroupComboBox;
    @FXML
    private TextField topicTextField;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField timeTextField;

    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser";
    private static final String CADETS_FILE = "cadets.ser"; // Assuming a file for cadets to get batches

    private ObservableList<bmasec2.bmaapplication.fatema.TrainingSession> trainingSessions;

    @FXML
    public void initialize() {
        loadTrainingSessions();
        populateCadetGroups();
        populateListView();
    }

    private void loadTrainingSessions() {
        List<bmasec2.bmaapplication.fatema.TrainingSession> loadedSessions = DataPersistenceManager.loadObjects(TRAINING_SESSIONS_FILE);
        trainingSessions = FXCollections.observableArrayList(loadedSessions);
    }

    private void populateCadetGroups() {
        // In a real application, load actual cadet batches from cadets.ser or a similar source
        // For now, using dummy data
        ObservableList<String> batches = FXCollections.observableArrayList("Batch A", "Batch B", "Batch C", "All Cadets");
        cadetgroupComboBox.setItems(batches);
    }

    private void populateListView() {
        // Filter for upcoming sessions and sort them
        ObservableList<bmasec2.bmaapplication.fatema.TrainingSession> upcoming = trainingSessions.stream()
                .filter(session -> session.getDate().isAfter(LocalDate.now().minusDays(1)))
                .sorted((s1, s2) -> {
                    int dateCompare = s1.getDate().compareTo(s2.getDate());
                    if (dateCompare == 0) {
                        return s1.getTime().compareTo(s2.getTime());
                    }
                    return dateCompare;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        upcomingschedulesessionListView.setItems(upcoming);

//        upcomingschedulesessionListView.setCellFactory(param -> new javafx.scene.control.ListCell<TrainingSession>() {
//            @Override
//            protected void updateItem(TrainingSession item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(String.format("%s - %s at %s (%s)",
//                            item.getDate().toString(),
//                            item.getTopic(),
//                            item.getTime().toString(),
//                            item.getCadetBatch()));
//                }
//            }
//        });
    }

    @FXML
    void saveScheduleButtonOnAction(ActionEvent event) {
        String cadetGroup = cadetgroupComboBox.getSelectionModel().getSelectedItem();
        String topic = topicTextField.getText();
        LocalDate date = dateDatePicker.getValue();
        String timeStr = timeTextField.getText();

        if (cadetGroup == null || topic.isEmpty() || date == null || timeStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Time Format", "Please enter time in HH:MM format (e.g., 14:30).");
            return;
        }

        // Assuming a dummy instructor ID and location for now
        String instructorId = "instructor123";
        String location = "BMA Training Ground";
        int maxParticipants = 50; // Default value

        String sessionId = UUID.randomUUID().toString();
        bmasec2.bmaapplication.fatema.TrainingSession newSession = new bmasec2.bmaapplication.fatema.TrainingSession(
                sessionId, topic, date, time, location, instructorId, cadetGroup, maxParticipants);

        trainingSessions.add(newSession);
        DataPersistenceManager.saveObjects(trainingSessions.stream().collect(Collectors.toList()), TRAINING_SESSIONS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Training session scheduled successfully!");

        // Clear form and refresh list
        cadetgroupComboBox.getSelectionModel().clearSelection();
        topicTextField.clear();
        dateDatePicker.setValue(null);
        timeTextField.clear();
        populateListView();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


