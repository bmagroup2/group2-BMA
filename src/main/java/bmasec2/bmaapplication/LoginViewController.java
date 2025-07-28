package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void signInBtnOnAction(ActionEvent actionEvent) throws IOException {

        Node source = (Node) actionEvent.getSource();
        Stage loginStage = (Stage) source.getScene().getWindow();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("nav-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage newStage = new Stage();
        newStage.setTitle("BMA Application");
        newStage.setScene(scene);
        newStage.show();


        loginStage.close();


    }
}