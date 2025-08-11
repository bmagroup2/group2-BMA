package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class notifyCommandantViewController {
    @FXML
    private ComboBox<String> criticalItemComboBox;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextArea messageTextArea;

    @FXML
    public void initialize() {

        criticalItemComboBox.setItems(FXCollections.observableArrayList(
                "Ammunition (Low Stock)", "Medical Kits (Expired)", "Rations (Critical Shortage)",
                "Vehicle Fuel (Low)", "Training Equipment (Damaged)"
        ));
    }

    @FXML
    public void sendNotificationButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String selectedItem = criticalItemComboBox.getValue();
            String subject = subjectTextField.getText().trim();
            String message = messageTextArea.getText().trim();


            String announcementId = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8);
            String title = "URGENT: " + subject + " - " + selectedItem;
            String content = "From Logistics Officer:\n\n" + message;
            String createdBy = "Logistics Officer"; // In real app, get from logged-in user
            String targetAudience = "Commandant";

            Announcement notification = new Announcement(
                    announcementId, title, content, createdBy, new Date(), targetAudience, "Pending Approval"
            );

            List<Announcement> announcements = DataPersistenceManager.loadObjects("announcements.bin");
            announcements.add(notification);
            DataPersistenceManager.saveObjects(announcements, "announcements.bin");

            showAlert("Success", "Notification sent to Commandant successfully!\nNotification ID: " + announcementId);
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to send notification: " + e.getMessage());
        }
    }

    @FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    private boolean validateInputs() {
        if (criticalItemComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a critical item.");
            return false;
        }

        if (subjectTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a subject for the notification.");
            return false;
        }

        if (messageTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your message.");
            return false;
        }

        if (messageTextArea.getText().trim().length() < 20) {
            showAlert("Validation Error", "Message should be at least 20 characters long.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        criticalItemComboBox.setValue(null);
        subjectTextField.clear();
        messageTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
