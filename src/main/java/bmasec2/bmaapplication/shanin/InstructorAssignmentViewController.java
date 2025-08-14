package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.Instructor;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.afifa.Cadet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstructorAssignmentViewController {

    @FXML private ListView<Instructor> availableInstructorListView;
    @FXML private ListView<String> cadetBatchesListView;

    private ObservableList<Instructor> instructors;
    private ObservableList<String> batches;

    private static final String INSTRUCTORS_FILE = "instructors.dat";
    private static final String USERS_FILE = "users.dat";
    private static final String ASSIGNMENTS_FILE = "assignments.dat";

    @FXML
    public void initialize() {
        availableInstructorListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cadetBatchesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        loadInstructors();
        loadCadetBatches();

        
        if (instructors.isEmpty()) {
            instructors.addAll(
                    new Instructor("INS-001", "Mr. Smith", "Drill"),
                    new Instructor("INS-002", "Ms. Johnson", "Academics"),
                    new Instructor("INS-003", "Dr. Lee", "Medical Training")
            );
            DataPersistenceManager.saveObjects(instructors.stream().collect(Collectors.toList()), INSTRUCTORS_FILE);
        }
        if (batches.isEmpty()) {
            batches.addAll("Batch A", "Batch B", "Batch C");

        }
    }

    private void loadInstructors() {
        List<Instructor> loadedInstructors = DataPersistenceManager.loadObjects(INSTRUCTORS_FILE);
        instructors = FXCollections.observableArrayList(loadedInstructors);
        availableInstructorListView.setItems(instructors);
    }

    private void loadCadetBatches() {
        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        batches = FXCollections.observableArrayList(
                allUsers.stream()
                        .filter(user -> user instanceof Cadet)
                        .map(user -> ((Cadet) user).getBatch())
                        .distinct()
                        .collect(Collectors.toList())
        );
        cadetBatchesListView.setItems(batches);
    }

    @FXML
    public void assignBtnOnAction(ActionEvent actionEvent) {
        Instructor selectedInstructor = availableInstructorListView.getSelectionModel().getSelectedItem();
        String selectedBatch = cadetBatchesListView.getSelectionModel().getSelectedItem();

        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.WARNING, "No Instructor Selected", "Please select an instructor to assign.");
            return;
        }
        if (selectedBatch == null) {
            showAlert(Alert.AlertType.WARNING, "No Batch Selected", "Please select a cadet batch to assign.");
            return;
        }


        List<String> assignments = DataPersistenceManager.loadObjects(ASSIGNMENTS_FILE);
        String assignmentRecord = selectedInstructor.getName() + " (" + selectedInstructor.getInstructorId() + ") assigned to " + selectedBatch;

        if (assignments.contains(assignmentRecord)) {
            showAlert(Alert.AlertType.INFORMATION, "Already Assigned", assignmentRecord + " is already assigned.");
        } else {
            assignments.add(assignmentRecord);
            DataPersistenceManager.saveObjects(assignments, ASSIGNMENTS_FILE);
            showAlert(Alert.AlertType.INFORMATION, "Assignment Successful", assignmentRecord + " successfully.");
        }


        availableInstructorListView.getSelectionModel().clearSelection();
        cadetBatchesListView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}