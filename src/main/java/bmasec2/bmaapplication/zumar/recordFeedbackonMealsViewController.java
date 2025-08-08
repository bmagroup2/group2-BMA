package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class recordFeedbackonMealsViewController
{
    @javafx.fxml.FXML
    private Label feedbackInfoLabel;
    @javafx.fxml.FXML
    private ListView mealFeedbackListView;
    @javafx.fxml.FXML
    private DatePicker filterByDateDatePicker;
    @javafx.fxml.FXML
    private TextArea feedbackCommentsTextArea;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void filterOnActionButton(ActionEvent actionEvent) {
    }
}