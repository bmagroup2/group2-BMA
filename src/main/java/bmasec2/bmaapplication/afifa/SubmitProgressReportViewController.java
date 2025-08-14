package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Report;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubmitProgressReportViewController {

    @FXML
    private TextArea submitweeklyreporttextare;

    private Cadet loggedInCadet;

    @FXML
    public void initialize() {

    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
    }

    @FXML
    public void weeklyreportsubmitonaction(ActionEvent actionEvent) {
        String reportContent = submitweeklyreporttextare.getText();

        if (reportContent.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please enter your progress report.");
            return;
        }

        
        if (reportContent.split("\\s+").length > 500) {
            showAlert(AlertType.WARNING, "Input Warning", "Report exceeds word limit (500 words).");
            return;
        }


        String reportId = UUID.randomUUID().toString();

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("reportType", "Weekly Progress");
        contentMap.put("cadetId", loggedInCadet != null ? loggedInCadet.getUserId() : "Unknown");
        contentMap.put("reportDetails", reportContent);

        Report newReport = new Report(
                reportId,
                "Progress Report",
                loggedInCadet != null ? loggedInCadet.getName() : "Unknown Cadet",
                contentMap
        );

        List<Report> reports = DataPersistenceManager.loadObjects("reports.dat");
        reports.add(newReport);
        DataPersistenceManager.saveObjects(reports, "reports.dat");

        showAlert(AlertType.INFORMATION, "Submission Successful", "Weekly progress report submitted successfully.");
        submitweeklyreporttextare.clear();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


