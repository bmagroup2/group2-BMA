package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class cadetHealthHistoryViewController
{
    @javafx.fxml.FXML
    private TableColumn detailsColn;
    @javafx.fxml.FXML
    private TableView cadetHealthHistoryTableView;
    @javafx.fxml.FXML
    private ComboBox selectCadetComboBox;
    @javafx.fxml.FXML
    private TableColumn recordTypeColn;
    @javafx.fxml.FXML
    private TableColumn dateColn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void viewHistoryOnActionButton(ActionEvent actionEvent) {
    }
}