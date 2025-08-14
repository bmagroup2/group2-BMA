package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.util.stream.Collectors;

public class MentorAssignmentViewController {
    @FXML
    private ListView<Cadet> mentorslistview;
    @FXML
    private ListView<Cadet> mentornewcadetlistview;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        loadCadetsWithoutMentors();
        loadPotentialMentors();
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
        loadCadetsWithoutMentors();
        loadPotentialMentors();
    }

    private void loadCadetsWithoutMentors() {
//        List<Cadet> allCadets = DataPersistenceManager.loadObjects("cadets.dat");
//        // Assuming a cadet without a mentor has a specific status or a null mentor field
//        // For simplicity, let's assume a new cadet has a default status or a flag indicating no mentor
//        // In a real system, you'd have a 'mentorId' field in Cadet, and filter where it's null
//        ObservableList<Cadet> newCadets = FXCollections.observableArrayList(allCadets.stream()
//////                .filter(cadet -> cadet.getMedicalStatus().equals("Fit")) // Placeholder: assuming 'Fit' means new and no mentor assigned yet
////                .collect(Collectors.toList()));
//        mentornewcadetlistview.setItems(newCadets);
    }

    private void loadPotentialMentors() {
        List<Cadet> allCadets = DataPersistenceManager.loadObjects("cadets.dat");
        // Potential mentors could be senior cadets or even other supervisors
        // For simplicity, let's allow all existing cadets to be potential mentors
        ObservableList<Cadet> potentialMentors = FXCollections.observableArrayList(allCadets);
        mentorslistview.setItems(potentialMentors);
    }

    @FXML
    public void mentorassignonaction(ActionEvent actionEvent) {
        Cadet selectedNewCadet = mentornewcadetlistview.getSelectionModel().getSelectedItem();
        Cadet selectedMentor = mentorslistview.getSelectionModel().getSelectedItem();

        if (selectedNewCadet == null || selectedMentor == null) {
            showAlert(AlertType.ERROR, "Selection Error", "Please select both a new cadet and a mentor.");
            return;
        }

        if (selectedNewCadet.getUserId().equals(selectedMentor.getUserId())) {
            showAlert(AlertType.ERROR, "Assignment Error", "A cadet cannot be their own mentor.");
            return;
        }

        // Perform the assignment (update the cadet object with mentor information)
        // This is a placeholder. In a real system, you'd have a dedicated field for mentor ID in Cadet class.
        // For now, let's just simulate the assignment.
//        selectedNewCadet.setMedicalStatus("Mentored by " + selectedMentor.getName()); // Placeholder for actual mentor assignment

        // Save updated cadet data
        List<Cadet> allCadets = DataPersistenceManager.loadObjects("cadets.dat");
        for (int i = 0; i < allCadets.size(); i++) {
            if (allCadets.get(i).getUserId().equals(selectedNewCadet.getUserId())) {
                allCadets.set(i, selectedNewCadet);
                break;
            }
        }
        DataPersistenceManager.saveObjects(allCadets, "cadets.dat");

        showAlert(AlertType.INFORMATION, "Success", selectedMentor.getName() + " has been assigned as mentor to " + selectedNewCadet.getName() + ".");
        loadCadetsWithoutMentors(); // Refresh the list of new cadets
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


