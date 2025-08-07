package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.Instructor;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class InstructorPerformanceViewController {

    @FXML private ComboBox<Instructor> selectInstructorComboBox;
    @FXML private BarChart<String, Number> performanceRatingsBarChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private ObservableList<Instructor> instructors;
    private static final String INSTRUCTORS_FILE = "instructors.bin";

    @FXML
    public void initialize() {

        xAxis.setLabel("Performance Metric");
        yAxis.setLabel("Rating");
        performanceRatingsBarChart.setTitle("Instructor Performance Overview");

        loadInstructors();


        selectInstructorComboBox.setOnAction(event -> viewPerformanceBtnOnAction(null));
    }

    private void loadInstructors() {
        List<Instructor> loadedInstructors = DataPersistenceManager.loadObjects(INSTRUCTORS_FILE);
        instructors = FXCollections.observableArrayList(loadedInstructors);
        selectInstructorComboBox.setItems(instructors);


        if (!instructors.isEmpty()) {
            selectInstructorComboBox.getSelectionModel().selectFirst();
            viewPerformanceBtnOnAction(null);
        }
    }

    @FXML
    public void viewPerformanceBtnOnAction(ActionEvent actionEvent) {
        Instructor selectedInstructor = selectInstructorComboBox.getSelectionModel().getSelectedItem();

        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.WARNING, "No Instructor Selected", "Please select an instructor to view performance.");
            return;
        }


        performanceRatingsBarChart.getData().clear();


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedInstructor.getName() + " Performance");

        Random rand = new Random();
        series.getData().add(new XYChart.Data<>("Teaching Quality", rand.nextInt(50) + 50)); // 50-99
        series.getData().add(new XYChart.Data<>("Cadet Engagement", rand.nextInt(50) + 50));
        series.getData().add(new XYChart.Data<>("Subject Mastery", rand.nextInt(50) + 50));
        series.getData().add(new XYChart.Data<>("Discipline", rand.nextInt(50) + 50));
        series.getData().add(new XYChart.Data<>("Overall Rating", rand.nextInt(50) + 50));

        performanceRatingsBarChart.getData().add(series);
        performanceRatingsBarChart.setTitle(selectedInstructor.getName() + " Performance Overview");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}