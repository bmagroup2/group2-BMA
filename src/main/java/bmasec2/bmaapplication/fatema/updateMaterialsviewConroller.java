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

    private static final String TRAINING_MATERIALS_FILE = "training_materials.dat";
    private static final String TRAINING_SESSIONS_FILE = "training_sessions.dat";
    private static final String CADETS_FILE = "cadets.dat";
    private static final String UPLOAD_DIR = "uploads/training_materials";

    @FXML
    public void initialize() {

        new File(UPLOAD_DIR).mkdirs();

        populateTrainingSessions();
        populateTargetBatches();
    }

    private void populateTrainingSessions() {

        ObservableList<String> sessions = FXCollections.observableArrayList("Session 1: Marksmanship", "Session 2: First Aid", "Session 3: Navigation");
        trainingSessionComboBox.setItems(sessions);
    }

    private void populateTargetBatches() {

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

            Path destinationPath = Paths.get(UPLOAD_DIR, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);


            String materialId = UUID.randomUUID().toString();

            String uploadedByUserId = "instructor123"; 

            bmasec2.bmaapplication.model.TrainingMaterial newMaterial = new bmasec2.bmaapplication.model.TrainingMaterial(
                    materialId,
                    trainingTopic,
                    trainingTopic,
                    targetBatch,
                    selectedFile.getName(),
                    destinationPath.toString(),
                    uploadedByUserId
            );

            
            List<bmasec2.bmaapplication.model.TrainingMaterial> trainingMaterials = DataPersistenceManager.loadObjects(TRAINING_MATERIALS_FILE);
            trainingMaterials.add(newMaterial);
            DataPersistenceManager.saveObjects(trainingMaterials, TRAINING_MATERIALS_FILE);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Training material uploaded successfully!");


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


