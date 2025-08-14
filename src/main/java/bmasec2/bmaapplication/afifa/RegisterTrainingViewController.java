package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterTrainingViewController {

    @FXML
    private TableColumn<Training, String> registerinstructorcolumn;
    @FXML
    private TableView<Training> registerttraningtableview;
    @FXML
    private TableColumn<Training, String> registertraningsessioncolumn;
    @FXML
    private TableColumn<Training, String> registerstatuscolumn;
    @FXML
    private TableColumn<Training, Integer> registerslotscolumn;
    @FXML
    private TableColumn<Training, String> registertraningdatecolumn;

    private Cadet loggedInCadet;
    private ObservableList<Training> availableTrainings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        registertraningsessioncolumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        registerinstructorcolumn.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        registertraningdatecolumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        registerslotscolumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants")); // Assuming this represents available slots
        registerstatuscolumn.setCellValueFactory(new PropertyValueFactory<>("status")); // Need to add a status property to Training class or derive it

        loadAvailableTrainings();
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
        loadAvailableTrainings();
    }

    private void loadAvailableTrainings() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        // Filter for trainings that have available slots and are not in the past
        availableTrainings.setAll(allTrainings.stream()
                .filter(training -> training.getMaxParticipants() > 0) // Assuming maxParticipants > 0 means slots available
                .filter(training -> training.getDateTime().after(new java.util.Date())) // Only future trainings
                .collect(Collectors.toList()));
        registerttraningtableview.setItems(availableTrainings);
    }

    @FXML
    public void registertraningonaction(ActionEvent actionEvent) {
        Training selectedTraining = registerttraningtableview.getSelectionModel().getSelectedItem();

        if (selectedTraining == null) {
            showAlert(AlertType.ERROR, "Selection Error", "Please select a training session to register for.");
            return;
        }

        if (selectedTraining.getMaxParticipants() <= 0) {
            showAlert(AlertType.WARNING, "Registration Failed", "No available slots for this training session.");
            return;
        }

        // Simulate registration: decrease available slots and add cadet to participants (if Training class had a participants list)
        // For now, just decrease maxParticipants as a proxy for registration
        selectedTraining.setMaxParticipants(selectedTraining.getMaxParticipants() - 1);

        // Save updated training data
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        for (int i = 0; i < allTrainings.size(); i++) {
            if (allTrainings.get(i).getSessionId().equals(selectedTraining.getSessionId())) {
                allTrainings.set(i, selectedTraining);
                break;
            }
        }
        DataPersistenceManager.saveObjects(allTrainings, "trainings.dat");

        showAlert(AlertType.INFORMATION, "Registration Successful", "You have successfully registered for " + selectedTraining.getTopic() + ".");
        loadAvailableTrainings(); // Refresh the table
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to set status (e.g., "Available", "Full") - requires a 'status' property in Training class
    // For now, this is a placeholder. You might need to modify the Training class.
    // public String getStatus() { return getMaxParticipants() > 0 ? "Available" : "Full"; }
}


