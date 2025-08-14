package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Instructor;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MyInstructorsViewController {
    @FXML
    private TableView<Instructor> instructorstableview;
    @FXML
    private TableColumn<Instructor, String> instructornamecolumn;
    @FXML
    private TableColumn<Instructor, String> instructorcontactinfocolumn;
    @FXML
    private TableColumn<Instructor, String> instructorsuborfieldcolumn;

    private Cadet loggedInCadet;

    @FXML
    public void initialize() {
        instructornamecolumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        instructorcontactinfocolumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        instructorsuborfieldcolumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        loadInstructors();
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
        loadInstructors();
    }

    private void loadInstructors() {
        List<Instructor> allInstructors = DataPersistenceManager.loadObjects("instructors.dat");
        ObservableList<Instructor> instructors = FXCollections.observableArrayList();


        instructors.addAll(allInstructors);

        instructorstableview.setItems(instructors);
    }
}


