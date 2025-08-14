package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.ComboBoxTableCell;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class takeAttendanceviewController {

    @FXML
    private ComboBox<bmasec2.bmaapplication.fatema.TrainingSession> selectSessionComboBox;
    @FXML
    private TableView<Attendance> takeAttendanceTableView;
    @FXML
    private TableColumn<Attendance, String> cadetIdTableColumn;
    @FXML
    private TableColumn<Attendance, String> cadetNameTableColumn;
    @FXML
    private TableColumn<Attendance, String> statusTableColumn;

    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser";
    private static final String CADETS_FILE = "cadets.ser";
    private static final String ATTENDANCE_FILE = "attendance.ser";

    private ObservableList<bmasec2.bmaapplication.fatema.TrainingSession> trainingSessions;
    private ObservableList<Cadet> allCadets;
    private ObservableList<Attendance> currentSessionAttendance;

//    @FXML
//    public void initialize() {
//        // Initialize table columns
//        cadetIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
//        cadetNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
//
//        // Setup status column with ComboBox
//        ObservableList<String> attendanceStatuses = FXCollections.observableArrayList("Present", "Absent", "Late", "Excused");
//        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//        statusTableColumn.setCellFactory(ComboBoxTableCell.forTableColumn(attendanceStatuses));
//        statusTableColumn.setOnEditCommit(event -> {
//            event.getTableView().getItems().get(event.getTablePosition().getRow()).setStatus(event.getNewValue());
//        });
//        takeAttendanceTableView.setEditable(true);
//
//        loadInitialData();
//        populateSessionComboBox();
//    }
//
//    private void loadInitialData() {
//        trainingSessions = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(TRAINING_SESSIONS_FILE));
//        allCadets = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(CADETS_FILE));
//    }
//
//    private void populateSessionComboBox() {
//        selectSessionComboBox.setItems(trainingSessions);
//        selectSessionComboBox.setConverter(new javafx.util.StringConverter<bmasec2.bmaapplication.fatema.TrainingSession>() {
//            @Override
//            public String toString(bmasec2.bmaapplication.fatema.TrainingSession session) {
//                return session != null ? session.getTopic() + " (" + session.getDate() + ")" : "";
//            }
//
//            @Override
//            public bmasec2.bmaapplication.fatema.TrainingSession fromString(String string) {
//                return trainingSessions.stream()
//                        .filter(session -> (session.getTopic() + " (" + session.getDate() + ")").equals(string))
//                        .findFirst()
//                        .orElse(null);
//            }
//        });
//    }
//
//    @FXML
//    void loadCadetListButtonOnAction(ActionEvent event) {
//        bmasec2.bmaapplication.fatema.TrainingSession selectedSession = selectSessionComboBox.getSelectionModel().getSelectedItem();
//        if (selectedSession == null) {
//            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a training session.");
//            return;
//        }
//
//        // Filter cadets based on the selected session's batch
//        List<Cadet> filteredCadets = allCadets.stream()
//                .filter(cadet -> selectedSession.getCadetBatch().equals("All Cadets") || cadet.getBatch().equals(selectedSession.getCadetBatch()))
//                .collect(Collectors.toList());
//
//        // Create Attendance objects for each cadet for the current session
//        currentSessionAttendance = FXCollections.observableArrayList();
////        for (Cadet cadet : filteredCadets) {
////            // Check if attendance for this cadet and session already exists
////            List<Attendance> existingAttendance = DataPersistenceManager.loadObjects(ATTENDANCE_FILE);
////            Attendance existingRecord = existingAttendance.stream()
////                    .filter(a -> a.getCadetId().equals(cadet.getCadetId()) && a.getSessionId().equals(selectedSession.getSessionId()) && a.getDate().equals(LocalDate.now()))
////                    .findFirst()
////                    .orElse(null);
////
////            if (existingRecord != null) {
////                currentSessionAttendance.add(existingRecord);
////            } else {
////                currentSessionAttendance.add(new Attendance(
////                        UUID.randomUUID().toString(),
////                        cadet.getCadetId(),
////                        cadet.getName(),
////                        selectedSession.getSessionId(),
////                        selectedSession.getTopic(),
////                        LocalDate.now(),
////                        "Absent", // Default status
////                        ""
////                ));
////            }
//        }
////        takeAttendanceTableView.setItems(currentSessionAttendance);
//    }
//
//    @FXML
//    void saveAttendanceButtonOnAction(ActionEvent event) {
////        if (currentSessionAttendance == null || currentSessionAttendance.isEmpty()) {
////            showAlert(Alert.AlertType.WARNING, "No Data", "No attendance data to save. Please load a cadet list first.");
////            return;
////        }
//
//        List<Attendance> allAttendance = DataPersistenceManager.loadObjects(ATTENDANCE_FILE);
//        // Remove old records for the current session and date to avoid duplicates
//        TrainingSession selectedSession = selectSessionComboBox.getSelectionModel().getSelectedItem();
//        if (selectedSession != null) {
//            allAttendance.removeIf(a -> a.getSessionId().equals(selectedSession.getSessionId()) && a.getDate().equals(LocalDate.now()));
//        }
//
//        allAttendance.addAll(currentSessionAttendance);
//        DataPersistenceManager.saveObjects(allAttendance, ATTENDANCE_FILE);
//
//        showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance saved successfully!");
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
}

