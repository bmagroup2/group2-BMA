package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginViewController
{
    @javafx.fxml.FXML
    private PasswordField passwordTextField;
    @javafx.fxml.FXML
    private ComboBox<String> roleComboBox;
    @javafx.fxml.FXML
    private TextField usernameTextField;

    @javafx.fxml.FXML
    public void initialize() {
        roleComboBox.getItems().addAll("System Admin", "Commandant", "Cadet", "Cadet SuperVisor", "Training Instructor", "Logistic Officer", "Medical Officer", "Mess Officer" );
    }

    @javafx.fxml.FXML
    public void signInBtnOnAction(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String role = roleComboBox.getValue();
        if (username.isEmpty() && password.isEmpty() && role == null ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Handling");
            alert.setContentText("Please fillup all field");
            alert.showAndWait();
            return;
        }

        if (username.equals("admin") && Objects.equals(password, "admin1234") && Objects.equals(role, "System Admin") ||
                username.equals("commandant") && Objects.equals(password, "commandant1234") && Objects.equals(role, "Commandant") ||
                username.equals("cadet") && Objects.equals(password, "cadet1234") && Objects.equals(role, "Cadet") ||
                username.equals("cadetsupervisor") && Objects.equals(password, "cadetsupervisor1234") && Objects.equals(role, "Cadet SuperVisor") ||
                username.equals("traininginstructor") && Objects.equals(password, "traininginstructor1234") && Objects.equals(role, "Training Instructor") ||
                username.equals("logisticofficer") && Objects.equals(password, "logisticofficer1234") && Objects.equals(role, "Logistic Officer") ||
                username.equals("medicalofficer") && Objects.equals(password, "medicalofficer1234") && Objects.equals(role, "Medical Officer") ||
                username.equals("messofficer") && Objects.equals(password, "messofficer1234") && Objects.equals(role, "Mess Officer")


        ) {

            Node source = (Node) actionEvent.getSource();
            Stage loginStage = (Stage) source.getScene().getWindow();


            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("nav-menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());


            NavMenuViewController controller = fxmlLoader.getController();


            controller.initData(role, username);


            Stage newStage = new Stage();
            newStage.setTitle("BMA Application");
            newStage.setScene(scene);
            newStage.show();


            loginStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Handling");
            alert.setContentText("Please enter a valid username and password");
            alert.showAndWait();
        }




    }
}