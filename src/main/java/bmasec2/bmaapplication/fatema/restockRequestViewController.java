package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class restockRequestViewController
{
    @javafx.fxml.FXML
    private Label itemNameLabel;
    @javafx.fxml.FXML
    private ListView itemsWithLowStockListView;
    @javafx.fxml.FXML
    private TextField quantityToRequestTextField;
    @javafx.fxml.FXML
    private TextArea remarksTextArea;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void submitRestockRequestButtonOnAction(ActionEvent actionEvent) {
    }
}