package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class BarChartsViewController
{
    @javafx.fxml.FXML
    private CategoryAxis yAxis;
    @javafx.fxml.FXML
    private TableView<Grade> tableViewScene;
    @javafx.fxml.FXML
    private TableColumn<Grade,Integer> countCol;
    @javafx.fxml.FXML
    private BarChart<String, Integer> barChart;
    @javafx.fxml.FXML
    private NumberAxis xAxis;
    @javafx.fxml.FXML
    private TextField countTF;
    @javafx.fxml.FXML
    private TextField gradeTF;
    @javafx.fxml.FXML
    private TableColumn<String,Integer> gradeCol;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void addBtnOnAction(ActionEvent actionEvent) {
//        String l = gradeTF.getText();
//        int c = Integer.parseInt(countTF.getText());
//        tableViewScene.getItems().addAll(l,c);
    }

    @javafx.fxml.FXML
    public void generateBtnOnAction(ActionEvent actionEvent) {
        List<XYChart.Data<String, Integer>> dataPoints = new ArrayList<>();
        dataPoints.add(new XYChart.Data<>("A", 4));
        dataPoints.add(new XYChart.Data<>("B", 9));
        dataPoints.add(new XYChart.Data<>("C", 10));
        dataPoints.add(new XYChart.Data<>("D", 3));
        dataPoints.add(new XYChart.Data<>("W", 1));

        XYChart.Series<String, Integer> section1 = new XYChart.Series<>();
        section1.getData().addAll(dataPoints);
        section1.setName("Section 1");

        List<XYChart.Data<String, Integer>> dataPoints2 = new ArrayList<>();
        dataPoints2.add(new XYChart.Data<>("A", 3));
        dataPoints2.add(new XYChart.Data<>("B", 12));
        dataPoints2.add(new XYChart.Data<>("C", 11));
        dataPoints2.add(new XYChart.Data<>("D", 9));
        dataPoints2.add(new XYChart.Data<>("W", 8));

        XYChart.Series<String, Integer> section2 = new XYChart.Series<>();
        section2.getData().addAll(dataPoints2);
        section1.setName("Section 2");

        barChart.getData().clear();
        barChart.getData().addAll(section1, section2);
    }

    @javafx.fxml.FXML
    public void deleteBtnOnAction(ActionEvent actionEvent) {
        int selectedIndex = tableViewScene.getSelectionModel().getSelectedIndex();

    }

//    private List<XYChart.Data> getData () {
//
//    }
}