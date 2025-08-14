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
        registerslotscolumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        registerstatuscolumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAvailableTrainings();
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
        loadAvailableTrainings();
    }

    private void loadAvailableTrainings() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");

        availableTrainings.setAll(allTrainings.stream()
                .filter(training -> training.getMaxParticipants() > 0)
                .filter(training -> training.getDateTime().after(new java.util.Date()))
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


        selectedTraining.setMaxParticipants(selectedTraining.getMaxParticipants() - 1);


        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        for (int i = 0; i < allTrainings.size(); i++) {
            if (allTrainings.get(i).getSessionId().equals(selectedTraining.getSessionId())) {
                allTrainings.set(i, selectedTraining);
                break;
            }
        }
        DataPersistenceManager.saveObjects(allTrainings, "trainings.dat");

        showAlert(AlertType.INFORMATION, "Registration Successful", "You have successfully registered for " + selectedTraining.getTopic() + ".");
        loadAvailableTrainings();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


