package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

import bmasec2.bmaapplication.system.Menu;
import javafx.stage.Stage;

public class NavMenuViewController {


    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {

        loadView("systemAdminDashboardView.fxml");
    }



    @FXML
    private void handleMenuClick(ActionEvent event) {

        Menu.switchMenu(event);
    }


    private void loadView(String fxmlFile) {
        try {

            URL fxmlUrl = getClass().getResource("/bmasec2/bmaapplication/shanin/" + fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file for initial load: " + fxmlFile);
                return;
            }
            Parent view = FXMLLoader.load(fxmlUrl);
            contentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void signOutBtnOnAction(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage NavMenu = (Stage) source.getScene().getWindow();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage newStage = new Stage();
        newStage.setTitle("BMA Application");
        newStage.setScene(scene);
        newStage.show();


        NavMenu.close();
    }
}
