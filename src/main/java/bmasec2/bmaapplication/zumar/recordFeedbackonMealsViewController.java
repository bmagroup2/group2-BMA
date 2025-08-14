package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.Feedback;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class recordFeedbackonMealsViewController {

    @FXML
    private Label feedbackInfoLabel;
    @FXML
    private ListView<String> mealFeedbackListView;
    @FXML
    private DatePicker filterByDateDatePicker;
    @FXML
    private TextArea feedbackCommentsTextArea;

    private ObservableList<Feedback> allFeedbacks;

    @FXML
    public void initialize() {
        filterByDateDatePicker.setValue(LocalDate.now());
        loadAllFeedbacks();
        filterOnActionButton(null); // Load feedback for today on startup

        mealFeedbackListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayFeedbackDetails(newValue);
                }
            }
        );
    }

    private void loadAllFeedbacks() {
        allFeedbacks = FXCollections.observableArrayList(DataPersistenceManager.loadObjects("feedback.dat"));
    }

    private void displayFeedbackDetails(String selectedFeedbackString) {
        // Parse the selected string to find the actual Feedback object
        // This assumes the string format is "Date: [date], Cadet: [cadetId], Rating: [rating]"
        // A more robust solution would be to store Feedback objects directly in the ListView
        // or use a custom CellFactory.
        
        // For simplicity, let's try to find the feedback by matching parts of the string
        Feedback selectedFeedback = allFeedbacks.stream()
                .filter(feedback -> selectedFeedbackString.contains(feedback.getCadetId()) &&
                                     selectedFeedbackString.contains(String.valueOf(feedback.getRating())))
                .findFirst()
                .orElse(null);

        if (selectedFeedback != null) {
            feedbackInfoLabel.setText("Feedback from " + selectedFeedback.getCadetId() + " on " + new SimpleDateFormat("yyyy-MM-dd").format(selectedFeedback.getDate()));
            feedbackCommentsTextArea.setText(selectedFeedback.getComments());
        } else {
            feedbackInfoLabel.setText("Select a feedback to view details");
            feedbackCommentsTextArea.clear();
        }
    }

    @FXML
    public void filterOnActionButton(ActionEvent actionEvent) {
        try {
            LocalDate selectedDate = filterByDateDatePicker.getValue();
            if (selectedDate == null) {
                showAlert("Error", "Please select a date to filter.");
                return;
            }

            Date filterDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            List<Feedback> filteredList = allFeedbacks.stream()
                    .filter(feedback -> {
                        LocalDate feedbackLocalDate = feedback.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return feedbackLocalDate.isEqual(selectedDate);
                    })
                    .collect(Collectors.toList());

            ObservableList<String> displayList = FXCollections.observableArrayList();
            if (filteredList.isEmpty()) {
                displayList.add("No feedback recorded for this date.");
            } else {
                for (Feedback feedback : filteredList) {
                    displayList.add(String.format("Date: %s, Cadet: %s, Rating: %d",
                            new SimpleDateFormat("yyyy-MM-dd").format(feedback.getDate()),
                            feedback.getCadetId(),
                            feedback.getRating()));
                }
            }
            mealFeedbackListView.setItems(displayList);
            feedbackInfoLabel.setText("Select a feedback to view details");
            feedbackCommentsTextArea.clear();

        } catch (Exception e) {
            showAlert("Error", "Failed to filter feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}