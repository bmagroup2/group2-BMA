package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.shanin.Commandant;
import bmasec2.bmaapplication.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PublishResultViewController {

    @FXML private TextArea finalResultsTextArea;
    @FXML private ComboBox<String> selectBatchComboBox;
    @FXML private ComboBox<String> selectSemesterComboBox;

    private static final String USERS_FILE = "users.dat";
    private static final String RESULTS_FILE = "published_results.dat";


    private Commandant loggedInCommandant;

    @FXML
    public void initialize() {

        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        List<String> batches = allUsers.stream()
                .filter(user -> user instanceof Cadet)
                .map(user -> ((Cadet) user).getBatch())
                .distinct()
                .collect(Collectors.toList());
        selectBatchComboBox.getItems().addAll(batches);


        selectSemesterComboBox.getItems().addAll("Semester 1", "Semester 2", "Semester 3");

        
        Optional<User> commandantOptional = allUsers.stream()
                .filter(user -> user.getRole().equals("Commandant"))
                .findFirst();
        if (commandantOptional.isPresent()) {
            loggedInCommandant = (Commandant) commandantOptional.get();
        } else {
            
            loggedInCommandant = new Commandant("CMD-001", "Dummy Commandant", "cmd@bma.com", "password", "CMD-001", "1234567890");
            allUsers.add(loggedInCommandant);
            DataPersistenceManager.saveObjects(allUsers, USERS_FILE);
        }

        selectBatchComboBox.setOnAction(event -> displayResultsPreview());
        selectSemesterComboBox.setOnAction(event -> displayResultsPreview());
    }

    private void displayResultsPreview() {
        String selectedBatch = selectBatchComboBox.getValue();
        String selectedSemester = selectSemesterComboBox.getValue();

        if (selectedBatch != null && selectedSemester != null) {
            
            String results = "--- Results for " + selectedBatch + " - " + selectedSemester + " ---\n\n";
            results += "Cadet ID | Name | Score | Status\n";
            results += "-------------------------------------\n";
            results += "C-001    | John Doe | 85.5  | Passed\n";
            results += "C-002    | Jane Smith | 72.0  | Passed\n";
            results += "C-003    | Peter Jones | 60.0  | Failed\n";
            results += "\n\nThese results are ready for official publication.";
            finalResultsTextArea.setText(results);
        } else {
            finalResultsTextArea.setText("Select a Batch and Semester to preview results.");
        }
    }

    @FXML
    public void confirmAndPublishBtnOnAction(ActionEvent actionEvent) {
        String selectedBatch = selectBatchComboBox.getValue();
        String selectedSemester = selectSemesterComboBox.getValue();

        if (selectedBatch == null || selectedSemester == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select both a Batch and a Semester.");
            return;
        }

        if (loggedInCommandant == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Commandant not loaded. Cannot publish results.");
            return;
        }


        boolean published = loggedInCommandant.publishResults(selectedBatch, selectedSemester);

        if (published) {

            List<String> publishedRecords = DataPersistenceManager.loadObjects(RESULTS_FILE);
            String record = selectedBatch + "-" + selectedSemester + "-Published";
            if (!publishedRecords.contains(record)) {
                publishedRecords.add(record);
                DataPersistenceManager.saveObjects(publishedRecords, RESULTS_FILE);
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Results for " + selectedBatch + " - " + selectedSemester + " published successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Failed to publish results. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

