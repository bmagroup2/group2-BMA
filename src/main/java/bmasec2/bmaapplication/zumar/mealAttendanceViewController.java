package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class mealAttendanceViewController {

    @FXML
    private TableColumn<Attendance, String> statusColn;
    @FXML
    private TableColumn<Attendance, String> cadetIdColn;
    @FXML
    private ComboBox<String> selectBatchComboBox;
    @FXML
    private TableColumn<Attendance, String> cadetNameColn;
    @FXML
    private DatePicker selectDateMealAttendanceDatePicker;
    @FXML
    private TableView<Attendance> mealAttendanceTableView;
    @FXML
    private ComboBox<String> selectMealComboBox;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCadetBatches();
        populateMealTypes();
        selectDateMealAttendanceDatePicker.setValue(LocalDate.now());
    }

    private void setupTableColumns() {
        cadetIdColn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));

        cadetNameColn.setCellValueFactory(cellData -> {
            String cadetId = cellData.getValue().getCadetId();
            List<bmasec2.bmaapplication.User> users = DataPersistenceManager.loadObjects("users.dat");
            bmasec2.bmaapplication.User user = users.stream()
                    .filter(u -> u instanceof Cadet && u.getUserId().equals(cadetId))
                    .findFirst()
                    .orElse(null);
            return new javafx.beans.property.SimpleStringProperty(user instanceof Cadet ? user.getName() : "Unknown");
        });
        statusColn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadCadetBatches() {
        List<bmasec2.bmaapplication.User> users = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> batches = FXCollections.observableArrayList();
        users.stream()
                .filter(user -> user instanceof Cadet)
                .map(user -> ((Cadet) user).getBatch())
                .distinct()
                .sorted()
                .forEach(batches::add);
        selectBatchComboBox.setItems(batches);
        if (!batches.isEmpty()) {
            selectBatchComboBox.setValue(batches.get(0));
        }
    }

    private void populateMealTypes() {
        ObservableList<String> mealTypes = FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner");
        selectMealComboBox.setItems(mealTypes);
        selectMealComboBox.setValue("Lunch");
    }

    @FXML
    public void viewAttendanceOnActionButton(ActionEvent actionEvent) {
        try {
            if (selectBatchComboBox.getValue() == null || selectBatchComboBox.getValue().isEmpty()) {
                showAlert("Error", "Please select a cadet batch.");
                return;
            }
            if (selectDateMealAttendanceDatePicker.getValue() == null) {
                showAlert("Error", "Please select a date.");
                return;
            }
            if (selectMealComboBox.getValue() == null || selectMealComboBox.getValue().isEmpty()) {
                showAlert("Error", "Please select a meal type.");
                return;
            }

            String selectedBatch = selectBatchComboBox.getValue();
            LocalDate localDate = selectDateMealAttendanceDatePicker.getValue();
            Date attendanceDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String mealType = selectMealComboBox.getValue();

            List<Attendance> allAttendances = DataPersistenceManager.loadObjects("attendance.dat");
            List<bmasec2.bmaapplication.User> allUsers = DataPersistenceManager.loadObjects("users.dat");
            List<Cadet> cadetsInBatch = allUsers.stream()
                    .filter(user -> user instanceof Cadet && ((Cadet) user).getBatch().equals(selectedBatch))
                    .map(user -> (Cadet) user)
                    .collect(Collectors.toList());

            ObservableList<Attendance> mealAttendances = FXCollections.observableArrayList();

            for (Cadet cadet : cadetsInBatch) {

                Attendance existingAttendance = allAttendances.stream()
                        .filter(att -> att.getCadetId().equals(cadet.getUserId()) &&
                                att.getDate().equals(attendanceDate) &&
                                att.getSessionId().equals(mealType))
                        .findFirst()
                        .orElse(null);

                if (existingAttendance != null) {
                    mealAttendances.add(existingAttendance);
                } else {

                    mealAttendances.add(new Attendance("N/A", cadet.getUserId(), attendanceDate, "Absent", mealType));
                }
            }

            if (mealAttendances.isEmpty()) {
                showAlert("Info", "No attendance records found for this batch, date, and meal type.");
            }
            mealAttendanceTableView.setItems(mealAttendances);

        } catch (Exception e) {
            showAlert("Error", "Failed to load meal attendance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}