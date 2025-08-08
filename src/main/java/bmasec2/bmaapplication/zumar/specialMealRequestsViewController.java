package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class specialMealRequestsViewController
{
    @javafx.fxml.FXML
    private TableView activeSpecialRequestsTableView;
    @javafx.fxml.FXML
    private ComboBox selectCadetComboBox;
    @javafx.fxml.FXML
    private TableColumn cadetNameColn;
    @javafx.fxml.FXML
    private TextArea requestDetailsTextArea;
    @javafx.fxml.FXML
    private TableColumn requestDetailsColn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void saveRequestOnActionButton(ActionEvent actionEvent) {
    }
}