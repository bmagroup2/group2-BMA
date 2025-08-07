package bmasec2.bmaapplication.afifa;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class CSLeaveRequestViewController
{

    @javafx.fxml.FXML
    private Label leavereasonlabel;
    @javafx.fxml.FXML
    private Label leavedaterangelabel;
    @javafx.fxml.FXML
    private ListView cadetleavependingrequestlistview;
    @javafx.fxml.FXML
    private TextArea leaverequesttextarea;
    @javafx.fxml.FXML
    private Label leavecadetnamelabel;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @Deprecated
    public void rejectonaction(ActionEvent actionEvent) {
    }

    @Deprecated
    public void approveonaction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void leaveapproveonaction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void leaverejectonaction(ActionEvent actionEvent) {
    }
}