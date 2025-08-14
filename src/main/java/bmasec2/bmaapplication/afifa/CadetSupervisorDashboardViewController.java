package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Leave;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class CadetSupervisorDashboardViewController {

    @FXML
    private ListView<String> supmycadetlistview;
    @FXML
    private ListView<String> supupcomingscheduleddrillslistview;
    @FXML
    private Label supattendenceissuelabel;
    @FXML
    private Label suppendingrequestlabel;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        
        supattendenceissuelabel.setText("Attendance Issues: None");
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
        if (loggedInSupervisor != null) {
            loadMyCadets();
            loadUpcomingDrills();
            loadPendingLeaveRequests();
        }
    }

    private void loadMyCadets() {
        
        List<Cadet> allCadets = DataPersistenceManager.loadObjects("cadets.dat");
        ObservableList<String> cadetNames = FXCollections.observableArrayList();
        for (Cadet cadet : allCadets) {
            cadetNames.add(cadet.getName() + " (Batch: " + cadet.getBatch() + ")");
        }
        supmycadetlistview.setItems(cadetNames);
    }

    private void loadUpcomingDrills() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        ObservableList<String> upcomingDrills = FXCollections.observableArrayList();

        LocalDate today = LocalDate.now();

        List<Training> drills = allTrainings.stream()
                .filter(training -> training.getTopic().toLowerCase().contains("drill"))
                .filter(training -> training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(today) || training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        if (drills.isEmpty()) {
            upcomingDrills.add("No upcoming drills scheduled.");
        } else {
            for (Training drill : drills) {
                upcomingDrills.add(drill.getTopic() + " on " + drill.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() + " at " + drill.getLocation());
            }
        }
        supupcomingscheduleddrillslistview.setItems(upcomingDrills);
    }

    private void loadPendingLeaveRequests() {
        List<Leave> allLeaveRequests = DataPersistenceManager.loadObjects("leave_requests.dat");
        long pendingCount = allLeaveRequests.stream()
                .filter(leave -> leave.getStatus().equals("Pending"))

                .count();
        suppendingrequestlabel.setText("Pending Leave Requests: " + pendingCount);
    }
}


