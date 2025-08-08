package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class manageInventoryViewController
{
    @javafx.fxml.FXML
    private TableColumn statusTableColumn;
    @javafx.fxml.FXML
    private ComboBox filterByCategoryComboBox;
    @javafx.fxml.FXML
    private TableColumn quantityTableColumn;
    @javafx.fxml.FXML
    private TableColumn itemIdTableColumn;
    @javafx.fxml.FXML
    private TableView manageInventoryTableView;
    @javafx.fxml.FXML
    private TableColumn categoryTableColumn;
    @javafx.fxml.FXML
    private TextField selectItemNameTextField;
    @javafx.fxml.FXML
    private TableColumn itemNameTableColumn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void updateSelectedStockOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void searchButtonOnAction(ActionEvent actionEvent) {
    }
}