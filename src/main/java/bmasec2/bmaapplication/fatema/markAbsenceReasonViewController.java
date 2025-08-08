package bmasec2.bmaapplication.fatema;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class markAbsenceReasonViewController
{
    @javafx.fxml.FXML
    private Label absenceDetailsLabel;
    @javafx.fxml.FXML
    private TextArea reasonForAbsenceTextArea;
    @javafx.fxml.FXML
    private TableView rercentUnexplainedAbsencesTableView;
    @javafx.fxml.FXML
    private TableColumn sessionTableColumn;
    @javafx.fxml.FXML
    private TableColumn dateTableColumn;
    @javafx.fxml.FXML
    private TableColumn cadetNameTableColumn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void saveReasonButtonOnAction(ActionEvent actionEvent) {
    }
}