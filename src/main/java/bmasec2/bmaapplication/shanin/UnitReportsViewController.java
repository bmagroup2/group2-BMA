package bmasec2.bmaapplication.shanin;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

public class UnitReportsViewController
{
    @javafx.fxml.FXML
    private DatePicker endDateDatePicker;
    @javafx.fxml.FXML
    private TextArea reportContentDisplayTextArea;
    @javafx.fxml.FXML
    private ComboBox chooseUnitComboBox;
    @javafx.fxml.FXML
    private DatePicker startDateDatePicker;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void generateReportBtnOnAction(ActionEvent actionEvent) {
    }
}