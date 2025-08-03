package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectWriterController
{
    @javafx.fxml.FXML
    private TableColumn<String, UserTest> usernameColumn;
    @javafx.fxml.FXML
    private TableColumn<String, UserTest> passColumn;
    @javafx.fxml.FXML
    private TextField usernameTF;
    @javafx.fxml.FXML
    private TextField passTF;
    @javafx.fxml.FXML
    private TableView<UserTest> userTestTableView;
    private String username, password;
    public final static String filename = "data.bin";
    @javafx.fxml.FXML
    private Label labelSave;

    @javafx.fxml.FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    @javafx.fxml.FXML
    public void addBtnOnAction(ActionEvent actionEvent) {
        UserTest u = new UserTest(usernameTF.getText(), passTF.getText());

    }

    @javafx.fxml.FXML
    public void loadBtnOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void saveBtnOnAction(ActionEvent actionEvent) {
        try(ObjectOutputStream stream = new ObjectOutputStream(
                new FileOutputStream(filename)
        )) {

            for (UserTest u : userTestTableView.getItems()) {
                stream.writeObject(u);
            }
            labelSave.setText("Saved to binary file: " + filename);

        } catch (IOException e ) {
            labelSave.setText("Something Error: " + filename);

        }

    }
}