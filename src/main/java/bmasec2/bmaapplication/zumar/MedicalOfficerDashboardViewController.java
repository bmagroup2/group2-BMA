package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.MedicalRecord;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalOfficerDashboardViewController
{
    @javafx.fxml.FXML
    private Label todaysCheckupsAppointmentLabel;
    @javafx.fxml.FXML
    private ListView<String> scheduledCheckupsForTodayListView;
    @javafx.fxml.FXML
    private Label pendingHealthCasesLable;

    @javafx.fxml.FXML
    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {

        List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
        

        long pendingCases = medicalRecords.stream()
                .filter(record -> record.getTreatment() == null || record.getTreatment().isEmpty() || 
                                record.getDiagnosis() == null || record.getDiagnosis().isEmpty())
                .count();
        
        pendingHealthCasesLable.setText(pendingCases + " Cases");
        

        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        

        List<MedicalRecord> todaysCheckups = medicalRecords.stream()
                .filter(record -> {
                    LocalDate recordDate = record.getDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return recordDate.equals(today);
                })
                .collect(Collectors.toList());
        
        todaysCheckupsAppointmentLabel.setText(todaysCheckups.size() + " Appointments");
        
        
        ObservableList<String> checkupsList = FXCollections.observableArrayList();
        for (MedicalRecord record : todaysCheckups) {
            String checkupInfo = "Cadet ID: " + record.getCadetId() + 
                               " - " + (record.getDiagnosis() != null ? record.getDiagnosis() : "General Checkup");
            checkupsList.add(checkupInfo);
        }
        
        if (checkupsList.isEmpty()) {
            checkupsList.add("No checkups scheduled for today");
        }
        
        scheduledCheckupsForTodayListView.setItems(checkupsList);
    }
}