package bmasec2.bmaapplication.zumar;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class emergencyMedicalRecordCasesViewController
{
    @javafx.fxml.FXML
    private TextArea diagnosisTextArea;
    @javafx.fxml.FXML
    private TableView emergencyCaseLogTableView;
    @javafx.fxml.FXML
    private TableColumn diagnosisColn;
    @javafx.fxml.FXML
    private TableColumn cadetNameColn;
    @javafx.fxml.FXML
    private ComboBox cadetComboBox;
    @javafx.fxml.FXML
    private TextArea symptomsTextArea;
    @javafx.fxml.FXML
    private TableColumn dateColn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void emergencyLogOnActionButton(ActionEvent actionEvent) {
    }
}