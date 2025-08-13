package bmasec2.bmaapplication.shanin;


import bmasec2.bmaapplication.afifa.CadetSupervisor;
import bmasec2.bmaapplication.fatema.LogisticOfficer;
import bmasec2.bmaapplication.fatema.TrainingInstructor;
import bmasec2.bmaapplication.model.AuditLog;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.afifa.Cadet;

import bmasec2.bmaapplication.zumar.MedicalOfficer;
import bmasec2.bmaapplication.zumar.MessOfficer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static bmasec2.bmaapplication.LoginViewController.*;

public class UserManagementViewController {

    @FXML private ComboBox<String> filterByRoleComboBox;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableColumn<User, String> userIdColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TextField userIdTextField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private TextField fullNameTextField;
    @FXML private TextField searchByNameTextfield;
    @FXML private TableView<User> userListTableView;
    @FXML private TableColumn<User, String> emailColumn;

    private String currentUserId;

    private ObservableList<User> masterUserList;
    private static final String USERS_FILE = "users.bin";
    private static final String AUDIT_LOGS_FILE = "auditlogs.bin";

    @FXML
    private TextField passwordTextField;
    @FXML
    private Pane userAddPane;

    @FXML
    public void initialize() {

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        roleComboBox.getItems().addAll("System Admin", "Commandant", "Cadet", "Cadet Supervisor", "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer");
        filterByRoleComboBox.getItems().addAll("All", "System Admin", "Commandant", "Cadet", "Cadet Supervisor", "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer");
        filterByRoleComboBox.setValue("All");
        statusComboBox.getItems().addAll("Active", "Inactive");

        loadUsers();


        userListTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdTextField.setText(newSelection.getUserId());
                fullNameTextField.setText(newSelection.getName());
                emailTextField.setText(newSelection.getEmail());
                roleComboBox.setValue(newSelection.getRole());
                statusComboBox.setValue(newSelection.getStatus());
                passwordTextField.setText("");
            }
        });


        searchByNameTextfield.textProperty().addListener((obs, oldVal, newVal) -> filterUsers());
        filterByRoleComboBox.setOnAction(event -> filterUsers());

        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        Optional<User> authenticatedUser = allUsers.stream()
                .filter(user -> (user.getName().equalsIgnoreCase(enteredUsername) ||
                        user.getEmail().equalsIgnoreCase(enteredUsername) ||
                        user.getUserId().equalsIgnoreCase(enteredUsername)) &&
                        user.getPassword().equals(enteredPassword) &&
                        user.getRole().equals(selectedRole))
                .findFirst();

        if (authenticatedUser.isPresent()) {
            User loggedInUser = authenticatedUser.get();
            loggedInUser.login(enteredUsername, enteredPassword, selectedRole);
            currentUserId = loggedInUser.getUserId();
        }
    }



    private void loadUsers() {
        List<User> users = DataPersistenceManager.loadObjects(USERS_FILE);
        masterUserList = FXCollections.observableArrayList(users);
        userListTableView.setItems(masterUserList);
        filterUsers();
    }

    private void saveUsers() {
        DataPersistenceManager.saveObjects(new ArrayList<>(masterUserList), USERS_FILE);
    }

    private void filterUsers() {
        String searchText = searchByNameTextfield.getText().toLowerCase();
        String selectedRole = filterByRoleComboBox.getValue();

        List<User> filteredList = masterUserList.stream()
                .filter(user -> user.getName().toLowerCase().contains(searchText) || user.getUserId().toLowerCase().contains(searchText))
                .filter(user -> selectedRole == null || selectedRole.equals("All") || user.getRole().equals(selectedRole))
                .collect(Collectors.toList());

        userListTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void crossBtnOnMouseClicked(Event event) {
        clearFormFields();
    }

    @FXML
    public void deleteSelectedBtnOnAction(ActionEvent actionEvent) {
        User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + selectedUser.getName() + "?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                logAction("USER_DELETED",
                        String.format("Updated %s: %s (%s)", selectedUser.getRole(), selectedUser.getUserId(), selectedUser.getName()));
                masterUserList.remove(selectedUser);


                saveUsers();
                clearFormFields();
                filterUsers();
            }
        } else {
            showAlert(AlertType.WARNING, "No User Selected", "Please select a user to delete.");
        }
    }

    @FXML
    public void addNewUserBtnOnAction(ActionEvent actionEvent) {
        clearFormFields();
        userAddPane.setVisible(true);
        userIdTextField.setEditable(true);
    }

    @FXML
    public void submitBtnOnAction(ActionEvent actionEvent) {
        String userId = userIdTextField.getText();
        String name = fullNameTextField.getText();
        String email = emailTextField.getText();
        String role = roleComboBox.getValue();
        String password = passwordTextField.getText();
        String status = statusComboBox.getValue();

        if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || role == null || password.isEmpty() || status == null) {
            showAlert(AlertType.ERROR, "Missing Information", "Please fill in all fields.");
            return;
        }

        boolean isNewUser = masterUserList.stream().noneMatch(u -> u.getUserId().equals(userId));

        if (isNewUser) {

            User newUser = createUserByRole(userId, name, email, password, role);
            if (newUser != null) {

                masterUserList.add(newUser);
                logAction("USER_CREATED",
                        String.format("Created %s: %s (%s)", role, userId, name));
                showAlert(AlertType.INFORMATION, "User Added", "New user " + name + " added successfully.");
            } else {
                showAlert(AlertType.ERROR, "Error", "Could not create user for role: " + role);
            }
        } else {
            User existingUser = masterUserList.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
            if (existingUser != null) {
                existingUser.setName(name);
                existingUser.setEmail(email);
                existingUser.setRole(role);
                existingUser.setPassword(password);
                existingUser.setStatus(status);
                logAction("USER_UPDATED",
                        String.format("Updated %s: %s (%s)", role, userId, name));
                showAlert(AlertType.INFORMATION, "User Updated", "User " + name + " updated successfully.");

            }
        }
        saveUsers();
        clearFormFields();
        userAddPane.setVisible(false);
        filterUsers();
    }

    private User createUserByRole(String userId, String name, String email, String password, String role) {
        switch (role) {
            case "System Admin":
                return new SystemAdministrator(userId, name, email, password, 1);
            case "Commandant":
                return new Commandant(userId, name, email, password, userId, "N/A");
            case "Cadet":
                return new Cadet(userId, name, email, password, "N/A", "N/A");
            case "Cadet Supervisor":
                return new CadetSupervisor(userId, name, email, password, userId, "General Department");
            case "Training Instructor":
                return new TrainingInstructor(userId, name, email, password, userId, "Physical Training");
            case "Logistic Officer":
                return new LogisticOfficer(userId, name, email, password, userId, "0123456789");
            case "Medical Officer":
                return new MedicalOfficer(userId, name, email, password, userId, "MED-LIC-001");
            case "Mess Officer":
                return new MessOfficer(userId, name, email, password, userId, "Day Shift");
            default:
                return null;
        }
    }



    private void logAction(String action, String details) {

        String logId = "LOG-" + System.currentTimeMillis();
        AuditLog log = new AuditLog(logId, currentUserId, action, details);
        List<AuditLog> existingLogs = DataPersistenceManager.loadObjects(AUDIT_LOGS_FILE);
        existingLogs.add(log);
        DataPersistenceManager.saveObjects(existingLogs, AUDIT_LOGS_FILE);

 }

    private void clearFormFields() {
        userIdTextField.setText("");
        fullNameTextField.setText("");
        emailTextField.setText("");
        roleComboBox.getSelectionModel().clearSelection();
        passwordTextField.setText("");
        statusComboBox.getSelectionModel().clearSelection();
        userIdTextField.setEditable(false);
        userListTableView.getSelectionModel().clearSelection();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void filterBtnOnAction(ActionEvent actionEvent) {
        filterUsers();
    }
}
