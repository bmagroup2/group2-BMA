package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.model.TrainingSession;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class submitEvaluationViewController {

    @FXML
    private ComboBox<TrainingSession> selectTrainingSessionComboBox;
    @FXML
    private ComboBox<Cadet> selectCadetComboBox;
    @FXML
    private TextField scoreTextField;
    @FXML
    private TextArea evaluationRemarksTextArea;

    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser";
    private static final String CADETS_FILE = "cadets.ser";
    private static final String EVALUATIONS_FILE = "evaluations.ser";

    private ObservableList<TrainingSession> trainingSessions;
    private ObservableList<Cadet> cadets;

    @FXML
    public void initialize() {
        loadData();
        populateComboBoxes();
    }

    private void loadData() {
        trainingSessions = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(TRAINING_SESSIONS_FILE));
        cadets = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(CADETS_FILE));
    }

    private void populateComboBoxes() {
        selectTrainingSessionComboBox.setItems(trainingSessions);
        selectTrainingSessionComboBox.setConverter(new javafx.util.StringConverter<TrainingSession>() {
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


        selectTrainingSessionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

                ObservableList<Cadet> filteredCadets = cadets.stream()
                        .filter(cadet -> newVal.getCadetBatch().equals("All Cadets") || cadet.getBatch().equals(newVal.getCadetBatch()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                selectCadetComboBox.setItems(filteredCadets);
            } else {
                selectCadetComboBox.setItems(cadets);
            }
        });

        selectCadetComboBox.setConverter(new javafx.util.StringConverter<Cadet>() {
            @Override
            public String toString(Cadet cadet) {
                return cadet != null ? cadet.getName() + " (" + cadet.getCadetId() + ")" : "";
            }

            @Override
            public Cadet fromString(String string) {
                return cadets.stream()
                        .filter(cadet -> (cadet.getName() + " (" + cadet.getCadetId() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void submitEvaluationButtonOnAction(ActionEvent event) {
        TrainingSession selectedSession = selectTrainingSessionComboBox.getSelectionModel().getSelectedItem();
        Cadet selectedCadet = selectCadetComboBox.getSelectionModel().getSelectedItem();
        String scoreStr = scoreTextField.getText();
        String remarks = evaluationRemarksTextArea.getText();

        if (selectedSession == null || selectedCadet == null || scoreStr.isEmpty() || remarks.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreStr);
            if (score < 0 || score > 100) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Score must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Score must be a valid number.");
            return;
        }


        String evaluatorId = "instructor123";
        String evaluatorName = "Training Instructor";

        String evaluationId = UUID.randomUUID().toString();

        Evaluation newEvaluation = new Evaluation(
                evaluationId, selectedCadet.getCadetId(), selectedCadet.getName(), "N/A", evaluatorId,  scoreStr,  remarks);

        List<Evaluation> evaluations = DataPersistenceManager.loadObjects(EVALUATIONS_FILE);
        evaluations.add(newEvaluation);
        DataPersistenceManager.saveObjects(evaluations, EVALUATIONS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Evaluation submitted successfully!");


        selectTrainingSessionComboBox.getSelectionModel().clearSelection();
        selectCadetComboBox.getSelectionModel().clearSelection();
        scoreTextField.clear();
        evaluationRemarksTextArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


