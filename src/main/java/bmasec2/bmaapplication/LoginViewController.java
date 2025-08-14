package bmasec2.bmaapplication;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.afifa.CadetSupervisor;
import bmasec2.bmaapplication.fatema.LogisticOfficer;
import bmasec2.bmaapplication.shanin.Commandant;
import bmasec2.bmaapplication.shanin.SystemAdministrator;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.zumar.MedicalOfficer;
import bmasec2.bmaapplication.zumar.MessOfficer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginViewController {

    @FXML private PasswordField passwordTextField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField usernameTextField;

    public static String enteredUsername;
    public static String enteredPassword;
    public static String selectedRole;

    private static final String USERS_FILE = "users.bin";
    private static final String APP_TITLE = "BMA Application";
    List<User> allUsers;
    @FXML
    public void initialize() {
        allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        System.out.println(allUsers);
        setupRoleComboBox();
        initializeDefaultUsers();
    }

    private void setupRoleComboBox() {
        roleComboBox.getItems().addAll(
                "System Admin", "Commandant", "Cadet", "Cadet Supervisor",
                "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer"
        );
    }

    private void initializeDefaultUsers() {
        try {

            List<User> users = DataPersistenceManager.loadObjects(USERS_FILE);
            if (users == null) {
                users = new ArrayList<>();
            }

            if (users.isEmpty()) {
                createDefaultUsers(users);
                DataPersistenceManager.saveObjects(users, USERS_FILE);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                    "Failed to initialize user data: " + e.getMessage());
        }
    }

    private void createDefaultUsers(List<User> users) {
        users.add(new SystemAdministrator(
                "admin", "Admin", "admin@gmail.com", "admin1234", 2));
        users.add(new Commandant(
                "commandant", "Cmdt User", "cmd@bma.com", "commandant1234", "CMD-001", "01987654321"));
        users.add(new Cadet(
                "cadet", "Cadet", "cadet@bma.com", "cadet1234", "Batch A", "Junior"));
        users.add(new CadetSupervisor(
                "cadetsupervisor", "Cadet Supervisor", "cadetsupervisor@bma.com", "cadetsupervisor", "CSV-001","N/A"));
        users.add(new LogisticOfficer(
                "logisticofficer", "Logistic Officer", "logisticofficer@bma.com", "logisticofficer1234", "LOF-001","0192231211221" ));
        users.add(new bmasec2.bmaapplication.fatema.TrainingInstructor(
                "trofficer", "Training Officer", "trofficer@bma.com", "trofficer1234", "TRO-001", "N/A"));
        users.add(new MedicalOfficer("medicalofficer","Medical Officer","medicalofficer@bma.com","medicalofficer1234","MOF-001","BMD-0012"));
        users.add(new MessOfficer(
                "messofficer","Mess Officer","messofficer@bma.com","messofficer1234","MEO-001","Day"));
    }

    @FXML
    public void signInBtnOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {

            if (allUsers == null) {
                allUsers = new ArrayList<>();
            }

            Optional<User> authenticatedUser = authenticateUser(allUsers);

            if (authenticatedUser.isPresent()) {
                handleSuccessfulLogin(actionEvent, authenticatedUser.get(), allUsers);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed",
                        "Invalid username, password, or role.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login Error",
                    "An error occurred during login: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        enteredUsername = usernameTextField.getText().trim();
        enteredPassword = passwordTextField.getText();
        selectedRole = roleComboBox.getValue();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Information",
                    "Please fill in all fields.");
            return false;
        }
        return true;
    }

    private Optional<User> authenticateUser(List<User> allUsers) {
        return allUsers.stream()
                .filter(user -> (user.getName().equalsIgnoreCase(enteredUsername) ||
                        user.getEmail().equalsIgnoreCase(enteredUsername) ||
                        user.getUserId().equalsIgnoreCase(enteredUsername)) &&
                        user.getPassword().equals(enteredPassword) &&
                        user.getRole().equalsIgnoreCase(selectedRole))
                .findFirst();
    }

    private void handleSuccessfulLogin(ActionEvent actionEvent, User loggedInUser, List<User> allUsers) throws IOException {
        loggedInUser.login(enteredUsername, enteredPassword, selectedRole);
        DataPersistenceManager.saveObjects(allUsers, USERS_FILE);
        openMainMenu(actionEvent, loggedInUser);
    }

    private void openMainMenu(ActionEvent actionEvent, User loggedInUser) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage loginStage = (Stage) source.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("nav-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        NavMenuViewController controller = fxmlLoader.getController();
        controller.initData(loggedInUser);

        Stage newStage = new Stage();
        newStage.setTitle(APP_TITLE);
        newStage.setScene(scene);
        newStage.show();

        loginStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}