package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PerformanceEvaluationViewController {

    @FXML
    private ComboBox<String> myperformaceevaluationcombobox;
    @FXML
    private BarChart<String, Number> myperformancescorebarchart;

    private Cadet loggedInCadet;

    @FXML
    public void initialize() {
        
        
        
        
        

        myperformaceevaluationcombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayPerformance(newValue);
            }
        });
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
        if (loggedInCadet != null) {
            loadEvaluationTypes();
        }
    }

    private void loadEvaluationTypes() {
        List<Evaluation> allEvaluations = DataPersistenceManager.loadObjects("evaluations.dat");
        ObservableList<String> evaluationTypes = FXCollections.observableArrayList();

        allEvaluations.stream()
                .filter(eval -> eval.getCadetId().equals(loggedInCadet.getUserId()))
                .map(eval -> (String) eval.getEvaluationType())
                .distinct()
                .forEach(evaluationTypes::add);

        myperformaceevaluationcombobox.setItems(evaluationTypes);
        if (!evaluationTypes.isEmpty()) {
            myperformaceevaluationcombobox.getSelectionModel().selectFirst();
        }
    }

    private void displayPerformance(String evaluationType) {
        myperformancescorebarchart.getData().clear();

        List<Evaluation> allEvaluations = DataPersistenceManager.loadObjects("evaluations.dat");
        List<Evaluation> cadetEvaluations = allEvaluations.stream()
                .filter(eval -> eval.getCadetId().equals(loggedInCadet.getUserId()) && eval.getEvaluationType().equals(evaluationType))
                .collect(Collectors.toList());

        if (!cadetEvaluations.isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(evaluationType + " Scores");

            for (Evaluation eval : cadetEvaluations) {
                series.getData().add(new XYChart.Data<>(eval.getEvalId(), eval.getScore()));
            }
            myperformancescorebarchart.getData().add(series);
        } else {
            
            System.out.println("No evaluation data found for " + loggedInCadet.getName() + " for " + evaluationType);
        }
    }
}


