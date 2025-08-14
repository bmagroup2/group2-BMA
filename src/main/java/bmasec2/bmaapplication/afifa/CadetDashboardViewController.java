package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class CadetDashboardViewController {

    @FXML
    private Label cddrillpracticelabel;
    @FXML
    private ListView<String> cdrecentannouncmentlistview;
    @FXML
    private Label cdpendingprogresslabel;
    @FXML
    private ListView<String> cdtodayroutinelistview;

    private Cadet loggedInCadet;

    @FXML
    public void initialize() {

        cddrillpracticelabel.setText("Next Drill: 07:00 AM - Parade Ground");
        cdpendingprogresslabel.setText("Pending Reports: 1");
    }

    public void initData(Cadet cadet) {
        this.loggedInCadet = cadet;
        if (loggedInCadet != null) {
            loadAnnouncements();
            loadDailyRoutine();
        }
    }

    private void loadAnnouncements() {
        List<Announcement> allAnnouncements = DataPersistenceManager.loadObjects("announcements.dat");
        ObservableList<String> recentAnnouncements = FXCollections.observableArrayList();


        for (Announcement ann : allAnnouncements) {
            recentAnnouncements.add(ann.getTitle() + ": " + ann.getContent());
        }
        cdrecentannouncmentlistview.setItems(recentAnnouncements);
    }

    private void loadDailyRoutine() {
        List<Training> allTrainings = DataPersistenceManager.loadObjects("trainings.dat");
        ObservableList<String> todayRoutine = FXCollections.observableArrayList();

        LocalDate today = LocalDate.now();

        List<Training> cadetsTrainingsToday = allTrainings.stream()
                .filter(training -> training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
                .filter(training -> training.getMaxParticipants() > 0)
                .collect(Collectors.toList());

        if (cadetsTrainingsToday.isEmpty()) {
            todayRoutine.add("No scheduled activities for today.");
        } else {
            for (Training training : cadetsTrainingsToday) {
                todayRoutine.add(training.getTopic() + " at " + training.getLocation() + " (" + training.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime() + ")");
            }
        }
        cdtodayroutinelistview.setItems(todayRoutine);
    }
}


