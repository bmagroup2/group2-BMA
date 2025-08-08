package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class messStaffDutyRosterViewController
{
    @javafx.fxml.FXML
    private TextField remarksTextField;
    @javafx.fxml.FXML
    private TextField dutyTimeTextField;
    @javafx.fxml.FXML
    private ComboBox staffMemberComboBox;
    @javafx.fxml.FXML
    private ListView messStaffDutyRosterListView;
    @javafx.fxml.FXML
    private DatePicker dateAssignDutyDatePicker;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void saveRosterOnActionButton(ActionEvent actionEvent) {
    }
}