package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Leave;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LeaveRequestViewController {
    @FXML
    private ComboBox<String> leavetypecombobox;
    @FXML
    private DatePicker datepickerend;
    @FXML
    private DatePicker datepickerstart;
    @FXML
    private TextArea reasontextarea;

    private Cadet loggedInCadet;

    @FXML
    public void initialize() {
        ObservableList<String> leaveTypes = FXCollections.observableArrayList(
                "Casual Leave", "Sick Leave", "Annual Leave", "Special Leave"
        );
        leavetypecombobox.setItems(leaveTypes);
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
    }

    @FXML
    public void submitonaction(ActionEvent actionEvent) {
        String leaveType = leavetypecombobox.getValue();
        LocalDate startDate = datepickerstart.getValue();
        LocalDate endDate = datepickerend.getValue();
        String reason = reasontextarea.getText();

        if (leaveType == null || startDate == null || endDate == null || reason.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert(AlertType.ERROR, "Date Error", "Start date cannot be after end date.");
            return;
        }


        List<Leave> existingLeaves = DataPersistenceManager.loadObjects("leave_requests.dat");
        boolean hasOverlap = existingLeaves.stream()
                .filter(leave -> leave.getCadetId().equals(loggedInCadet.getUserId()))
                .anyMatch(leave -> {
                    LocalDate existingStartDate = leave.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate existingEndDate = leave.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return (startDate.isBefore(existingEndDate) || startDate.isEqual(existingEndDate)) &&
                           (endDate.isAfter(existingStartDate) || endDate.isEqual(existingStartDate));
                });

        if (hasOverlap) {
            showAlert(AlertType.WARNING, "Leave Conflict", "You have an overlapping or pending leave request.");
            return;
        }

        String leaveId = UUID.randomUUID().toString();
        Leave newLeave = new Leave(
                leaveId,
                loggedInCadet.getUserId(),
                Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                reason
        );

        existingLeaves.add(newLeave);
        DataPersistenceManager.saveObjects(existingLeaves, "leave_requests.dat");

        showAlert(AlertType.INFORMATION, "Success", "Leave request submitted for approval.");
        // In a real application, notify supervisor here
        System.out.println("Notification: Leave request submitted by " + loggedInCadet.getName() + " for supervisor approval.");
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
        leavetypecombobox.getSelectionModel().clearSelection();
        datepickerstart.setValue(null);
        datepickerend.setValue(null);
        reasontextarea.clear();
    }
}


