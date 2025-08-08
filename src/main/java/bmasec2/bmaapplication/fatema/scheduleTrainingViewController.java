package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class scheduleTrainingViewController
{
    @javafx.fxml.FXML
    private DatePicker dateDatePicker;
    @javafx.fxml.FXML
    private ListView upcomingschedulesessionListView;
    @javafx.fxml.FXML
    private TextField timeTextField;
    @javafx.fxml.FXML
    private ComboBox cadetgroupComboBox;
    @javafx.fxml.FXML
    private TextField topicTextField;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void saveScheduleButtonOnAction(ActionEvent actionEvent) {
    }
}