package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ChartsView
{
    @javafx.fxml.FXML
    private Label labelOutPut;
    @javafx.fxml.FXML
    private PieChart pieChart;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void loadOnAction(ActionEvent actionEvent) {
        List<PieChart.Data> dataList = getData();
        pieChart.getData().addAll(dataList);
        pieChart.setTitle("Grade Distribution");
        pieChart.setClockwise(true);
    }

    private List<PieChart.Data> getData () {

        List<PieChart.Data> dataList = new ArrayList<>();
        dataList.add(new PieChart.Data("A", 4));
        dataList.add(new PieChart.Data("B", 9));
        dataList.add(new PieChart.Data("C", 10));
        dataList.add(new PieChart.Data("D", 3));
        dataList.add(new PieChart.Data("W", 1));

        return dataList;
    }
}