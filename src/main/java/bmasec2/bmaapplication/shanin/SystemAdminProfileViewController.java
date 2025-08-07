package bmasec2.bmaapplication.shanin;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SystemAdminProfileViewController
{
    @javafx.fxml.FXML
    private TextField fullNameTextField;
    @javafx.fxml.FXML
    private PasswordField currentPasswordTextField;
    @javafx.fxml.FXML
    private PasswordField newPasswordTextField;
    @javafx.fxml.FXML
    private PasswordField confirmNewPasswordTextField;
    @javafx.fxml.FXML
    private TextField emailAddressTextField;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void updateProfileBtnOnAction(ActionEvent actionEvent) {
    }
}