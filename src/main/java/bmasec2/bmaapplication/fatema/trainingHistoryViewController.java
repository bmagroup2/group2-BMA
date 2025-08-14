package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class trainingHistoryViewController {

    @FXML
    private ComboBox<Cadet> seelctCadetComboBox;
    @FXML
    private TableView<Evaluation> cadetTrainingHistoryTableView;
    @FXML
    private TableColumn<Evaluation, LocalDate> dateTableColumn;
    @FXML
    private TableColumn<Evaluation, String> trainingSessionTableColumn;
    @FXML
    private TableColumn<Evaluation, String> instructorTableColumn;
    @FXML
    private TableColumn<Evaluation, Double> scoreTableColumn;

    private static final String CADETS_FILE = "cadets.dat";
    private static final String EVALUATIONS_FILE = "evaluations.dat";

    private ObservableList<Cadet> allCadets;
    private ObservableList<Evaluation> allEvaluations;

    @FXML
    public void initialize() {

        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("evaluationDate"));
        trainingSessionTableColumn.setCellValueFactory(new PropertyValueFactory<>("evaluationType"));
        instructorTableColumn.setCellValueFactory(new PropertyValueFactory<>("evaluatorName"));
        scoreTableColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        loadData();
        populateCadetComboBox();
    }

    private void loadData() {
        allCadets = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(CADETS_FILE));
        allEvaluations = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(EVALUATIONS_FILE));
    }

    private void populateCadetComboBox() {
        seelctCadetComboBox.setItems(allCadets);
        seelctCadetComboBox.setConverter(new javafx.util.StringConverter<Cadet>() {
            @Override
            public String toString(Cadet cadet) {
                return cadet != null ? cadet.getName() + " (" + cadet.getCadetId() + ")" : "";
            }

            @Override
            public Cadet fromString(String string) {
                return allCadets.stream()
                        .filter(cadet -> (cadet.getName() + " (" + cadet.getCadetId() + ")").equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void viewHistoryButtonOnAction(ActionEvent event) {
        Cadet selectedCadet = seelctCadetComboBox.getSelectionModel().getSelectedItem();

        if (selectedCadet == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a cadet to view their training history.");
            return;
        }

        ObservableList<Evaluation> cadetEvaluations = allEvaluations.stream()
                .filter(evaluation -> evaluation.getCadetId().equals(selectedCadet.getCadetId()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        cadetTrainingHistoryTableView.setItems(cadetEvaluations);

        if (cadetEvaluations.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No History", "No training history found for the selected cadet.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


