package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.SystemSettings;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class messStaffDutyRosterViewController {

    @FXML
    private TextField remarksTextField;
    @FXML
    private TextField dutyTimeTextField;
    @FXML
    private ComboBox<String> staffMemberComboBox;
    @FXML
    private ListView<String> messStaffDutyRosterListView;
    @FXML
    private DatePicker dateAssignDutyDatePicker;

    @FXML
    public void initialize() {
        loadStaffMembers();
        loadDutyRoster();
        dateAssignDutyDatePicker.setValue(LocalDate.now());
    }

    private void loadStaffMembers() {
        List<User> users = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> staffMembers = FXCollections.observableArrayList();
        users.stream()
                .filter(user -> user.getRole().equals("Mess Staff")) // Assuming a 'Mess Staff' role exists
                .forEach(user -> staffMembers.add(user.getUserId() + " - " + user.getName()));

        if (staffMembers.isEmpty()) {
            staffMembers.add("No Mess Staff Found");
        }
        staffMemberComboBox.setItems(staffMembers);
        staffMemberComboBox.setValue(staffMembers.get(0));
    }

    private void loadDutyRoster() {
        // For simplicity, we'll store duty rosters as SystemSettings or similar
        // In a real app, you'd have a dedicated DutyRoster model
        List<SystemSettings> dutyRosters = DataPersistenceManager.loadObjects("system_settings.dat");
        ObservableList<String> rosterEntries = FXCollections.observableArrayList();

        dutyRosters.stream()
                .filter(setting -> setting.getSettingName().startsWith("DutyRoster_"))
                .forEach(setting -> rosterEntries.add(setting.getSettingValue()));

        if (rosterEntries.isEmpty()) {
            rosterEntries.add("No duty roster entries found.");
        }
        messStaffDutyRosterListView.setItems(rosterEntries);
    }

    @FXML
    public void saveRosterOnActionButton(ActionEvent actionEvent) {
        try {
            if (staffMemberComboBox.getValue() == null || staffMemberComboBox.getValue().equals("No Mess Staff Found")) {
                showAlert("Error", "Please select a staff member.");
                return;
            }
            if (dateAssignDutyDatePicker.getValue() == null) {
                showAlert("Error", "Please select a date.");
                return;
            }
            if (dutyTimeTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter duty time.");
                return;
            }

            String selectedStaff = staffMemberComboBox.getValue();
            String staffId = selectedStaff.split(" - ")[0];
            LocalDate localDate = dateAssignDutyDatePicker.getValue();
            Date dutyDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String dutyTime = dutyTimeTextField.getText().trim();
            String remarks = remarksTextField.getText().trim();

            String rosterEntry = String.format("Date: %s, Staff: %s, Time: %s, Remarks: %s",
                    localDate.toString(), selectedStaff, dutyTime, remarks.isEmpty() ? "N/A" : remarks);

            // Save as a SystemSetting for simplicity
            SystemSettings newDuty = new SystemSettings(
                    "DutyRoster_" + UUID.randomUUID().toString(),
                    "DutyRosterEntry",
                    rosterEntry,
                    "Mess Officer"
            );
            DataPersistenceManager.addSystemSettingAndSave(newDuty);

            showAlert("Success", "Duty roster saved successfully!");
            clearForm();
            loadDutyRoster();

        } catch (Exception e) {
            showAlert("Error", "Failed to save duty roster: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        staffMemberComboBox.setValue(null);
        dateAssignDutyDatePicker.setValue(LocalDate.now());
        dutyTimeTextField.clear();
        remarksTextField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}