package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class cadetHealthCheckupViewController
{
    @javafx.fxml.FXML
    private TextField healthCheckupTimeTextField;
    @javafx.fxml.FXML
    private DatePicker healthCheckupDateDatePicker;
    @javafx.fxml.FXML
    private TextField healthCheckupRoomTextField;
    @javafx.fxml.FXML
    private ListView upcomingScheduledCheckupsListView;
    @javafx.fxml.FXML
    private ComboBox healthCheckupCadetBatchComboBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void healthCheckupSaveScheduleOnActionButton(ActionEvent actionEvent) {
    }
}