package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CadetManagementViewController {

    @FXML private TableColumn<Cadet, String> batchColumn;
    @FXML private TableColumn<Cadet, String> cadetIdColumn;
    @FXML private TextField batchTextField;
    @FXML private TableView<Cadet> cadetListTableView;
    @FXML private TableColumn<Cadet, String> fullNameColumn;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableColumn<Cadet, String> statusColumn;
    @FXML private TextField cadetIdTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField fullNameTextField;
    @FXML private TextField searchByNameOrIdTextField;
    @FXML private TableColumn<Cadet, String> emailColumn;
    @FXML private TextField passwordTextField;
    @FXML private TextField rankTextField;
    @FXML private TableColumn<Cadet, String> rankColumn;

    private ObservableList<Cadet> masterCadetList;
    private static final String USERS_FILE = "users.dat";
    @FXML
    private Pane cadetPane;

    @FXML
    public void initialize() {

        cadetIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        batchColumn.setCellValueFactory(new PropertyValueFactory<>("batch"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        statusComboBox.getItems().addAll("Active", "Inactive");

        loadCadets();


        cadetListTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cadetIdTextField.setText(newSelection.getUserId());
                fullNameTextField.setText(newSelection.getName());
                emailTextField.setText(newSelection.getEmail());
                batchTextField.setText(newSelection.getBatch());
                rankTextField.setText(newSelection.getRank());
                statusComboBox.setValue(newSelection.getStatus());
                passwordTextField.setText("");
                cadetIdTextField.setEditable(false);
            } else {
                clearFormFields();
            }
        });


        searchByNameOrIdTextField.textProperty().addListener((obs, oldVal, newVal) -> filterCadets());
    }

    private void loadCadets() {
        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        masterCadetList = FXCollections.observableArrayList(
                allUsers.stream()
                        .filter(user -> user instanceof Cadet)
                        .map(user -> (Cadet) user)
                        .collect(Collectors.toList())
        );
        cadetListTableView.setItems(masterCadetList);
        filterCadets();
    }

    private void saveCadets() {

        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        List<User> nonCadetUsers = allUsers.stream()
                .filter(user -> !(user instanceof Cadet))
                .collect(Collectors.toList());

        nonCadetUsers.addAll(masterCadetList);
        DataPersistenceManager.saveObjects(nonCadetUsers, USERS_FILE);
    }

    private void filterCadets() {
        String searchText = searchByNameOrIdTextField.getText().toLowerCase();

        List<Cadet> filteredList = masterCadetList.stream()
                .filter(cadet -> cadet.getName().toLowerCase().contains(searchText) || cadet.getUserId().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        cadetListTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void crossBtnOnMouseClicked(Event event) {
        clearFormFields();
        cadetPane.setVisible(false);
    }

    @FXML
    public void deleteSelectedBtnOnAction(ActionEvent actionEvent) {
        Cadet selectedCadet = cadetListTableView.getSelectionModel().getSelectedItem();
        if (selectedCadet != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + selectedCadet.getName() + "?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                masterCadetList.remove(selectedCadet);
                saveCadets();
                clearFormFields();
                filterCadets();
                showAlert(AlertType.INFORMATION, "Cadet Deleted", "Cadet " + selectedCadet.getName() + " deleted successfully.");
            }
        } else {
            showAlert(AlertType.WARNING, "No Cadet Selected", "Please select a cadet to delete.");
        }
    }

    @FXML
    public void addNewCadetBtnOnAction(ActionEvent actionEvent) {
        clearFormFields();
        cadetPane.setVisible(true);
    }

    @FXML
    public void searchBtnOnAction(ActionEvent actionEvent) {
        filterCadets();
    }

    @FXML
    public void submitBtnOnAction(ActionEvent actionEvent) {
        String cadetId = cadetIdTextField.getText();
        String name = fullNameTextField.getText();
        String email = emailTextField.getText();
        String batch = batchTextField.getText();
        String rank = rankTextField.getText();
        String status = statusComboBox.getValue();
        String password = passwordTextField.getText();

        if (cadetId.isEmpty() || name.isEmpty() || email.isEmpty() || batch.isEmpty() || rank.isEmpty() || status == null) {
            showAlert(AlertType.ERROR, "Missing Information", "Please fill in all fields.");
            return;
        }

        boolean isNewCadet = masterCadetList.stream().noneMatch(c -> c.getUserId().equals(cadetId));

        if (isNewCadet) {
            if (password.isEmpty()) {
                showAlert(AlertType.ERROR, "Missing Information", "Password is required for new cadets.");
                return;
            }

            Cadet newCadet = new Cadet(cadetId, name, email, password, batch, rank);
            newCadet.setStatus(status);
            masterCadetList.add(newCadet);
            showAlert(AlertType.INFORMATION, "Cadet Added", "New cadet " + name + " added successfully.");
        } else {

            Cadet existingCadet = masterCadetList.stream().filter(c -> c.getUserId().equals(cadetId)).findFirst().orElse(null);
            if (existingCadet != null) {
                existingCadet.setName(name);
                existingCadet.setEmail(email);
                existingCadet.setBatch(batch);
                existingCadet.setRank(rank);
                existingCadet.setStatus(status);
                if (!password.isEmpty()) {
                    existingCadet.setPassword(password);
                }
                showAlert(AlertType.INFORMATION, "Cadet Updated", "Cadet " + name + " updated successfully.");
            }
        }
        saveCadets();
        clearFormFields();
        filterCadets();
        cadetPane.setVisible(false);
    }

    private void clearFormFields() {
        cadetIdTextField.setText("");
        fullNameTextField.setText("");
        emailTextField.setText("");
        batchTextField.setText("");
        rankTextField.setText("");
        statusComboBox.getSelectionModel().clearSelection();
        passwordTextField.setText("");
        cadetIdTextField.setEditable(true);
        cadetListTableView.getSelectionModel().clearSelection();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

