package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class monthlyMedicalReportViewController
{
    @javafx.fxml.FXML
    private ComboBox reportTypeComboBox;
    @javafx.fxml.FXML
    private BarChart medicalReportSummaryBarChart;
    @javafx.fxml.FXML
    private DatePicker monthDatePicker;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void generateReportOnActionButton(ActionEvent actionEvent) {
    }
}