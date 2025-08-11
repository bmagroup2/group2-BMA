package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class trainingHistoryViewController {
    @FXML
    private ComboBox<String> cadetComboBox;
    @FXML
    private TableView<TrainingHistoryEntry> trainingHistoryTableView;
    @FXML
    private TableColumn<TrainingHistoryEntry, String> sessionIdColumn;
    @FXML
    private TableColumn<TrainingHistoryEntry, String> topicColumn;
    @FXML
    private TableColumn<TrainingHistoryEntry, Date> dateColumn;
    @FXML
    private TableColumn<TrainingHistoryEntry, String> locationColumn;
    @FXML
    private TableColumn<TrainingHistoryEntry, Double> scoreColumn;
    @FXML
    private TableColumn<TrainingHistoryEntry, String> evaluationStatusColumn;
    @FXML
    private TextArea detailsTextArea;

    private ObservableList<TrainingHistoryEntry> trainingHistoryData = FXCollections.observableArrayList();
    private List<Training> allTrainingSessions;
    private List<Evaluation> allEvaluations;

    @FXML
    public void initialize() {

        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        evaluationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("evaluationStatus"));

        trainingHistoryTableView.setItems(trainingHistoryData);


        trainingHistoryTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayDetails(newValue);
                    }
                }
        );


        cadetComboBox.setItems(FXCollections.observableArrayList(
                "CDT-001 - John Smith", "CDT-002 - Jane Doe", "CDT-003 - Mike Johnson"
        ));


        allTrainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        allEvaluations = DataPersistenceManager.loadObjects("evaluations.bin");
    }

    @FXML
    public void loadHistoryButtonOnAction(ActionEvent actionEvent) {
        String selectedCadet = cadetComboBox.getValue();
        if (selectedCadet == null) {
            showAlert("Error", "Please select a cadet first.");
            return;
        }

        String cadetId = selectedCadet.split(" - ")[0];
        trainingHistoryData.clear();
        detailsTextArea.clear();


        List<Training> cadetTrainings = allTrainingSessions.stream()
                .filter(training -> training.getRegisteredCadets().contains(cadetId))
                .collect(Collectors.toList());

        if (cadetTrainings.isEmpty()) {
            showAlert("Information", "No training history found for this cadet.");
            return;
        }

        for (Training training : cadetTrainings) {

            Evaluation evaluation = allEvaluations.stream()
                    .filter(eval -> eval.getCadetId().equals(cadetId) && eval.getSessionId().equals(training.getSessionId()))
                    .findFirst()
                    .orElse(null);

            double score = (evaluation != null) ? evaluation.getScore() : -1.0;
            String evalStatus = (evaluation != null) ? evaluation.getStatus() : "N/A";

            trainingHistoryData.add(new TrainingHistoryEntry(
                    training.getSessionId(),
                    training.getTopic(),
                    training.getDateTime(),
                    training.getLocation(),
                    score,
                    evalStatus
            ));
        }

        if (trainingHistoryData.isEmpty()) {
            showAlert("Information", "No training history found for this cadet.");
        }
    }

    private void displayDetails(TrainingHistoryEntry entry) {
        StringBuilder details = new StringBuilder();
        details.append("Session ID: ").append(entry.getSessionId()).append("\n");
        details.append("Topic: ").append(entry.getTopic()).append("\n");
        details.append("Date: ").append(entry.getDate()).append("\n");
        details.append("Location: ").append(entry.getLocation()).append("\n");
        details.append("Score: ").append(entry.getScore() == -1.0 ? "N/A" : String.format("%.2f", entry.getScore())).append("\n");
        details.append("Evaluation Status: ").append(entry.getEvaluationStatus()).append("\n");


        detailsTextArea.setText(details.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static class TrainingHistoryEntry {
        private String sessionId;
        private String topic;
        private Date date;
        private String location;
        private double score;
        private String evaluationStatus;

        public TrainingHistoryEntry(String sessionId, String topic, Date date, String location, double score, String evaluationStatus) {
            this.sessionId = sessionId;
            this.topic = topic;
            this.date = date;
            this.location = location;
            this.score = score;
            this.evaluationStatus = evaluationStatus;
        }


        public String getSessionId() { return sessionId; }
        public String getTopic() { return topic; }
        public Date getDate() { return date; }
        public String getLocation() { return location; }
        public double getScore() { return score; }
        public String getEvaluationStatus() { return evaluationStatus; }
    }
}
