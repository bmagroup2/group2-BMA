package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.MedicalRecord;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class cadetHealthHistoryViewController {

    @FXML
    private TableColumn<MedicalRecord, String> detailsColn;
    @FXML
    private TableView<MedicalRecord> cadetHealthHistoryTableView;
    @FXML
    private ComboBox<String> selectCadetComboBox;
    @FXML
    private TableColumn<MedicalRecord, String> recordTypeColn;
    @FXML
    private TableColumn<MedicalRecord, String> dateColn;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCadets();
    }

    private void setupTableColumns() {
        dateColn.setCellValueFactory(cellData -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return new javafx.beans.property.SimpleStringProperty(dateFormat.format(cellData.getValue().getDate()));
        });
        recordTypeColn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        detailsColn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
    }

    private void loadCadets() {
        List<bmasec2.bmaapplication.User> users = DataPersistenceManager.loadObjects("users.dat");
        ObservableList<String> cadetOptions = FXCollections.observableArrayList();

        for (bmasec2.bmaapplication.User user : users) {
            if (user instanceof Cadet) {
                Cadet cadet = (Cadet) user;
                cadetOptions.add(cadet.getUserId() + " - " + cadet.getName());
            }
        }

        if (cadetOptions.isEmpty()) {
            cadetOptions.add("No cadets found");
        }

        selectCadetComboBox.setItems(cadetOptions);
    }

    @FXML
    public void viewHistoryOnActionButton(ActionEvent actionEvent) {
        try {
            if (selectCadetComboBox.getValue() == null || selectCadetComboBox.getValue().equals("No cadets found")) {
                showAlert("Error", "Please select a cadet.");
                return;
            }

            String selectedCadet = selectCadetComboBox.getValue();
            String cadetId = selectedCadet.split(" - ")[0];

            List<MedicalRecord> medicalRecords = DataPersistenceManager.loadObjects("medical_records.dat");
            List<MedicalRecord> cadetHistory = medicalRecords.stream()
                    .filter(record -> record.getCadetId().equals(cadetId))
                    .collect(Collectors.toList());

            if (cadetHistory.isEmpty()) {
                showAlert("Info", "No medical history found for this cadet.");
            }
            cadetHealthHistoryTableView.setItems(FXCollections.observableArrayList(cadetHistory));

        } catch (Exception e) {
            showAlert("Error", "Failed to load medical history: " + e.getMessage());
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

