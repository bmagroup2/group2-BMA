package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.CadetRanking;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CadetRankingViewController {

    @FXML private TableColumn<CadetRanking, String> rankColumn;
    @FXML private ComboBox<String> filterByMedalCategoryComboBox;
    @FXML private TableView<CadetRanking> cadetRankingListTableView;
    @FXML private TableColumn<CadetRanking, Double> overallScoreColumn;
    @FXML private TableColumn<CadetRanking, String> cadetIdColumn;
    @FXML private TableColumn<CadetRanking, String> cadetNameColumn;
    @FXML private TableColumn<CadetRanking, String> recommendedForColumn;
    @FXML private ComboBox<String> filterBySemesterComboBox;

    private ObservableList<CadetRanking> masterRankingList;
    private static final String RANKINGS_FILE = "cadet_rankings.dat";

    @FXML
    public void initialize() {

        cadetIdColumn.setCellValueFactory(new PropertyValueFactory<>("cadetId"));
        cadetNameColumn.setCellValueFactory(new PropertyValueFactory<>("cadetName"));
        overallScoreColumn.setCellValueFactory(new PropertyValueFactory<>("overallScore"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        recommendedForColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedFor"));


        filterBySemesterComboBox.getItems().addAll("All", "Semester 1", "Semester 2", "Semester 3");
        filterBySemesterComboBox.setValue("All");
        filterByMedalCategoryComboBox.getItems().addAll("All", "Gold", "Silver", "Bronze", "Commendation");
        filterByMedalCategoryComboBox.setValue("All");

        loadRankings();


        filterBySemesterComboBox.setOnAction(event -> filterRankings());
        filterByMedalCategoryComboBox.setOnAction(event -> filterRankings());
    }

    private void loadRankings() {
        List<CadetRanking> loadedRankings = DataPersistenceManager.loadObjects(RANKINGS_FILE);
        masterRankingList = FXCollections.observableArrayList(loadedRankings);

        if (masterRankingList.isEmpty()) {

            DataPersistenceManager.saveObjects(new ArrayList<>(masterRankingList), RANKINGS_FILE);
        }
        filterRankings();
    }

    private void filterRankings() {
        String selectedSemester = filterBySemesterComboBox.getValue();
        String selectedMedalCategory = filterByMedalCategoryComboBox.getValue();

        List<CadetRanking> filteredList = masterRankingList.stream()
                .filter(ranking -> selectedSemester.equals("All") || "Semester 1".equals(selectedSemester))
                .filter(ranking -> selectedMedalCategory.equals("All") || ranking.getRecommendedFor().equals(selectedMedalCategory))
                .collect(Collectors.toList());

        cadetRankingListTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void viewRankingsBtnOnAction(ActionEvent actionEvent) {
        filterRankings();
    }
}