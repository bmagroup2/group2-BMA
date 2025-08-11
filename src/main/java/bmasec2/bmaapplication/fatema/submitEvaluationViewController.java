package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class submitEvaluationViewController {
    @javafx.fxml.FXML
    private ComboBox<String> cadetComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> sessionComboBox;
    @javafx.fxml.FXML
    private TextField scoreTextField;
    @javafx.fxml.FXML
    private TextField maxScoreTextField;
    @javafx.fxml.FXML
    private TextArea commentsTextArea;
    @javafx.fxml.FXML
    private ComboBox<String> evaluationTypeComboBox;
    @javafx.fxml.FXML
    private Label scoreValidationLabel;

    @javafx.fxml.FXML
    public void initialize() {
        evaluationTypeComboBox.setItems(FXCollections.observableArrayList(
                "Physical Training", "Academic Performance", "Discipline",
                "Leadership", "Teamwork", "Technical Skills", "Overall Performance"
        ));


        cadetComboBox.setItems(FXCollections.observableArrayList(
                "CDT-001 - John Smith", "CDT-002 - Jane Doe", "CDT-003 - Mike Johnson",
                "CDT-004 - Sarah Wilson", "CDT-005 - David Brown"
        ));


        sessionComboBox.setItems(FXCollections.observableArrayList(
                "TRN-001 - Physical Training", "TRN-002 - Weapons Training",
                "TRN-003 - Leadership Workshop", "TRN-004 - Tactical Operations"
        ));


        maxScoreTextField.setText("100");


        scoreTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateScore();
        });

        maxScoreTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateScore();
        });
    }

    @javafx.fxml.FXML
    public void submitEvaluationButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {

            String cadetSelection = cadetComboBox.getValue();
            String cadetId = cadetSelection.split(" - ")[0];

            String sessionSelection = sessionComboBox.getValue();
            String sessionId = sessionSelection.split(" - ")[0];

            String evaluationType = evaluationTypeComboBox.getValue();
            double score = Double.parseDouble(scoreTextField.getText().trim());
            double maxScore = Double.parseDouble(maxScoreTextField.getText().trim());
            String comments = commentsTextArea.getText().trim();


            String evaluatorId = "INST-001";


            String evaluationId = "EVAL-" + UUID.randomUUID().toString().substring(0, 8);
            Evaluation evaluation = new Evaluation(
                    evaluationId, cadetId, evaluatorId, evaluationType,
                    score, maxScore, comments
            );


            List<Evaluation> evaluations = DataPersistenceManager.loadObjects("evaluations.bin");
            evaluations.add(evaluation);
            DataPersistenceManager.saveObjects(evaluations, "evaluations.bin");

            showAlert("Success", "Evaluation submitted successfully!\nEvaluation ID: " + evaluationId);
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to submit evaluation: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @javafx.fxml.FXML
    public void calculatePercentageButtonOnAction(ActionEvent actionEvent) {
        try {
            double score = Double.parseDouble(scoreTextField.getText().trim());
            double maxScore = Double.parseDouble(maxScoreTextField.getText().trim());

            if (maxScore > 0) {
                double percentage = (score / maxScore) * 100;
                String grade = getGrade(percentage);

                showAlert("Score Calculation",
                        String.format("Score: %.2f / %.2f\nPercentage: %.2f%%\nGrade: %s",
                                score, maxScore, percentage, grade));
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for score and max score.");
        }
    }

    private String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 85) return "A";
        else if (percentage >= 80) return "A-";
        else if (percentage >= 75) return "B+";
        else if (percentage >= 70) return "B";
        else if (percentage >= 65) return "B-";
        else if (percentage >= 60) return "C+";
        else if (percentage >= 55) return "C";
        else if (percentage >= 50) return "C-";
        else return "F";
    }

    private void validateScore() {
        try {
            if (!scoreTextField.getText().trim().isEmpty() && !maxScoreTextField.getText().trim().isEmpty()) {
                double score = Double.parseDouble(scoreTextField.getText().trim());
                double maxScore = Double.parseDouble(maxScoreTextField.getText().trim());

                if (score < 0) {
                    scoreValidationLabel.setText("Score cannot be negative");
                    scoreValidationLabel.setStyle("-fx-text-fill: red;");
                } else if (score > maxScore) {
                    scoreValidationLabel.setText("Score cannot exceed max score");
                    scoreValidationLabel.setStyle("-fx-text-fill: red;");
                } else {
                    double percentage = (score / maxScore) * 100;
                    scoreValidationLabel.setText(String.format("%.1f%%", percentage));
                    scoreValidationLabel.setStyle("-fx-text-fill: green;");
                }
            } else {
                scoreValidationLabel.setText("");
            }
        } catch (NumberFormatException e) {
            scoreValidationLabel.setText("Invalid number format");
            scoreValidationLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validateInputs() {
        if (cadetComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a cadet.");
            return false;
        }

        if (sessionComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a training session.");
            return false;
        }

        if (evaluationTypeComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select an evaluation type.");
            return false;
        }

        try {
            double score = Double.parseDouble(scoreTextField.getText().trim());
            double maxScore = Double.parseDouble(maxScoreTextField.getText().trim());

            if (score < 0) {
                showAlert("Validation Error", "Score cannot be negative.");
                return false;
            }

            if (maxScore <= 0) {
                showAlert("Validation Error", "Max score must be greater than 0.");
                return false;
            }

            if (score > maxScore) {
                showAlert("Validation Error", "Score cannot exceed max score.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter valid numeric values for score and max score.");
            return false;
        }

        if (commentsTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please provide evaluation comments.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        cadetComboBox.setValue(null);
        sessionComboBox.setValue(null);
        evaluationTypeComboBox.setValue(null);
        scoreTextField.clear();
        maxScoreTextField.setText("100");
        commentsTextArea.clear();
        scoreValidationLabel.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
