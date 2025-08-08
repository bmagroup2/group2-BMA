package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.DatePicker;

public class monthlyFoodUsageReportViewController
{
    @javafx.fxml.FXML
    private BarChart foodUsageBarChart;
    @javafx.fxml.FXML
    private DatePicker selectMonthDatePicker;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void generateReportOnActionButton(ActionEvent actionEvent) {
    }
}