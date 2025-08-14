package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.SystemSettings;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SystemSettingsViewController {

    @FXML private TextField applicationNameTextField;
    @FXML private ImageView applicationLogoImageView;

    private SystemSettings currentSettings;
    private static final String SETTINGS_FILE = "system_settings.dat";

    @FXML
    public void initialize() {

        List<SystemSettings> loadedSettings = DataPersistenceManager.loadObjects(SETTINGS_FILE);
        if (loadedSettings.isEmpty()) {
            currentSettings = new SystemSettings();
        } else {
            currentSettings = loadedSettings.get(0);
        }


        applicationNameTextField.setText(currentSettings.getApplicationName());
        if (currentSettings.getApplicationLogoPath() != null && !currentSettings.getApplicationLogoPath().isEmpty()) {
            try {
                File logoFile = new File(currentSettings.getApplicationLogoPath());
                if (logoFile.exists()) {
                    Image image = new Image(new FileInputStream(logoFile));
                    applicationLogoImageView.setImage(image);
                } else {
                    System.err.println("Logo file not found: " + currentSettings.getApplicationLogoPath());
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error loading logo image: " + e.getMessage());
            }
        }
    }

    @FXML
    public void updateNewLogoBtnOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Application Logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(applicationLogoImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                applicationLogoImageView.setImage(image);
                currentSettings.setApplicationLogoPath(selectedFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not load image: " + e.getMessage());
            }
        }
    }

    @FXML
    public void saveChangesBtnOnAction(ActionEvent actionEvent) {
        currentSettings.setApplicationName(applicationNameTextField.getText());

        // Save the updated settings
        DataPersistenceManager.saveObjects(List.of(currentSettings), SETTINGS_FILE);
        showAlert(Alert.AlertType.INFORMATION, "Settings Saved", "System settings updated successfully.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}