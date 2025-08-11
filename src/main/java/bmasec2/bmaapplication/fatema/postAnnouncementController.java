package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class postAnnouncementController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private ComboBox<String> targetAudienceComboBox;

    @FXML
    public void initialize() {
        targetAudienceComboBox.setItems(FXCollections.observableArrayList(
                "All Cadets", "Specific Batches", "All Instructors", "All Officers", "All Users"
        ));
        targetAudienceComboBox.setValue("All Cadets");
    }

    @FXML
    public void postAnnouncementButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String announcementId = "ANN-" + UUID.randomUUID().toString().substring(0, 8);
            String title = titleTextField.getText().trim();
            String message = messageTextArea.getText().trim();
            String createdBy = "Training Instructor"; // In real app, get from logged-in user
            String targetAudience = targetAudienceComboBox.getValue();

            Announcement announcement = new Announcement(
                    announcementId, title, message, createdBy, new Date(), targetAudience, "Pending Approval"
            );

            List<Announcement> announcements = DataPersistenceManager.loadObjects("announcements.bin");
            announcements.add(announcement);
            DataPersistenceManager.saveObjects(announcements, "announcements.bin");

            showAlert("Success", "Announcement posted successfully!\nAnnouncement ID: " + announcementId);
            clearForm();

        } catch (Exception e) {
            showAlert("Error", "Failed to post announcement: " + e.getMessage());
        }
    }

    @FXML
    public void clearFormButtonOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    private boolean validateInputs() {
        if (titleTextField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a title for the announcement.");
            return false;
        }

        if (messageTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter the announcement message.");
            return false;
        }

        if (messageTextArea.getText().trim().length() < 10) {
            showAlert("Validation Error", "Message should be at least 10 characters long.");
            return false;
        }

        if (targetAudienceComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a target audience.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        titleTextField.clear();
        messageTextArea.clear();
        targetAudienceComboBox.setValue("All Cadets");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
