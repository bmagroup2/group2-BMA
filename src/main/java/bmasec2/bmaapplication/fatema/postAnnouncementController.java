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

import java.util.List;
import java.util.UUID;

public class postAnnouncementController {

    @FXML
    private ComboBox<String> targetAudienceComboBox;
    @FXML
    private TextField announcementTitleTextField;
    @FXML
    private TextArea messageTextArea;

    private static final String ANNOUNCEMENTS_FILE = "announcements.dat";

    @FXML
    public void initialize() {

        targetAudienceComboBox.getItems().addAll("All Cadets", "Batch A", "Batch B", "Batch C", "My Assigned Cadets");
    }

    @FXML
    void postAnnouncementButtonOnAction(ActionEvent event) {
        String targetAudience = targetAudienceComboBox.getSelectionModel().getSelectedItem();
        String title = announcementTitleTextField.getText();
        String message = messageTextArea.getText();

        if (targetAudience == null || title.isEmpty() || message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }


        String createdByUserId = "trainingInstructor123"; 

        String announcementId = UUID.randomUUID().toString();
        Announcement newAnnouncement = new Announcement(
                announcementId, title, message, createdByUserId);

        List<Announcement> announcements = DataPersistenceManager.loadObjects(ANNOUNCEMENTS_FILE);
        announcements.add(newAnnouncement);
        DataPersistenceManager.saveObjects(announcements, ANNOUNCEMENTS_FILE);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Announcement posted successfully! It will be reviewed by the Commandant.");


        targetAudienceComboBox.getSelectionModel().clearSelection();
        announcementTitleTextField.clear();
        messageTextArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


