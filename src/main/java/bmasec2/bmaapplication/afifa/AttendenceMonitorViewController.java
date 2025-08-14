package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AttendenceMonitorViewController {

    @FXML
    private ComboBox<String> attendencecadetcombobox;
    @FXML
    private DatePicker attendencedatepicker;
    @FXML
    private TableColumn<Attendance, String> attendencedatecolumn;
    @FXML
    private TableView<Attendance> attendencemonitortableview;
    @FXML
    private TableColumn<Attendance, String> attendenceceactivitycolumn;
    @FXML
    private TableColumn<Attendance, String> attendencestatuscolumn;
    @FXML
    private TableColumn<Attendance, String> attendencecadetnamecolumn;

    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        attendencedatecolumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        attendenceceactivitycolumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        attendencestatuscolumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        attendencecadetnamecolumn.setCellValueFactory(new PropertyValueFactory<>("cadetId")); // Assuming cadetId is used for name

        // Load cadets for the combobox (placeholder for actual cadet loading)
        // In a real application, you would load actual cadet names/IDs here
        List<Cadet> cadets = DataPersistenceManager.loadObjects("cadets.dat");
        ObservableList<String> cadetNames = FXCollections.observableArrayList();
        for (Cadet cadet : cadets) {
            cadetNames.add(cadet.getName());
        }
        attendencecadetcombobox.setItems(cadetNames);

        loadAttendanceRecords();
    }

    @FXML
    public void fetchrecordsonaction(ActionEvent actionEvent) {
        loadAttendanceRecords();
    }

    private void loadAttendanceRecords() {
        List<Attendance> allAttendance = DataPersistenceManager.loadObjects("attendance.dat");

        String selectedCadetName = attendencecadetcombobox.getValue();
        LocalDate selectedDate = attendencedatepicker.getValue();

        List<Attendance> filteredAttendance = allAttendance.stream()
                .filter(attendance -> {
                    boolean matchesCadet = (selectedCadetName == null || selectedCadetName.isEmpty()) || attendance.getCadetId().equals(selectedCadetName); // Assuming cadetId is name
                    boolean matchesDate = (selectedDate == null) || attendance.getDate().equals(selectedDate);
                    return matchesCadet && matchesDate;
                })
                .collect(Collectors.toList());

        attendanceList.setAll(filteredAttendance);
        attendencemonitortableview.setItems(attendanceList);
    }
}


