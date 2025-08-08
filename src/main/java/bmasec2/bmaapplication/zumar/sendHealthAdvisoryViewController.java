package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class sendHealthAdvisoryViewController
{
    @javafx.fxml.FXML
    private TextField advisoryTitleTextField;
    @javafx.fxml.FXML
    private TextArea messageTextArea;
    @javafx.fxml.FXML
    private ComboBox recipientGroupComboBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void broadcastMessageOnActionButton(ActionEvent actionEvent) {
    }
}