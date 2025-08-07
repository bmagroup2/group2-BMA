package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnouncementApprovalViewController {

    @FXML private ListView<Announcement> pendingAnnouncementListView;
    @FXML private TextArea announcementContentTextArea;

    private ObservableList<Announcement> pendingAnnouncements;
    private static final String ANNOUNCEMENTS_FILE = "announcements.bin";

    @FXML
    public void initialize() {
        loadPendingAnnouncements();

        pendingAnnouncementListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                announcementContentTextArea.setText(newSelection.getContent());
            } else {
                announcementContentTextArea.setText("");
            }
        });
    }

    private void loadPendingAnnouncements() {
        List<Announcement> allAnnouncements = DataPersistenceManager.loadObjects(ANNOUNCEMENTS_FILE);
        pendingAnnouncements = FXCollections.observableArrayList(
                allAnnouncements.stream()
                        .filter(ann -> ann.getStatus().equals("Pending"))
                        .collect(Collectors.toList())
        );
        pendingAnnouncementListView.setItems(pendingAnnouncements);
    }

    private void saveAnnouncements() {

        List<Announcement> allAnnouncements = DataPersistenceManager.loadObjects(ANNOUNCEMENTS_FILE);


        List<Announcement> updatedPending = pendingAnnouncements.stream()
                .filter(ann -> !ann.getStatus().equals("Pending"))
                .collect(Collectors.toList());


        allAnnouncements.removeIf(ann -> updatedPending.stream().anyMatch(upd -> upd.getAnnouncementId().equals(ann.getAnnouncementId())));
        allAnnouncements.addAll(updatedPending);

        DataPersistenceManager.saveObjects(allAnnouncements, ANNOUNCEMENTS_FILE);
    }

    @FXML
    public void approveBtnOnAction(ActionEvent actionEvent) {
        Announcement selectedAnnouncement = pendingAnnouncementListView.getSelectionModel().getSelectedItem();
        if (selectedAnnouncement != null) {
            selectedAnnouncement.setStatus("Approved");
            saveAnnouncements();
            loadPendingAnnouncements();
            showAlert(Alert.AlertType.INFORMATION, "Approved", "Announcement approved successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an announcement to approve.");
        }
    }

    @FXML
    public void rejectBtnOnAction(ActionEvent actionEvent) {
        Announcement selectedAnnouncement = pendingAnnouncementListView.getSelectionModel().getSelectedItem();
        if (selectedAnnouncement != null) {
            selectedAnnouncement.setStatus("Rejected");
            saveAnnouncements();
            loadPendingAnnouncements();
            showAlert(Alert.AlertType.INFORMATION, "Rejected", "Announcement rejected successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an announcement to reject.");
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