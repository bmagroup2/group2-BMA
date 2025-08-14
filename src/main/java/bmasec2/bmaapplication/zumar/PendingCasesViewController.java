package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PendingCasesViewController
{
    @javafx.fxml.FXML
    private Label caseDetailsLabel;
    @javafx.fxml.FXML
    private TableView<PendingCase> pendingHealthCasesTableView;
    @javafx.fxml.FXML
    private TableColumn<PendingCase, String> dateFlaggedColn;
    @javafx.fxml.FXML
    private TableColumn<PendingCase, String> cadetNameColn;
    @javafx.fxml.FXML
    private TextArea statusRemarkTextArea;
    @javafx.fxml.FXML
    private TableColumn<PendingCase, String> issueColn;

    private ObservableList<PendingCase> pendingCases = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    public void initialize() {
        setupTableColumns();
        loadPendingCases();
        setupTableSelection();
    }

    private void setupTableColumns() {
        cadetNameColn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        issueColn.setCellValueFactory(new PropertyValueFactory<>("issue"));
        dateFlaggedColn.setCellValueFactory(new PropertyValueFactory<>("dateFlagged"));
    }

    private void setupTableSelection() {
        pendingHealthCasesTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    caseDetailsLabel.setText("Case Details for: " + newValue.getCadetId());
                    statusRemarkTextArea.setText(newValue.getNotes());
                }
            }
        );
    }

    private void loadPendingCases() {
        // Load medical records to find pending cases
        List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
        
        // Filter pending cases (records with no treatment or incomplete diagnosis)
        List<MedicalRecord> pendingRecords = medicalRecords.stream()
                .filter(record -> record.getTreatment() == null || record.getTreatment().isEmpty() || 
                                record.getTreatment().equals("Pending") ||
                                record.getDiagnosis() == null || record.getDiagnosis().isEmpty())
                .collect(Collectors.toList());
        
        pendingCases.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (MedicalRecord record : pendingRecords) {
            PendingCase pendingCase = new PendingCase(
                record.getCadetId(),
                record.getDiagnosis() != null ? record.getDiagnosis() : "Undiagnosed",
                dateFormat.format(record.getDate()),
                record.getNotes() != null ? record.getNotes() : "No notes available"
            );
            pendingCases.add(pendingCase);
        }
        
        if (pendingCases.isEmpty()) {
            PendingCase noCase = new PendingCase("No pending cases", "All cases resolved", "N/A", "No pending health cases found");
            pendingCases.add(noCase);
        }
        
        pendingHealthCasesTableView.setItems(pendingCases);
    }

    @javafx.fxml.FXML
    public void markAsResolvedOnActionButton(ActionEvent actionEvent) {
        PendingCase selectedCase = pendingHealthCasesTableView.getSelectionModel().getSelectedItem();
        
        if (selectedCase == null) {
            showAlert("Error", "Please select a case to mark as resolved.");
            return;
        }
        
        if (selectedCase.getCadetId().equals("No pending cases")) {
            showAlert("Info", "No cases to resolve.");
            return;
        }
        
        // Update the medical record with resolution
        List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
        
        for (MedicalRecord record : medicalRecords) {
            if (record.getCadetId().equals(selectedCase.getCadetId()) && 
                (record.getTreatment() == null || record.getTreatment().isEmpty() || record.getTreatment().equals("Pending"))) {
                
                String remarks = statusRemarkTextArea.getText().trim();
                if (remarks.isEmpty()) {
                    remarks = "Case resolved";
                }
                
                record.setTreatment("Resolved: " + remarks);
                record.setNotes(record.getNotes() + " | Resolution: " + remarks);
                break;
            }
        }
        
        // Save updated records
        DataPersistenceManager.saveObjects(medicalRecords, "medical_records.dat");
        
        showAlert("Success", "Case marked as resolved successfully!");
        
        // Refresh the table
        loadPendingCases();
        statusRemarkTextArea.clear();
        caseDetailsLabel.setText("Case Details");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for table data
    public static class PendingCase {
        private final SimpleStringProperty cadetId;
        private final SimpleStringProperty issue;
        private final SimpleStringProperty dateFlagged;
        private final SimpleStringProperty notes;

        public PendingCase(String cadetId, String issue, String dateFlagged, String notes) {
            this.cadetId = new SimpleStringProperty(cadetId);
            this.issue = new SimpleStringProperty(issue);
            this.dateFlagged = new SimpleStringProperty(dateFlagged);
            this.notes = new SimpleStringProperty(notes);
        }

        public String getCadetId() { return cadetId.get(); }
        public String getIssue() { return issue.get(); }
        public String getDateFlagged() { return dateFlagged.get(); }
        public String getNotes() { return notes.get(); }
    }
}