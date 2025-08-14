package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.MissionEvent;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class EventParticipationViewController {
    @FXML
    private TableView<CadetParticipation> participanttableview;
    @FXML
    private TableColumn<CadetParticipation, String> participationnamecolumn;
    @FXML
    private TableColumn<CadetParticipation, String> participationidcolumn;
    @FXML
    private TableColumn<CadetParticipation, String> participationstatuscolumn;
    @FXML
    private ComboBox<String> trackeventcombobox;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        participationnamecolumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
        participationidcolumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        participationstatuscolumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        List<MissionEvent> events = DataPersistenceManager.loadObjects("mission_events.dat");
        ObservableList<String> eventNames = FXCollections.observableArrayList();
        for (MissionEvent event : events) {
            eventNames.add(event.getEventName());
        }
        trackeventcombobox.setItems(eventNames);
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
    }

    @FXML
    public void eventviewparticipationonaction(ActionEvent actionEvent) {
        String selectedEventName = trackeventcombobox.getValue();

        if (selectedEventName == null || selectedEventName.isEmpty()) {
            showAlert(AlertType.ERROR, "Selection Error", "Please select an event.");
            return;
        }

        List<MissionEvent> allEvents = DataPersistenceManager.loadObjects("mission_events.dat");
        MissionEvent selectedEvent = allEvents.stream()
                .filter(event -> event.getEventName().equals(selectedEventName))
                .findFirst()
                .orElse(null);

        if (selectedEvent == null) {
            showAlert(AlertType.ERROR, "Data Error", "Selected event not found.");
            return;
        }

        List<Cadet> allCadets = DataPersistenceManager.loadObjects("cadets.dat");
        ObservableList<CadetParticipation> participationList = FXCollections.observableArrayList();

        for (Cadet cadet : allCadets) {
            String status = selectedEvent.getParticipants().contains(cadet.getUserId()) ? "Present" : "Absent";

            participationList.add(new CadetParticipation(cadet.getName(), cadet.getUserId(), status));
        }

        participanttableview.setItems(participationList);
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper class for TableView
    public static class CadetParticipation {
        private final String cadetName;
        private final String cadetId;
        private final String status;

        public CadetParticipation(String cadetName, String cadetId, String status) {
            this.cadetName = cadetName;
            this.cadetId = cadetId;
            this.status = status;
        }

        public String getCadetName() {
            return cadetName;
        }

        public String getCadetId() {
            return cadetId;
        }

        public String getStatus() {
            return status;
        }
    }
}


