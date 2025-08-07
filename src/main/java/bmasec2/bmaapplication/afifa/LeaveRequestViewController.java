package bmasec2.bmaapplication.afifa;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

public class LeaveRequestViewController
{
    @javafx.fxml.FXML
    private ComboBox leavetypecombobox;
    @javafx.fxml.FXML
    private DatePicker datepickerend;
    @javafx.fxml.FXML
    private DatePicker datepickerstart;
    @javafx.fxml.FXML
    private TextArea reasontextarea;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void submitonaction(ActionEvent actionEvent) {
    }
}