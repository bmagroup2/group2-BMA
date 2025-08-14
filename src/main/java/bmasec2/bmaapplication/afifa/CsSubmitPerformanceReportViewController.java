package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Report;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CsSubmitPerformanceReportViewController {
    @FXML
    private TextArea performancetextarea;
    @FXML
    private ComboBox<String> performancecadetcombobox;
    @FXML
    private ComboBox<String> performanceevaluationcombobox;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        // Populate cadet combobox
        List<Cadet> cadets = DataPersistenceManager.loadObjects("cadets.dat");
        ObservableList<String> cadetNames = FXCollections.observableArrayList();
        for (Cadet cadet : cadets) {
            cadetNames.add(cadet.getName());
        }
        performancecadetcombobox.setItems(cadetNames);

        // Populate evaluation type combobox
        ObservableList<String> evaluationTypes = FXCollections.observableArrayList(
                "Discipline", "Academics", "Drills", "Overall"
        );
        performanceevaluationcombobox.setItems(evaluationTypes);
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
    }

    @FXML
    public void persubmitreportonaction(ActionEvent actionEvent) {
        String selectedCadet = performancecadetcombobox.getValue();
        String evaluationType = performanceevaluationcombobox.getValue();
        String reportContent = performancetextarea.getText();

        if (selectedCadet == null || selectedCadet.isEmpty() || evaluationType == null || evaluationType.isEmpty() || reportContent.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }


        String reportId = UUID.randomUUID().toString();


        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("cadetName", selectedCadet);
        contentMap.put("evaluationType", evaluationType);
        contentMap.put("reportDetails", reportContent);

        Report newReport = new Report(
                reportId,
                "Performance Report",
                loggedInSupervisor != null ? loggedInSupervisor.getName() : "Unknown Supervisor",
                contentMap.toString()
        );

        List<Report> reports = DataPersistenceManager.loadObjects("reports.bin");
        reports.add(newReport);
        DataPersistenceManager.saveObjects(reports, "reports.bin");

        showAlert(AlertType.INFORMATION, "Submission Successful", "Performance report submitted for " + selectedCadet + ".");
        clearForm();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        performancecadetcombobox.getSelectionModel().clearSelection();
        performanceevaluationcombobox.getSelectionModel().clearSelection();
        performancetextarea.clear();
    }
}


