package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class foodStockSummaryViewController
{
    @javafx.fxml.FXML
    private TableColumn statusColn;
    @javafx.fxml.FXML
    private TableColumn currentStockColn;
    @javafx.fxml.FXML
    private ComboBox filterByCategoryComboBox;
    @javafx.fxml.FXML
    private TextField searchItemTextField;
    @javafx.fxml.FXML
    private TableColumn categoryColn;
    @javafx.fxml.FXML
    private TableView foodStockInventoryTableView;
    @javafx.fxml.FXML
    private TableColumn itemNameColn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void SearchOnActionButton(ActionEvent actionEvent) {
    }
}