package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.stream.Collectors;

public class CadetHeathReportsViewController {
    @FXML
    private TextArea healthsummarytextarea;
    @FXML
    private ComboBox<String> cadethealthcombobox;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {

        List<Cadet> cadets = DataPersistenceManager.loadObjects("cadets.dat");
        ObservableList<String> cadetNames = FXCollections.observableArrayList();
        for (Cadet cadet : cadets) {
            cadetNames.add(cadet.getName());
        }
        cadethealthcombobox.setItems(cadetNames);
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;

    }

    @FXML
    public void heathsummaryonaction(ActionEvent actionEvent) {
        String selectedCadetName = cadethealthcombobox.getValue();
        if (selectedCadetName != null && !selectedCadetName.isEmpty()) {
            List<MedicalRecord> allMedicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
            List<MedicalRecord> cadetRecords = allMedicalRecords.stream()
                    .filter(record -> record.getCadetId().equals(selectedCadetName))
                    .collect(Collectors.toList());

            if (cadetRecords.isEmpty()) {
                healthsummarytextarea.setText("No medical records found for " + selectedCadetName + ".");
            } else {
                StringBuilder summary = new StringBuilder();
                summary.append("Medical History for ").append(selectedCadetName).append(":\n\n");
                for (MedicalRecord record : cadetRecords) {
                    summary.append("Date: ").append(record.getDate()).append("\n");
                    summary.append("Diagnosis: ").append(record.getDiagnosis()).append("\n");
                    summary.append("Treatment: ").append(record.getTreatment()).append("\n");
                    summary.append("Notes: ").append(record.getNotes()).append("\n");
                    summary.append("-------------------------------------\n");
                }
                healthsummarytextarea.setText(summary.toString());
            }
        } else {
            healthsummarytextarea.setText("Please select a cadet to view health reports.");
        }
    }
}


