package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinalEvolationViewController {
    @FXML private TableColumn<Evaluation, String> cadetIdColumn;
    @FXML private TableView<Evaluation> filterByListTableView;
    @FXML private TableColumn<Evaluation, String> statusColumn;
    @FXML private TextArea scoreBreakdownAndNotesTextArea;
    @FXML private TableColumn<Evaluation, String> cadetNameColumn;
    @FXML private ComboBox<String> filterByBatchComboBox;
    @FXML private Label cadetNameLabel;

    private ObservableList<Evaluation> masterEvaluationList;
    private static final String EVALUATIONS_FILE = "evaluations.dat";

    @FXML
    public void initialize() {

        cadetIdColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        cadetNameColumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        loadEvaluations();


        filterByBatchComboBox.getItems().addAll("All", "Batch A", "Batch B", "Batch C");
        filterByBatchComboBox.setValue("All");


        filterByListTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cadetNameLabel.setText("Cadet: " + newSelection.getCadetName() + " (ID: " + newSelection.getCadetId() + ")");
                scoreBreakdownAndNotesTextArea.setText(
                        "Score Breakdown:\n" + newSelection.getScoreBreakdown() +
                                "\n\nNotes:\n" + newSelection.getNotes()
                );
            } else {
                cadetNameLabel.setText("Cadet: ");
                scoreBreakdownAndNotesTextArea.setText("");
            }
        });


        filterByBatchComboBox.setOnAction(event -> filterEvaluations());
    }

    private void loadEvaluations() {
        List<Evaluation> loadedEvals = DataPersistenceManager.loadObjects(EVALUATIONS_FILE);
        masterEvaluationList = FXCollections.observableArrayList(loadedEvals);
        filterEvaluations();


        if (masterEvaluationList.isEmpty()) {
            masterEvaluationList.add(new Evaluation("EVAL-001", "C-001", "John Doe", "Batch A", "I-001", "Drill: 85, Academics: 90", "Good overall performance."));
            masterEvaluationList.add(new Evaluation("EVAL-002", "C-002", "Jane Smith", "Batch B", "I-002", "Drill: 70, Academics: 75", "Needs improvement in drill."));
            masterEvaluationList.add(new Evaluation("EVAL-003", "C-003", "Peter Jones", "Batch A", "I-001", "Drill: 95, Academics: 88", "Excellent performance."));
            DataPersistenceManager.saveObjects(new ArrayList<>(masterEvaluationList), EVALUATIONS_FILE);
            filterEvaluations();
        }
    }

    private void saveEvaluations() {
        DataPersistenceManager.saveObjects(masterEvaluationList.stream().collect(Collectors.toList()), EVALUATIONS_FILE);
    }

    private void filterEvaluations() {
        String selectedBatch = filterByBatchComboBox.getValue();

        List<Evaluation> filteredList = masterEvaluationList.stream()
                .filter(eval -> selectedBatch == null || selectedBatch.equals("All") || eval.getBatch().equals(selectedBatch))
                .collect(Collectors.toList());

        filterByListTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void approveBtnOnAction(ActionEvent actionEvent) {
        Evaluation selectedEvaluation = filterByListTableView.getSelectionModel().getSelectedItem();
        if (selectedEvaluation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Approve this evaluation?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                selectedEvaluation.setStatus("Approved");
                saveEvaluations();
                filterEvaluations();
                showAlert(Alert.AlertType.INFORMATION, "Approved", "Evaluation for " + selectedEvaluation.getCadetName() + " approved.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an evaluation to approve.");
        }
    }

    @FXML
    public void requestReEvaluationBtnOnAction(ActionEvent actionEvent) {
        Evaluation selectedEvaluation = filterByListTableView.getSelectionModel().getSelectedItem();
        if (selectedEvaluation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Request re-evaluation for this cadet?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                selectedEvaluation.setStatus("Re-evaluation Requested");
                saveEvaluations();
                filterEvaluations();
                showAlert(Alert.AlertType.INFORMATION, "Re-evaluation Requested", "Re-evaluation requested for " + selectedEvaluation.getCadetName() + ".");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an evaluation to request re-evaluation.");
        }
    }

    @FXML
    public void filterBtnOnAction(ActionEvent actionEvent) {
        filterEvaluations();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}