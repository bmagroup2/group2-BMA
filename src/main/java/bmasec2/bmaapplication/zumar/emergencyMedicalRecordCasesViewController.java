package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class emergencyMedicalRecordCasesViewController {

    @FXML
    private TextArea diagnosisTextArea;
    @FXML
    private TableView<MedicalRecord> emergencyCaseLogTableView;
    @FXML
    private TableColumn<MedicalRecord, String> diagnosisColn;
    @FXML
    private TableColumn<MedicalRecord, String> cadetNameColn;
    @FXML
    private ComboBox<String> cadetComboBox;
    @FXML
    private TextArea symptomsTextArea;
    @FXML
    private TableColumn<MedicalRecord, String> dateColn;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCadets();
        loadEmergencyCases();
    }

    private void setupTableColumns() {
        cadetNameColn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        diagnosisColn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        dateColn.setCellValueFactory(cellData -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return new javafx.beans.property.SimpleStringProperty(dateFormat.format(cellData.getValue().getDate()));
        });
    }

    private void loadCadets() {
        List<bmasec2.bmaapplication.User> users = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> cadetOptions = FXCollections.observableArrayList();

        for (bmasec2.bmaapplication.User user : users) {
            if (user instanceof Cadet) {
                Cadet cadet = (Cadet) user;
                cadetOptions.add(cadet.getUserId() + " - " + cadet.getName());
            }
        }

        if (cadetOptions.isEmpty()) {
            cadetOptions.add("No cadets found");
        }

        cadetComboBox.setItems(cadetOptions);
    }

    private void loadEmergencyCases() {
        List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
        ObservableList<MedicalRecord> emergencyCases = FXCollections.observableArrayList(
                medicalRecords.stream()
                        .filter(record -> record.getDiagnosis() != null && record.getDiagnosis().contains("Emergency Case"))
                        .collect(Collectors.toList())
        );
        emergencyCaseLogTableView.setItems(emergencyCases);
    }

    @FXML
    public void emergencyLogOnActionButton(ActionEvent actionEvent) {
        try {
            if (cadetComboBox.getValue() == null || cadetComboBox.getValue().equals("No cadets found")) {
                showAlert("Error", "Please select a cadet.");
                return;
            }
            if (symptomsTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter symptoms.");
                return;
            }
            if (diagnosisTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter diagnosis.");
                return;
            }

            String selectedCadet = cadetComboBox.getValue();
            String cadetId = selectedCadet.split(" - ")[0];
            String symptoms = symptomsTextArea.getText().trim();
            String diagnosis = "Emergency Case: " + diagnosisTextArea.getText().trim();
            String treatment = "Symptoms: " + symptoms;
            String notes = "Emergency case logged for cadet " + cadetId;

            MedicalRecord emergencyRecord = new MedicalRecord(
                    UUID.randomUUID().toString(),
                    cadetId,
                    new Date(),
                    diagnosis,
                    treatment,
                    notes
            );

            DataPersistenceManager.addMedicalRecordAndSave(emergencyRecord);
            showAlert("Success", "Emergency case logged successfully!");
            clearForm();
            loadEmergencyCases();

        } catch (Exception e) {
            showAlert("Error", "Failed to log emergency case: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        cadetComboBox.setValue(null);
        symptomsTextArea.clear();
        diagnosisTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

