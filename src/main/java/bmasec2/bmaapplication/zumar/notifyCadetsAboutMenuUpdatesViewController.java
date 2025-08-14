package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.UUID;

public class notifyCadetsAboutMenuUpdatesViewController {

    @FXML
    private TextField notificationTitleTextField;
    @FXML
    private TextArea messageTextArea;

    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    @FXML
    public void sendNotificationOnActionButton(ActionEvent actionEvent) {
        try {
            if (notificationTitleTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter notification title.");
                return;
            }
            if (messageTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter notification message.");
                return;
            }

            String title = notificationTitleTextField.getText().trim();
            String message = messageTextArea.getText().trim();

            // Create an announcement for menu updates
            String announcementId = UUID.randomUUID().toString();
            String content = "MENU UPDATE: " + message;

            Announcement menuUpdate = new Announcement(
                    announcementId,
                    title,
                    content,
                    "Mess Officer", // Created by
                    "All Cadets" // Target audience
            );

            DataPersistenceManager.addAnnouncementAndSave(menuUpdate);
            showAlert("Success", "Menu update notification sent successfully!");
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to send notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        notificationTitleTextField.clear();
        messageTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}