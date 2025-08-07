package bmasec2.bmaapplication.shanin;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CadetRankingViewController
{
    @javafx.fxml.FXML
    private TableColumn rankColumn;
    @javafx.fxml.FXML
    private ComboBox filterByMedalCategoryComboBox;
    @javafx.fxml.FXML
    private TableView cadetRankingListTableView;
    @javafx.fxml.FXML
    private TableColumn overallScoreColumn;
    @javafx.fxml.FXML
    private TableColumn cadetIdColumn;
    @javafx.fxml.FXML
    private TableColumn cadetNameColumn;
    @javafx.fxml.FXML
    private TableColumn recommendedForColumn;
    @javafx.fxml.FXML
    private ComboBox filterBySemesterComboBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void viewRankingsBtnOnAction(ActionEvent actionEvent) {
    }
}