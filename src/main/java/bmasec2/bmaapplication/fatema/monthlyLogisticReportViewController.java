package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class monthlyLogisticReportViewController
{
    @javafx.fxml.FXML
    private PieChart itemUsageBycategoryPieChart;
    @javafx.fxml.FXML
    private DatePicker monthDatePicker;
    @javafx.fxml.FXML
    private ComboBox reportTypeComboBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
    }
}