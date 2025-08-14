package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.Cadet;
import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class updateVaccinationStatusViewController
{
    @javafx.fxml.FXML
    private DatePicker dateAdministeredDatePicker;
    @javafx.fxml.FXML
    private TextField vaccineNameTextField;
    @javafx.fxml.FXML
    private ComboBox<String> selectCadetComboBox;

    @javafx.fxml.FXML
    public void initialize() {
        loadCadets();
        // Set default date to today
        dateAdministeredDatePicker.setValue(LocalDate.now());
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
    public void saveVaccinationOnActionButton(ActionEvent actionEvent) {
        try {
            // Validate inputs
            if (selectCadetComboBox.getValue() == null || selectCadetComboBox.getValue().equals("No cadets found")) {
                showAlert("Error", "Please select a cadet.");
                return;
            }
            
            if (vaccineNameTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter vaccine name.");
                return;
            }
            
            if (dateAdministeredDatePicker.getValue() == null) {
                showAlert("Error", "Please select administration date.");
                return;
            }
            
            // Extract cadet ID from combo box selection
            String selectedCadet = selectCadetComboBox.getValue();
            String cadetId = selectedCadet.split(" - ")[0];
            
            // Get form data
            String vaccineName = vaccineNameTextField.getText().trim();
            LocalDate adminDate = dateAdministeredDatePicker.getValue();
            Date vaccinationDate = Date.from(adminDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Create new medical record for vaccination
            String recordId = UUID.randomUUID().toString();
            String diagnosis = "Vaccination Record";
            String treatment = "Administered: " + vaccineName + " on " + adminDate.toString();
            String notes = "Vaccination status updated for " + vaccineName;
            
            MedicalRecord vaccinationRecord = new MedicalRecord(
                recordId,
                cadetId,
                vaccinationDate,
                diagnosis,
                treatment,
                notes
            );
            
            // Save to data persistence
            DataPersistenceManager.addMedicalRecordAndSave(vaccinationRecord);
            
            // Show success message
            showAlert("Success", "Vaccination status updated successfully!");
            
            // Clear form
            clearForm();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update vaccination status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        selectCadetComboBox.setValue(null);
        vaccineNameTextField.clear();
        dateAdministeredDatePicker.setValue(LocalDate.now());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}