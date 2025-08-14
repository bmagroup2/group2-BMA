package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Leave;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSLeaveRequestViewController {

    @FXML
    private Label leavereasonlabel;
    @FXML
    private Label leavedaterangelabel;
    @FXML
    private ListView<Leave> cadetleavependingrequestlistview;
    @FXML
    private TextArea leaverequesttextarea;
    @FXML
    private Label leavecadetnamelabel;

    private ObservableList<Leave> pendingLeaveRequests = FXCollections.observableArrayList();
    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        loadPendingLeaveRequests();

        cadetleavependingrequestlistview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayLeaveRequestDetails(newValue);
            }
        });
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
        loadPendingLeaveRequests();
    }

    private void loadPendingLeaveRequests() {
        List<Leave> allLeaveRequests = DataPersistenceManager.loadObjects("leave_requests.dat");
        pendingLeaveRequests.setAll(allLeaveRequests.stream()
                .filter(leave -> leave.getStatus().equals("Pending"))

                .collect(Collectors.toList()));
        cadetleavependingrequestlistview.setItems(pendingLeaveRequests);
    }

    private void displayLeaveRequestDetails(Leave leave) {
        leavecadetnamelabel.setText("Cadet: " + leave.getCadetId());
        leavedaterangelabel.setText("Date: " + leave.getStartDate() + " to " + leave.getEndDate());
        leavereasonlabel.setText("Reason: " + leave.getReason());
        leaverequesttextarea.setText(leave.getReason());
    }

    @FXML
    public void leaveapproveonaction(ActionEvent actionEvent) {
        Leave selectedLeave = cadetleavependingrequestlistview.getSelectionModel().getSelectedItem();
        if (selectedLeave != null) {
            selectedLeave.setStatus("Approved");
            if (loggedInSupervisor != null) {
                selectedLeave.setApprovedBy(loggedInSupervisor.getName());
            }
            saveLeaveRequests();
            loadPendingLeaveRequests();
            clearDetails();
        }
    }

    @FXML
    public void leaverejectonaction(ActionEvent actionEvent) {
        Leave selectedLeave = cadetleavependingrequestlistview.getSelectionModel().getSelectedItem();
        if (selectedLeave != null) {
            selectedLeave.setStatus("Rejected");
            if (loggedInSupervisor != null) {
                selectedLeave.setApprovedBy(loggedInSupervisor.getName());
            }
            saveLeaveRequests();
            loadPendingLeaveRequests();
            clearDetails();
        }
    }

    private void saveLeaveRequests() {
        List<Leave> allLeaveRequests = DataPersistenceManager.loadObjects("leave_requests.dat");
        // Update the status of the modified leave request
        Optional<Leave> existingLeave = allLeaveRequests.stream()
                .filter(l -> l.getLeaveId().equals(cadetleavependingrequestlistview.getSelectionModel().getSelectedItem().getLeaveId()))
                .findFirst();
        existingLeave.ifPresent(leave -> leave.setStatus(cadetleavependingrequestlistview.getSelectionModel().getSelectedItem().getStatus()));

        DataPersistenceManager.saveObjects(allLeaveRequests, "leave_requests.dat");
    }

    private void clearDetails() {
        leavecadetnamelabel.setText("Cadet:");
        leavedaterangelabel.setText("Date:");
        leavereasonlabel.setText("Reason:");
        leaverequesttextarea.clear();
    }
}


