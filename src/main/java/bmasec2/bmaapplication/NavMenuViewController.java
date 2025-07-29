package bmasec2.bmaapplication;

import bmasec2.bmaapplication.system.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavMenuViewController {


    @FXML
    private AnchorPane contentArea;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label pageTitleLabel1;

    @FXML private VBox systemAdminMenu;
    @FXML private VBox commandantMenu;
    @FXML private VBox cadetMenu;
    @FXML private VBox cadetSupervisorMenu;
    @FXML private VBox trainingInstructorMenu;
    @FXML private VBox logisticsOfficerMenu;
    @FXML private VBox medicalOfficerMenu;
    @FXML private VBox messOfficerMenu;


    @FXML
    public void initialize() {

    }

    public void initData(String role, String username) {

        if (userNameLabel != null) {
            userNameLabel.setText("Welcome, " + username);
        }


        systemAdminMenu.setVisible(false);
        commandantMenu.setVisible(false);
        cadetMenu.setVisible(false);
        cadetSupervisorMenu.setVisible(false);
        trainingInstructorMenu.setVisible(false);
        logisticsOfficerMenu.setVisible(false);
        medicalOfficerMenu.setVisible(false);
        messOfficerMenu.setVisible(false);

        switch (role) {
            case "System Admin":
                systemAdminMenu.setVisible(true);
                loadView("shanin/systemAdminDashboardView.fxml", "Dashboard");
                break;
            case "Commandant":
                commandantMenu.setVisible(true);
                loadView("shanin/CommandantDashboardView.fxml", "Dashboard");
                break;
            case "Cadet":
                cadetMenu.setVisible(true);
                loadView("afifa/cadetDashboardView.fxml", "Dashboard");
                break;
            case "Cadet SuperVisor":
                cadetSupervisorMenu.setVisible(true);
                loadView("afifa/cadetSupervisorDashboardView.fxml", "Dashboard");
                break;
            case "Training Instructor":
                trainingInstructorMenu.setVisible(true);
                loadView("fatema/trainingInstructorDashboardView.fxml", "Dashboard");
                break;
            case "Logistic Officer":
                logisticsOfficerMenu.setVisible(true);
                loadView("fatema/logisticsOfficerDashboardView.fxml", "Dashboard");
                break;
            case "Medical Officer":
                medicalOfficerMenu.setVisible(true);
                loadView("zumar/medicalOfficerDashboardView.fxml", "Dashboard");
                break;
            case "Mess Officer":
                messOfficerMenu.setVisible(true);
                loadView("zumar/messOfficerDashboardView.fxml", "Dashboard");
                break;
            default:

                systemAdminMenu.setVisible(true);
                loadView("shanin/systemAdminDashboardView.fxml", "Dashboard");
                break;
        }
    }


    @FXML
    private void handleMenuClick(ActionEvent event) {

        Menu.switchMenu(event, this);
    }


    public void setContent(Parent view) {
        if (contentArea != null) {
            contentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } else {
            System.err.println("Error: contentArea is null. Check FXML file for fx:id='contentArea'.");
        }
    }


    public void setPageTitle(String title) {
        if (pageTitleLabel1 != null) {
            pageTitleLabel1.setText(title);
        } else {
            System.err.println("Error: pageTitleLabel1 is null. Check FXML file for fx:id='pageTitleLabel1'.");
        }
    }


    private void loadView(String fxmlPath, String title) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file for initial load: " + fxmlPath);
                return;
            }
            Parent view = FXMLLoader.load(fxmlUrl);
            setContent(view);
            setPageTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signOutBtnOnAction(ActionEvent actionEvent) throws IOException {

        Node source = (Node) actionEvent.getSource();
        Stage navStage = (Stage) source.getScene().getWindow();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage newStage = new Stage();
        newStage.setTitle("BMA Application");
        newStage.setScene(scene);
        newStage.show();


        navStage.close();
    }
}
