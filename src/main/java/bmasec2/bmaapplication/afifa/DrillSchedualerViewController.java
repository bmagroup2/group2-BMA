package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DrillSchedualerViewController {

    @FXML
    private ComboBox<String> drillcadetgroupcombobox;
    @FXML
    private DatePicker drilldatepicker;
    @FXML
    private TextField drillactivitytextfield;
    @FXML
    private TextField drilltimetextfield;
    @FXML
    private TextField drilllocationtextfield;
    @FXML
    private ListView<String> drillscheduleddrillslistview;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        // Populate cadet group combobox (placeholder)
        ObservableList<String> cadetGroups = FXCollections.observableArrayList(
                "Alpha Company", "Bravo Company", "Charlie Company", "Delta Company"
        );
        drillcadetgroupcombobox.setItems(cadetGroups);

        loadScheduledDrills();
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
        loadScheduledDrills();
    }

    @FXML
    public void drillsaveschedulonaction(ActionEvent actionEvent) {
        String cadetGroup = drillcadetgroupcombobox.getValue();
        LocalDate drillDate = drilldatepicker.getValue();
        String drillActivity = drillactivitytextfield.getText();
        String drillTime = drilltimetextfield.getText();
        String drillLocation = drilllocationtextfield.getText();

        if (cadetGroup == null || drillDate == null || drillActivity.isEmpty() || drillTime.isEmpty() || drillLocation.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(drillTime);
            LocalDateTime dateTime = LocalDateTime.of(drillDate, time);
            Date scheduledDateTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());


            String sessionId = UUID.randomUUID().toString();

            Training newDrill = new Training(
                    sessionId,
                    drillActivity,
                    scheduledDateTime,
                    drillLocation,
                    loggedInSupervisor != null ? loggedInSupervisor.getName() : "Unknown Supervisor",
                    0
            );

            List<Training> trainings = DataPersistenceManager.loadObjects("trainings.dat");
            trainings.add(newDrill);
            DataPersistenceManager.saveObjects(trainings, "trainings.dat");

            showAlert(AlertType.INFORMATION, "Success", "Drill scheduled successfully!");
            clearForm();
            loadScheduledDrills();

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Input Error", "Invalid time format. Please use HH:mm (e.g., 09:00).");
        }
    }

    private void loadScheduledDrills() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        ObservableList<String> drills = FXCollections.observableArrayList();

        List<Training> scheduledDrills = allTrainings.stream()
                .filter(training -> training.getTopic().toLowerCase().contains("drill"))
                .collect(Collectors.toList());

        if (scheduledDrills.isEmpty()) {
            drills.add("No drills scheduled.");
        } else {
            for (Training drill : scheduledDrills) {
                drills.add(drill.getTopic() + " on " + drill.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() + " at " + drill.getLocation() + " (" + drill.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime() + ")");
            }
        }
        drillscheduleddrillslistview.setItems(drills);
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        drillcadetgroupcombobox.getSelectionModel().clearSelection();
        drilldatepicker.setValue(null);
        drillactivitytextfield.clear();
        drilltimetextfield.clear();
        drilllocationtextfield.clear();
    }
}


