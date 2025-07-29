package bmasec2.bmaapplication.system;

import bmasec2.bmaapplication.NavMenuViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;

public class Menu {


    public static void switchMenu(ActionEvent event, NavMenuViewController controller) {
        Button clickedButton = (Button) event.getSource();


        String fxmlPath = (String) clickedButton.getUserData();
        String pageTitle = clickedButton.getText();

        if (fxmlPath == null || fxmlPath.isEmpty()) {
            System.err.println("Error: FXML path is not set in the button's userData.");
            return;
        }

        try {

            String absolutePath = "/bmasec2/bmaapplication/" + fxmlPath;

            URL fxmlUrl = Menu.class.getResource(absolutePath);

            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file at path: " + absolutePath);
                return;
            }

            Parent newView = FXMLLoader.load(fxmlUrl);


            controller.setContent(newView);
            controller.setPageTitle(pageTitle);

        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
