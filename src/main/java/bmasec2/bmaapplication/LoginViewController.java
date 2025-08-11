package bmasec2.bmaapplication;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoginViewController {

    @javafx.fxml.FXML
    private PasswordField passwordTextField;
    @javafx.fxml.FXML
    private ComboBox<String> roleComboBox;
    @javafx.fxml.FXML
    private TextField usernameTextField;

    public static String enteredUsername ;
    public static String enteredPassword;
    public static String selectedRole;

    private static final String USERS_FILE = "users.bin";

    @javafx.fxml.FXML
    public void initialize() {
        roleComboBox.getItems().addAll("System Admin", "Commandant", "Cadet", "Cadet Supervisor", "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer");


        List<User> users = DataPersistenceManager.loadObjects(USERS_FILE);
        if (users.isEmpty()) {
            users.add(new bmasec2.bmaapplication.shanin.SystemAdministrator("admin", "Admin", "admin@gmail.com", "admin1234",2));
            users.add(new bmasec2.bmaapplication.shanin.Commandant("CMD-001", "Cmdt User", "cmd@bma.com", "commandant1234", "CMD-001", "0987654321"));
            users.add(new bmasec2.bmaapplication.afifa.Cadet("C-001", "Cadet John", "john@bma.com", "cadet1234", "Batch A", "Junior"));

            DataPersistenceManager.saveObjects(users, USERS_FILE);
        }
    }

    @javafx.fxml.FXML
    public void signInBtnOnAction(ActionEvent actionEvent) throws IOException {
        enteredUsername = usernameTextField.getText();
        enteredPassword = passwordTextField.getText();
        selectedRole = roleComboBox.getValue();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in all fields.");
            return;
        }

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
            loggedInUser.login(enteredUsername, enteredPassword);
            DataPersistenceManager.saveObjects(allUsers, USERS_FILE);

            Node source = (Node) actionEvent.getSource();
            Stage loginStage = (Stage) source.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("nav-menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            NavMenuViewController controller = fxmlLoader.getController();

            controller.initData(loggedInUser);

            Stage newStage = new Stage();
            newStage.setTitle("BMA Application");
            newStage.setScene(scene);
            newStage.show();

            loginStage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}