package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class updateMaterialsviewConroller {

    @FXML
    private ComboBox<String> trainingSessionComboBox;
    @FXML
    private ComboBox<String> targetBtachComboBox;
    @FXML
    private TextField selectFileTextField;

    private File selectedFile;

    private static final String TRAINING_MATERIALS_FILE = "training_materials.ser";
    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser"; // Assuming a file for training sessions
    private static final String CADETS_FILE = "cadets.ser"; // Assuming a file for cadets to get batches
    private static final String UPLOAD_DIR = "uploads/training_materials";

    @FXML
    public void initialize() {
        // Ensure upload directory exists
        new File(UPLOAD_DIR).mkdirs();

        populateTrainingSessions();
        populateTargetBatches();
    }

    private void populateTrainingSessions() {
        // In a real application, you would load actual training sessions
        // For now, populate with dummy data or load from a file if available
        ObservableList<String> sessions = FXCollections.observableArrayList("Session 1: Marksmanship", "Session 2: First Aid", "Session 3: Navigation");
        trainingSessionComboBox.setItems(sessions);
    }

    private void populateTargetBatches() {
        // In a real application, you would load actual cadet batches
        // For now, populate with dummy data or load from a file if available
        ObservableList<String> batches = FXCollections.observableArrayList("Batch A", "Batch B", "Batch C");
        targetBtachComboBox.setItems(batches);
    }

    @FXML
    void browseButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Material File");
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectFileTextField.setText(selectedFile.getName());
        }
    }

    @FXML
    void updateMaterialOnAction(ActionEvent event) {
        String trainingTopic = trainingSessionComboBox.getSelectionModel().getSelectedItem();
        String targetBatch = targetBtachComboBox.getSelectionModel().getSelectedItem();

        if (trainingTopic == null || targetBatch == null || selectedFile == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please select a training session, target batch, and a file.");
            return;
        }

        try {
            // Save the file to the uploads directory
            Path destinationPath = Paths.get(UPLOAD_DIR, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Create a new TrainingMaterial object
            String materialId = UUID.randomUUID().toString();
            // Assuming a dummy user for now. In a real app, this would be the logged-in instructor.
            String uploadedByUserId = "instructor123"; 

            TrainingMaterial newMaterial = new TrainingMaterial(
                    materialId,
                    trainingTopic, // Using topic as session ID for simplicity, adjust if actual session IDs are available
                    trainingTopic,
                    targetBatch,
                    selectedFile.getName(),
                    destinationPath.toString(),
                    uploadedByUserId
            );

            // Load existing materials, add new one, and save
            List<TrainingMaterial> trainingMaterials = DataPersistenceManager.loadObjects(TRAINING_MATERIALS_FILE);
            trainingMaterials.add(newMaterial);
            DataPersistenceManager.saveObjects(trainingMaterials, TRAINING_MATERIALS_FILE);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Training material uploaded successfully!");

            // Clear form fields
            trainingSessionComboBox.getSelectionModel().clearSelection();
            targetBtachComboBox.getSelectionModel().clearSelection();
            selectFileTextField.clear();
            selectedFile = null;

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Failed to upload material: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


