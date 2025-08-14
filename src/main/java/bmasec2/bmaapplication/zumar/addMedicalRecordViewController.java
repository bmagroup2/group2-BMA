package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.Cadet;
import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class addMedicalRecordViewController
{
    @javafx.fxml.FXML
    private TextArea notesTextArea;
    @javafx.fxml.FXML
    private TextField vaccinationStatusTextField;
    @javafx.fxml.FXML
    private ComboBox<String> selectCadetComboBox;
    @javafx.fxml.FXML
    private TextField healthDataTextField;

    @javafx.fxml.FXML
    public void initialize() {
        loadCadets();
    }

    private void loadCadets() {
        // Load cadets from data persistence
        List<Cadet> cadets = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> cadetOptions = FXCollections.observableArrayList();
        
        for (Cadet cadet : cadets) {
            if (cadet instanceof Cadet) {
                cadetOptions.add(cadet.getUserId() + " - " + cadet.getName());
            }
        }
        
        if (cadetOptions.isEmpty()) {
            cadetOptions.add("No cadets found");
        }
        
        selectCadetComboBox.setItems(cadetOptions);
    }

    @javafx.fxml.FXML
    public void medicalRecordSaveOnActionButton(ActionEvent actionEvent) {
        try {
            // Validate inputs
            if (selectCadetComboBox.getValue() == null || selectCadetComboBox.getValue().equals("No cadets found")) {
                showAlert("Error", "Please select a cadet.");
                return;
            }
            
            if (healthDataTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter health data.");
                return;
            }
            
            // Extract cadet ID from combo box selection
            String selectedCadet = selectCadetComboBox.getValue();
            String cadetId = selectedCadet.split(" - ")[0];
            
            // Create new medical record
            String recordId = UUID.randomUUID().toString();
            String healthData = healthDataTextField.getText().trim();
            String vaccinationStatus = vaccinationStatusTextField.getText().trim();
            String notes = notesTextArea.getText().trim();
            
            // Combine health data and vaccination status as diagnosis
            String diagnosis = "Health Data: " + healthData;
            if (!vaccinationStatus.isEmpty()) {
                diagnosis += " | Vaccination: " + vaccinationStatus;
            }
            
            MedicalRecord newRecord = new MedicalRecord(
                recordId,
                cadetId,
                new Date(),
                diagnosis,
                "Pending", // Default treatment status
                notes
            );
            
            // Save to data persistence
            DataPersistenceManager.addMedicalRecordAndSave(newRecord);
            
            // Show success message
            showAlert("Success", "Medical record saved successfully!");
            
            // Clear form
            clearForm();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to save medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        selectCadetComboBox.setValue(null);
        healthDataTextField.clear();
        vaccinationStatusTextField.clear();
        notesTextArea.clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}