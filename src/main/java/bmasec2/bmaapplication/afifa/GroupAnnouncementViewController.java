package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GroupAnnouncementViewController {
    @FXML
    private TextField announcmenttextfield;
    @FXML
    private TextArea messagetextarea;
    @FXML
    private ComboBox<String> targetgroupcombox;

    private CadetSupervisor loggedInSupervisor;

    @FXML
    public void initialize() {
        // Populate target group combobox (placeholder)
        ObservableList<String> targetGroups = FXCollections.observableArrayList(
                "All Cadets", "Alpha Company", "Bravo Company", "Charlie Company", "Delta Company"
        );
        targetgroupcombox.setItems(targetGroups);
    }

    public void initData(CadetSupervisor supervisor) {
        this.loggedInSupervisor = supervisor;
    }

    @FXML
    public void announcmentonaction(ActionEvent actionEvent) {
        String title = announcmenttextfield.getText();
        String message = messagetextarea.getText();
        String targetAudience = targetgroupcombox.getValue();

        if (title.isEmpty() || message.isEmpty() || targetAudience == null || targetAudience.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        // Create a unique announcement ID
        String announcementId = UUID.randomUUID().toString();

//        Announcement newAnnouncement = new Announcement(
//                announcementId,
//                title,
//                message,
//                loggedInSupervisor != null ? loggedInSupervisor.getName() : "Unknown Supervisor",
//                targetAudience
//        );

        List<Announcement> announcements = DataPersistenceManager.loadObjects("announcements.dat");
//        announcements.add(newAnnouncement);
//        DataPersistenceManager.saveObjects(announcements, "announcements.dat");

        showAlert(AlertType.INFORMATION, "Success", "Announcement broadcasted successfully!");
        clearForm();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        announcmenttextfield.clear();
        messagetextarea.clear();
        targetgroupcombox.getSelectionModel().clearSelection();
    }
}


