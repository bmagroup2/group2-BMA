package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;
import java.util.UUID;

public class sendHealthAdvisoryViewController
{
    @javafx.fxml.FXML
    private TextField advisoryTitleTextField;
    @javafx.fxml.FXML
    private TextArea messageTextArea;
    @javafx.fxml.FXML
    private ComboBox<String> recipientGroupComboBox;

    @javafx.fxml.FXML
    public void initialize() {
        setupRecipientGroups();
    }

    private void setupRecipientGroups() {
        ObservableList<String> recipientOptions = FXCollections.observableArrayList();
        recipientOptions.addAll(
            "All Cadets",
            "First Year Cadets",
            "Second Year Cadets",
            "Third Year Cadets",
            "Fourth Year Cadets",
            "All Staff",
            "Medical Staff Only"
        );
        recipientGroupComboBox.setItems(recipientOptions);
        recipientGroupComboBox.setValue("All Cadets");
    }

    @javafx.fxml.FXML
    public void broadcastMessageOnActionButton(ActionEvent actionEvent) {
        try {
            
            if (advisoryTitleTextField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter advisory title.");
                return;
            }
            
            if (messageTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter advisory message.");
                return;
            }
            
            if (recipientGroupComboBox.getValue() == null) {
                showAlert("Error", "Please select recipient group.");
                return;
            }
            

            String title = advisoryTitleTextField.getText().trim();
            String message = messageTextArea.getText().trim();
            String recipientGroup = recipientGroupComboBox.getValue();
            

            String announcementId = UUID.randomUUID().toString();
            String content = "HEALTH ADVISORY\n\n" + message + "\n\nTarget Group: " + recipientGroup;
            
            Announcement healthAdvisory = new Announcement(
                announcementId,
                "Health Advisory: " + title,
                content,
                "Medical Officer",
                recipientGroup
            );
            

            DataPersistenceManager.addAnnouncementAndSave(healthAdvisory);
            

            showAlert("Success", "Health advisory broadcasted successfully to " + recipientGroup + "!");
            

            clearForm();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to send health advisory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        advisoryTitleTextField.clear();
        messageTextArea.clear();
        recipientGroupComboBox.setValue("All Cadets");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}