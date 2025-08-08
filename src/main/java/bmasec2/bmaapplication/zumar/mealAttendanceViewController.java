package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class mealAttendanceViewController
{
    @javafx.fxml.FXML
    private TableColumn statusColn;
    @javafx.fxml.FXML
    private TableColumn cadetIdColn;
    @javafx.fxml.FXML
    private ComboBox selectBatchComboBox;
    @javafx.fxml.FXML
    private TableColumn cadetNameColn;
    @javafx.fxml.FXML
    private DatePicker selectDateMealAttendanceDatePicker;
    @javafx.fxml.FXML
    private TableView mealAttendanceTableView;
    @javafx.fxml.FXML
    private ComboBox selectMealComboBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void viewAttendanceOnActionButton(ActionEvent actionEvent) {
    }
}