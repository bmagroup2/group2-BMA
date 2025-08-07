package bmasec2.bmaapplication.afifa;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AttendenceMonitorViewController
{

    @javafx.fxml.FXML
    private ComboBox attendencecadetcombobox;
    @javafx.fxml.FXML
    private DatePicker attendencedatepicker;
    @javafx.fxml.FXML
    private TableColumn attendencedatecolumn;
    @javafx.fxml.FXML
    private TableView attendencemonitortableview;
    @javafx.fxml.FXML
    private TableColumn attendenceceactivitycolumn;
    @javafx.fxml.FXML
    private TableColumn attendencestatuscolumn;
    @javafx.fxml.FXML
    private TableColumn attendencecadetnamecolumn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void fetchrecordsonaction(ActionEvent actionEvent) {
    }
}