package bmasec2.bmaapplication;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.afifa.CadetSupervisor;
import bmasec2.bmaapplication.fatema.LogisticOfficer;
import bmasec2.bmaapplication.fatema.TrainingInstructor;
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
import java.io.Serializable;
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

    private static final String USERS_FILE = "users.dat";
    private static final String APP_TITLE = "BMA Application";
    private List<User> allUsers;

    @FXML
    public void initialize() {
        setupRoleComboBox();
        loadUserData();
    }

    private void setupRoleComboBox() {
        roleComboBox.getItems().addAll(
                "System Admin", "Commandant", "Cadet", "Cadet Supervisor",
                "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer"
        );
    }

    private void loadUserData() {
        try {
            allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
            if (allUsers == null || allUsers.isEmpty()) {
                allUsers = createDefaultUsers();
                DataPersistenceManager.saveObjects(allUsers, USERS_FILE);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                    "Failed to initialize user data: " + e.getMessage());
            allUsers = new ArrayList<>();
        }
    }

    private List<User> createDefaultUsers() {
        List<User> defaultUsers = new ArrayList<>();
        defaultUsers.add(new SystemAdministrator(
                "admin", "Admin", "admin@gmail.com", "admin1234", 2));
        defaultUsers.add(new Commandant(
                "commandant", "Cmdt User", "cmd@bma.com", "commandant1234", "CMD-001", "01987654321"));
        defaultUsers.add(new Cadet(
                "cadet", "Cadet", "cadet@bma.com", "cadet1234", "Batch A", "Junior"));
        defaultUsers.add(new CadetSupervisor(
                "cadetsupervisor", "Cadet Supervisor", "cadetsupervisor@bma.com", "cadetsupervisor", "CSV-001", "N/A"));
        defaultUsers.add(new LogisticOfficer(
                "logisticofficer", "Logistic Officer", "logisticofficer@bma.com", "logisticofficer1234", "LOF-001", "0192231211221"));
        defaultUsers.add(new TrainingInstructor(
                "trofficer", "Training Officer", "trofficer@bma.com", "trofficer1234", "TRO-001", "N/A"));
        defaultUsers.add(new MedicalOfficer(
                "medicalofficer", "Medical Officer", "medicalofficer@bma.com", "medicalofficer1234", "MOF-001", "BMD-0012"));
        defaultUsers.add(new MessOfficer(
                "messofficer", "Mess Officer", "messofficer@bma.com", "messofficer1234", "MEO-001", "Day"));
        return defaultUsers;
    }

    @FXML
    public void signInBtnOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            Optional<User> authenticatedUser = authenticateUser(allUsers);

            if (authenticatedUser.isPresent()) {
                handleSuccessfulLogin(actionEvent, authenticatedUser.get());
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

    private Optional<User> authenticateUser(List<User> users) {
        return users.stream()
                .filter(user -> (user.getName().equalsIgnoreCase(enteredUsername) ||
                        user.getEmail().equalsIgnoreCase(enteredUsername) ||
                        user.getUserId().equalsIgnoreCase(enteredUsername)))
                .filter(user -> user.getPassword().equals(enteredPassword))
                .filter(user -> user.getRole().equalsIgnoreCase(selectedRole))
                .findFirst();
    }

    private void handleSuccessfulLogin(ActionEvent actionEvent, User loggedInUser) throws IOException {
        loggedInUser.login(enteredUsername, enteredPassword, selectedRole);
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