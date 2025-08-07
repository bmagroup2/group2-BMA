package bmasec2.bmaapplication.shanin;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MissionEventsViewController
{
    @javafx.fxml.FXML
    private TableColumn eventNameColumn;
    @javafx.fxml.FXML
    private TextField eventNameTextField;
    @javafx.fxml.FXML
    private DatePicker eventDateDatePicker;
    @javafx.fxml.FXML
    private TableView upcomingAndPastEventsTableView;
    @javafx.fxml.FXML
    private TableColumn eventDateColumn;
    @javafx.fxml.FXML
    private TextField descriptionTextField;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void deleteBtnOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void saveEventBtnOnAction(ActionEvent actionEvent) {
    }
}