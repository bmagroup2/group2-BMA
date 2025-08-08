package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class PendingCasesViewController
{
    @javafx.fxml.FXML
    private Label caseDetailsLabel;
    @javafx.fxml.FXML
    private TableView pendingHealthCasesTableView;
    @javafx.fxml.FXML
    private TableColumn dateFlaggedColn;
    @javafx.fxml.FXML
    private TableColumn cadetNameColn;
    @javafx.fxml.FXML
    private TextArea statusRemarkTextArea;
    @javafx.fxml.FXML
    private TableColumn issueColn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void markAsResolvedOnActionButton(ActionEvent actionEvent) {
    }
}