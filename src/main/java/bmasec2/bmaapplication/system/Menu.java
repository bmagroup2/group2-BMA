package bmasec2.bmaapplication.system;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;



public class Menu {

    public static void switchMenu(ActionEvent event) {
        String fxmlFile = ((Button) event.getSource()).getUserData().toString();

        try {

            URL fxmlUrl = Menu.class.getResource("/bmasec2/bmaapplication/shanin/" + fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file: " + fxmlFile);
                return;
            }
            Parent newView = FXMLLoader.load(fxmlUrl);

            Scene mainScene = ((Node) event.getSource()).getScene();
            AnchorPane contentArea = (AnchorPane) mainScene.lookup("#contentArea");

            if (contentArea != null) {
                contentArea.getChildren().setAll(newView);
                AnchorPane.setTopAnchor(newView, 0.0);
                AnchorPane.setBottomAnchor(newView, 0.0);
                AnchorPane.setLeftAnchor(newView, 0.0);
                AnchorPane.setRightAnchor(newView, 0.0);
            } else {
                System.err.println("Error: Could not find AnchorPane with fx:id='contentArea'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

