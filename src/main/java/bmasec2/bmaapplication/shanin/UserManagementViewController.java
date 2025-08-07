package bmasec2.bmaapplication.shanin;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UserManagementViewController
{
    @javafx.fxml.FXML
    private ComboBox filterByRoleComboBox;
    @javafx.fxml.FXML
    private TableColumn fullNameColumn;
    @javafx.fxml.FXML
    private TextField emailTextField;
    @javafx.fxml.FXML
    private ComboBox roleComboBox;
    @javafx.fxml.FXML
    private TableColumn userIdColumn;
    @javafx.fxml.FXML
    private TableColumn roleColumn;
    @javafx.fxml.FXML
    private TextField userIdTextField;
    @javafx.fxml.FXML
    private ComboBox statusComboBox;
    @javafx.fxml.FXML
    private TableColumn statusColumn;
    @javafx.fxml.FXML
    private TextField fullNameTextField;
    @javafx.fxml.FXML
    private TextField searchByNameTextfield;
    @javafx.fxml.FXML
    private TableView userListTableView;
    @javafx.fxml.FXML
    private TableColumn emailColumn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void crossBtnOnMouseClicked(Event event) {
    }

    @javafx.fxml.FXML
    public void deleteSelectedBtnOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void addNewUserBtnOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void submitBtnOnAction(ActionEvent actionEvent) {
    }
}