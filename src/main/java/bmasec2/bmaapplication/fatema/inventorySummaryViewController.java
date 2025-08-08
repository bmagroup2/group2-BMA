package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class inventorySummaryViewController
{
    @javafx.fxml.FXML
    private TableColumn itemNameColumn;
    @javafx.fxml.FXML
    private ComboBox allCategoriesComboBox;
    @javafx.fxml.FXML
    private TableColumn quantityColumn;
    @javafx.fxml.FXML
    private TableColumn lastUpdateColumn;
    @javafx.fxml.FXML
    private TableView inventorySummaryTableView;
    @javafx.fxml.FXML
    private PieChart inventoryCategoryPieChart;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void viewButtonOnAction(ActionEvent actionEvent) {
    }
}