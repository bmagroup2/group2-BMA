package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ViewRoutineViewController {
    @FXML
    private TableColumn<Training, String> tranningactivitycolumn;
    @FXML
    private TableView<Training> trainingscheduletableview;
    @FXML
    private TableColumn<Training, String> traninglocationcolumn;
    @FXML
    private TableColumn<Training, String> tranningtimecolumn;

    private Cadet loggedInCadet;
    private ObservableList<Training> routineList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tranningactivitycolumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        traninglocationcolumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        tranningtimecolumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        trainingscheduletableview.setItems(routineList);
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;

        loadDailyRoutine();
    }

    @FXML
    public void todayroutineonaction(ActionEvent actionEvent) {
        loadDailyRoutine();
    }

    @FXML
    public void weeklyroutineonaction(ActionEvent actionEvent) {
        loadWeeklyRoutine();
    }

    private void loadDailyRoutine() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.bin");
        LocalDate today = LocalDate.now();

        List<Training> dailyRoutine = allTrainings.stream()
                .filter(training -> training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
                .collect(Collectors.toList());

        routineList.setAll(dailyRoutine);
    }

    private void loadWeeklyRoutine() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.bin");
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<Training> weeklyRoutine = allTrainings.stream()
                .filter(training -> {
                    LocalDate trainingDate = training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return (trainingDate.isAfter(startOfWeek.minusDays(1)) && trainingDate.isBefore(endOfWeek.plusDays(1)));
                })
                .collect(Collectors.toList());

        routineList.setAll(weeklyRoutine);
    }
}


