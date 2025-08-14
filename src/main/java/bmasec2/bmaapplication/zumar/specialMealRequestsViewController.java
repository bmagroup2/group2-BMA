package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.SpecialMealRequest;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class specialMealRequestsViewController {

    @FXML
    private TableView<SpecialMealRequest> activeSpecialRequestsTableView;
    @FXML
    private ComboBox<String> selectCadetComboBox;
    @FXML
    private TableColumn<SpecialMealRequest, String> cadetNameColn;
    @FXML
    private TextArea requestDetailsTextArea;
    @FXML
    private TableColumn<SpecialMealRequest, String> requestDetailsColn;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCadets();
        loadSpecialRequests();
    }

    private void setupTableColumns() {
        cadetNameColn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        requestDetailsColn.setCellValueFactory(new PropertyValueFactory<>("requestDetails"));
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

    private void loadSpecialRequests() {
        List<SpecialMealRequest> requests = DataPersistenceManager.loadObjects("special_meal_requests.dat");
        ObservableList<SpecialMealRequest> activeRequests = FXCollections.observableArrayList(
                requests.stream()
                        .filter(request -> request.getStatus().equals("Pending"))
                        .collect(Collectors.toList())
        );
        activeSpecialRequestsTableView.setItems(activeRequests);
    }

    @FXML
    public void saveRequestOnActionButton(ActionEvent actionEvent) {
        try {
            if (selectCadetComboBox.getValue() == null || selectCadetComboBox.getValue().equals("No cadets found")) {
                showAlert("Error", "Please select a cadet.");
                return;
            }
            if (requestDetailsTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter request details.");
                return;
            }

            String selectedCadet = selectCadetComboBox.getValue();
            String cadetId = selectedCadet.split(" - ")[0];
            String requestDetails = requestDetailsTextArea.getText().trim();

            SpecialMealRequest newRequest = new SpecialMealRequest(
                    UUID.randomUUID().toString(),
                    cadetId,
                    new Date(),
                    requestDetails,
                    "Pending"
            );

            DataPersistenceManager.addSpecialMealRequestAndSave(newRequest);
            showAlert("Success", "Special meal request saved successfully!");
            clearForm();
            loadSpecialRequests();

        } catch (Exception e) {
            showAlert("Error", "Failed to save request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        selectCadetComboBox.setValue(null);
        requestDetailsTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

