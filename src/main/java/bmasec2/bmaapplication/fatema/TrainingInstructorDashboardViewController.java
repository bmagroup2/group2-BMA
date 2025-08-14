package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TrainingInstructorDashboardViewController {

    @FXML
    private Label nextSessionLabel;
    @FXML
    private Label attendanceStatusLabel;
    @FXML
    private ListView<TrainingSession> todaysScheduleSessionListView;
    @FXML
    private TableView<TrainingSession> upcomingSessionsTableView;
    @FXML
    private TableColumn<TrainingSession, String> sessionIdColumn;
    @FXML
    private TableColumn<TrainingSession, String> topicColumn;
    @FXML
    private TableColumn<TrainingSession, String> dateTimeColumn;
    @FXML
    private TableColumn<TrainingSession, String> locationColumn;
    @FXML
    private TableColumn<TrainingSession, Integer> participantsColumn;

    private static final String TRAINING_SESSIONS_FILE = "training_sessions.ser";
    private static final String ATTENDANCE_FILE = "attendance.ser";

    private ObservableList<TrainingSession> allTrainingSessions;
    private ObservableList<Attendance> allAttendanceRecords;

    @FXML
    public void initialize() {
        // Initialize table columns for upcomingSessionsTableView
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants")); // Assuming maxParticipants represents total participants

        // Custom cell factory for dateTimeColumn
        dateTimeColumn.setCellValueFactory(cellData -> {
            TrainingSession session = cellData.getValue();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String dateTime = session.getDate().format(dateFormatter) + " " + session.getTime().format(timeFormatter);
            return new javafx.beans.binding.SimpleStringProperty(dateTime);
        });

        loadData();
        updateDashboard();
    }

    private void loadData() {
        allTrainingSessions = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(TRAINING_SESSIONS_FILE));
        allAttendanceRecords = FXCollections.observableArrayList(DataPersistenceManager.loadObjects(ATTENDANCE_FILE));
    }

    private void updateDashboard() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Filter for today's sessions
        List<TrainingSession> todaysSessions = allTrainingSessions.stream()
                .filter(session -> session.getDate().equals(today))
                .sorted((s1, s2) -> s1.getTime().compareTo(s2.getTime()))
                .collect(Collectors.toList());

        // Update Today's Scheduled Sessions ListView
        todaysScheduleSessionListView.setItems(FXCollections.observableArrayList(todaysSessions));
        todaysScheduleSessionListView.setCellFactory(param -> new javafx.scene.control.ListCell<TrainingSession>() {
            @Override
            protected void updateItem(TrainingSession item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s (%s)",
                            item.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            item.getTopic(),
                            item.getCadetBatch()));
                }
            }
        });

        // Update Next Session Label
        TrainingSession nextSession = todaysSessions.stream()
                .filter(session -> session.getTime().isAfter(now))
                .findFirst()
                .orElse(null);

        if (nextSession != null) {
            nextSessionLabel.setText(String.format("%s - %s (%s)",
                    nextSession.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    nextSession.getTopic(),
                    nextSession.getCadetBatch()));
        } else {
            nextSessionLabel.setText("No upcoming sessions today");
        }

        // Update Attendance Status Label
        // This is a simplified check. In a real app, you'd check if attendance has been taken for the most recent past session.
        TrainingSession mostRecentPastSession = todaysSessions.stream()
                .filter(session -> session.getTime().isBefore(now))
                .reduce((first, second) -> second) // Get the last element
                .orElse(null);

        if (mostRecentPastSession != null) {
            boolean attendanceTaken = allAttendanceRecords.stream()
                    .anyMatch(a -> a.getSessionId().equals(mostRecentPastSession.getSessionId()) && a.getDate().equals(today));
            if (attendanceTaken) {
                attendanceStatusLabel.setText("Attendance recorded for " + mostRecentPastSession.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " Session");
                attendanceStatusLabel.setStyle("-fx-text-fill: #4CAF50;"); // Green color
            } else {
                attendanceStatusLabel.setText("Pending for " + mostRecentPastSession.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " Session");
                attendanceStatusLabel.setStyle("-fx-text-fill: #c62828;"); // Red color
            }
        } else {
            attendanceStatusLabel.setText("No past sessions today");
            attendanceStatusLabel.setStyle("-fx-text-fill: #546e7a;");
        }

        // Update Upcoming Sessions TableView (excluding today's sessions)
        List<TrainingSession> futureSessions = allTrainingSessions.stream()
                .filter(session -> session.getDate().isAfter(today))
                .sorted((s1, s2) -> {
                    int dateCompare = s1.getDate().compareTo(s2.getDate());
                    if (dateCompare == 0) {
                        return s1.getTime().compareTo(s2.getTime());
                    }
                    return dateCompare;
                })
                .collect(Collectors.toList());
        upcomingSessionsTableView.setItems(FXCollections.observableArrayList(futureSessions));
    }
}


