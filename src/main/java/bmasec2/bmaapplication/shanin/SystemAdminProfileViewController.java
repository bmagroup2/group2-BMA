package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.shanin.SystemAdministrator;
import bmasec2.bmaapplication.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SystemAdminProfileViewController {

    @FXML private TextField fullNameTextField;
    @FXML private PasswordField currentPasswordTextField;
    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField confirmNewPasswordTextField;
    @FXML private TextField emailAddressTextField;

    private SystemAdministrator loggedInAdmin;
    private static final String USERS_FILE = "users.dat";

    @FXML
    public void initialize() {

        List<User> users = DataPersistenceManager.loadObjects(USERS_FILE);
        Optional<User> adminOptional = users.stream()
                .filter(user -> user.getRole().equals("System Admin"))
                .findFirst();

        if (adminOptional.isPresent()) {
            loggedInAdmin = (SystemAdministrator) adminOptional.get();
            populateProfileFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "System Administrator not found in data.");

        }
    }

    private void populateProfileFields() {
        if (loggedInAdmin != null) {
            fullNameTextField.setText(loggedInAdmin.getName());
            emailAddressTextField.setText(loggedInAdmin.getEmail());
            currentPasswordTextField.setText("");
            newPasswordTextField.setText("");
            confirmNewPasswordTextField.setText("");
        }
    }

    @FXML
    public void updateProfileBtnOnAction(ActionEvent actionEvent) {
        if (loggedInAdmin == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No System Administrator profile loaded.");
            return;
        }

        String newFullName = fullNameTextField.getText();
        String newEmail = emailAddressTextField.getText();
        String currentPassword = currentPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmNewPassword = confirmNewPasswordTextField.getText();

        Map<String, String> updateData = new HashMap<>();
        updateData.put("name", newFullName);
        updateData.put("email", newEmail);
        loggedInAdmin.updateProfile(updateData);


        if (!newPassword.isEmpty() || !currentPassword.isEmpty()) {
            if (!loggedInAdmin.verifyPassword(currentPassword)) {
                showAlert(Alert.AlertType.ERROR, "Password Error", "Current password is incorrect.");
                return;
            }
            if (!newPassword.equals(confirmNewPassword)) {
                showAlert(Alert.AlertType.ERROR, "Password Error", "New password and confirm password do not match.");
                return;
            }
            if (newPassword.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Password Weak", "New password should be at least 6 characters long.");
                return;
            }
            loggedInAdmin.setPassword(newPassword);
            showAlert(Alert.AlertType.INFORMATION, "Password Changed", "Password updated successfully.");
        }


        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        List<User> updatedUsers = allUsers.stream()
                .map(user -> user.getUserId().equals(loggedInAdmin.getUserId()) ? loggedInAdmin : user)
                .collect(Collectors.toList());
        DataPersistenceManager.saveObjects(updatedUsers, USERS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Profile information saved successfully.");
        populateProfileFields();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}