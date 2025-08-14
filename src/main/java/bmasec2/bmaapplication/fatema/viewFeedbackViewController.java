package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Feedback;
import bmasec2.bmaapplication.model.TrainingSession;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class viewFeedbackViewController {

    @FXML
    private ComboBox<TrainingSession> selectTrainingSrssionComboBox;
    @FXML
    private ListView<Feedback> filterBySessionListView;
    @FXML
    private Label cadetIDLabel;
    @FXML
    private TextArea feedBackDetailsTextArea;

    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser";
    private static final String FEEDBACK_FILE = "feedback.ser";

    private ObservableList<TrainingSession> trainingSessions;
    private ObservableList<Feedback> allFeedback;

    @FXML
    public void initialize() {
        loadData();
        populateSessionComboBox();

        // Listen for selection changes in the feedback list view
        filterBySessionListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cadetIDLabel.setText(String.format("From: %s (%s)", newValue.getCadetName(), newValue.getCadetId()));
                        feedBackDetailsTextArea.setText(String.join("Rating: %d/5\nComments: %s\nSubmitted: %s",
                                newValue.toString(),
                                newValue.getComments(),
                                newValue.getSubmissionDate().toString()));
                    } else {
                        cadetIDLabel.setText("From: [Cadet ID]");
                        feedBackDetailsTextArea.clear();
                    }
                });
    }

    private void loadData() {
        trainingSessions = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(TRAINING_SESSIONS_FILE));
        allFeedback = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(FEEDBACK_FILE));
    }

    private void populateSessionComboBox() {
        selectTrainingSrssionComboBox.setItems(trainingSessions);
        selectTrainingSrssionComboBox.setConverter(new javafx.util.StringConverter<TrainingSession>() {
            @Override
            public String toString(TrainingSession session) {
                return session != null ? session.getTopic() + " (" + session.getDate() + ")" : "";
            }

            @Override
            public TrainingSession fromString(String string) {
                return trainingSessions.stream()
                        .filter(session -> (session.getTopic() + " (" + session.getDate() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Add listener to filter feedback when a session is selected
        selectTrainingSrssionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterFeedbackBySession(newVal.getSessionId());
            } else {
                filterBySessionListView.setItems(FXCollections.observableArrayList()); // Clear list if no session selected
            }
        });
    }

    private void filterFeedbackBySession(String sessionId) {
        ObservableList<Feedback> filteredFeedback = allFeedback.stream()
                .filter(feedback -> feedback.getSessionId().equals(sessionId))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        filterBySessionListView.setItems(filteredFeedback);

        filterBySessionListView.setCellFactory(param -> new javafx.scene.control.ListCell<Feedback>() {
            @Override
            protected void updateItem(Feedback item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Cadet: %s - Rating: %d/5", item.getCadetName(), item.getRating()));
                }
            }
        });
    }
}


