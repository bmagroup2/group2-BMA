package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Feedback;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class viewFeedbackViewController {
    @javafx.fxml.FXML
    private ComboBox<String> sessionComboBox;
    @javafx.fxml.FXML
    private DatePicker fromDatePicker;
    @javafx.fxml.FXML
    private DatePicker toDatePicker;
    @javafx.fxml.FXML
    private TableView<Feedback> feedbackTableView;
    @javafx.fxml.FXML
    private TableColumn<Feedback, String> feedbackIdColumn;
    @javafx.fxml.FXML
    private TableColumn<Feedback, String> cadetIdColumn;
    @javafx.fxml.FXML
    private TableColumn<Feedback, Integer> ratingColumn;
    @javafx.fxml.FXML
    private TableColumn<Feedback, String> commentsColumn;
    @javafx.fxml.FXML
    private TableColumn<Feedback, Date> dateColumn;
    @javafx.fxml.FXML
    private Label averageRatingLabel;
    @javafx.fxml.FXML
    private Label totalFeedbackLabel;
    @javafx.fxml.FXML
    private TextArea selectedFeedbackTextArea;

    private ObservableList<Feedback> feedbackData = FXCollections.observableArrayList();
    private List<Feedback> allFeedback;
    private List<Training> trainingSessions;

    @javafx.fxml.FXML
    public void initialize() {

        feedbackIdColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        cadetIdColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));

        feedbackTableView.setItems(feedbackData);


        feedbackTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayFeedbackDetails(newValue);
                    }
                }
        );


        loadTrainingSessions();
        loadFeedbackData();
    }

    private void loadTrainingSessions() {
        trainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        ObservableList<String> sessionOptions = FXCollections.observableArrayList();
        sessionOptions.add("All Sessions");

        for (Training session : trainingSessions) {
            sessionOptions.add(session.getSessionId() + " - " + session.getTopic());
        }

        sessionComboBox.setItems(sessionOptions);
        sessionComboBox.setValue("All Sessions");
    }

    private void loadFeedbackData() {
        allFeedback = DataPersistenceManager.loadObjects("feedback.bin");
      //  applyFilters();
    }

    @javafx.fxml.FXML
    public void loadFeedbackButtonOnAction(ActionEvent actionEvent) {
     //   applyFilters();
    }

    @javafx.fxml.FXML
    public void clearFiltersButtonOnAction(ActionEvent actionEvent) {
        sessionComboBox.setValue("All Sessions");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
     //   applyFilters();
    }

    @javafx.fxml.FXML
    public void exportFeedbackButtonOnAction(ActionEvent actionEvent) {
        if (feedbackData.isEmpty()) {
            showAlert("No Data", "No feedback data to export.");
            return;
        }

        StringBuilder exportData = new StringBuilder();
        exportData.append("Feedback Export Report\n");
        exportData.append("Generated on: ").append(new Date()).append("\n\n");
        exportData.append("Total Feedback: ").append(feedbackData.size()).append("\n");
        exportData.append("Average Rating: ").append(averageRatingLabel.getText()).append("\n\n");

        exportData.append("Detailed Feedback:\n");
        exportData.append("================\n\n");

        for (Feedback feedback : feedbackData) {
            exportData.append("Feedback ID: ").append(feedback.getFeedbackId()).append("\n");
            exportData.append("Session ID: ").append(feedback.getSessionId()).append("\n");
            exportData.append("Cadet ID: ").append(feedback.getCadetId()).append("\n");
            exportData.append("Rating: ").append(feedback.getRating()).append("/5\n");
            exportData.append("Date: ").append(feedback.getSubmissionDate()).append("\n");
            exportData.append("Comments: ").append(feedback.getComments()).append("\n");
            exportData.append("---\n\n");
        }


        showAlert("Export Complete", "Feedback data has been prepared for export.\n\n" +
                "Total records: " + feedbackData.size());
    }

    @javafx.fxml.FXML
    public void generateSummaryButtonOnAction(ActionEvent actionEvent) {
        if (feedbackData.isEmpty()) {
            showAlert("No Data", "No feedback data available for summary.");
            return;
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Feedback Summary Report\n");
        summary.append("======================\n\n");


        double avgRating = feedbackData.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        summary.append("Total Feedback: ").append(feedbackData.size()).append("\n");
        summary.append("Average Rating: ").append(String.format("%.2f", avgRating)).append("/5\n\n");


        summary.append("Rating Distribution:\n");
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            long count = feedbackData.stream().filter(f -> f.getRating() == finalI).count();
            double percentage = (count * 100.0) / feedbackData.size();
            summary.append(i).append(" stars: ").append(count).append(" (").append(String.format("%.1f", percentage)).append("%)\n");
        }


        summary.append("\nSession-wise Summary:\n");
        feedbackData.stream()
                .collect(Collectors.groupingBy(Feedback::getSessionId))
                .forEach((sessionId, feedbacks) -> {
                    double sessionAvg = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
                    summary.append("Session ").append(sessionId).append(": ")
                            .append(feedbacks.size()).append(" feedback(s), ")
                            .append("Avg: ").append(String.format("%.2f", sessionAvg)).append("/5\n");
                });

        showAlert("Feedback Summary", summary.toString());
    }

//    private void applyFilters() {
//        feedbackData.clear();
//
//        String selectedSession = sessionComboBox.getValue();
////        Date fromDate = fromDatePicker.getValue() != null ?
////                java.sql.Date.valueOf(fromDatePicker.getValue()) : null;
////        Date toDate = toDatePicker.getValue() != null ?
////                java.sql.Date.valueOf(toDatePicker.getValue()) : null;
//
//        for (Feedback feedback : allFeedback) {
//            boolean matchesSession = "All Sessions".equals(selectedSession) ||
//                    feedback.getSessionId().equals(selectedSession.split(" - ")[0]);
//
//            boolean matchesDateRange = true;
//            if (fromDate != null && feedback.getSubmissionDate().before(fromDate)) {
//                matchesDateRange = false;
//            }
//            if (toDate != null && feedback.getSubmissionDate().after(toDate)) {
//                matchesDateRange = false;
//            }
//
//            if (matchesSession && matchesDateRange) {
//                feedbackData.add(feedback);
//            }
//        }
//
//        updateStatistics();
//    }

    private void updateStatistics() {
        if (feedbackData.isEmpty()) {
            averageRatingLabel.setText("N/A");
            totalFeedbackLabel.setText("0");
            return;
        }

        double avgRating = feedbackData.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        averageRatingLabel.setText(String.format("%.2f/5", avgRating));
        totalFeedbackLabel.setText(String.valueOf(feedbackData.size()));


        if (avgRating >= 4.0) {
            averageRatingLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else if (avgRating >= 3.0) {
            averageRatingLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        } else {
            averageRatingLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void displayFeedbackDetails(Feedback feedback) {
        StringBuilder details = new StringBuilder();
        details.append("Feedback Details\n");
        details.append("================\n\n");
        details.append("Feedback ID: ").append(feedback.getFeedbackId()).append("\n");
        details.append("Session ID: ").append(feedback.getSessionId()).append("\n");
        details.append("Cadet ID: ").append(feedback.getCadetId()).append("\n");
        details.append("Rating: ").append(feedback.getRating()).append("/5\n");
        details.append("Type: ").append(feedback.getFeedbackType()).append("\n");
        details.append("Submission Date: ").append(feedback.getSubmissionDate()).append("\n\n");
        details.append("Comments:\n");
        details.append(feedback.getComments());

        selectedFeedbackTextArea.setText(details.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
