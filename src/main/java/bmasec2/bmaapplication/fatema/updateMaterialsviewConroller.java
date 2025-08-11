package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class updateMaterialsviewConroller {
    @FXML
    private ComboBox<String> trainingSessionComboBox;
    @FXML
    private TextField selectFileTextField;
    @FXML
    private ComboBox<String> targetBtachComboBox;

    private File selectedFile;
    private List<Training> trainingSessions;

    @FXML
    public void initialize() {

        trainingSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        ObservableList<String> sessionOptions = FXCollections.observableArrayList();
        for (Training session : trainingSessions) {
            sessionOptions.add(session.getSessionId() + " - " + session.getTopic());
        }
        trainingSessionComboBox.setItems(sessionOptions);


        targetBtachComboBox.setItems(FXCollections.observableArrayList(
                "Batch 2024-A", "Batch 2024-B", "All Batches"
        ));
    }

    @FXML
    public void browseButtonOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Training Material");
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            selectFileTextField.setText(selectedFile.getName());
        }
    }

    @FXML
    public void updateMaterialOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String selectedSession = trainingSessionComboBox.getValue();
            String sessionId = selectedSession.split(" - ")[0];
            String targetBatch = targetBtachComboBox.getValue();

            File materialsDir = new File("materials");
            if (!materialsDir.exists()) {
                materialsDir.mkdirs();
            }
            File destinationFile = new File(materialsDir, UUID.randomUUID().toString() + "_" + selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


            showAlert("Success", "Training material \"" + selectedFile.getName() + "\" uploaded for session " + sessionId + " and target batch " + targetBatch + ".");
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to upload material: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (trainingSessionComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a training session.");
            return false;
        }

        if (selectedFile == null) {
            showAlert("Validation Error", "Please select a file to upload.");
            return false;
        }

        if (targetBtachComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a target batch.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        trainingSessionComboBox.setValue(null);
        selectFileTextField.clear();
        targetBtachComboBox.setValue(null);
        selectedFile = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
