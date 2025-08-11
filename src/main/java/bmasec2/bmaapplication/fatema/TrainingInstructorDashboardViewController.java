package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.model.Feedback;
import bmasec2.bmaapplication.model.Evaluation;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TrainingInstructorDashboardViewController {
    @FXML
    private Label totalSessionsLabel;
    @FXML
    private Label upcomingSessionsLabel;
    @FXML
    private Label averageRatingLabel;
    @FXML
    private Label pendingEvaluationsLabel;

    @FXML
    private TableView<Training> upcomingSessionsTableView;
    @FXML
    private TableColumn<Training, String> sessionIdColumn;
    @FXML
    private TableColumn<Training, String> topicColumn;
    @FXML
    private TableColumn<Training, Date> dateTimeColumn;
    @FXML
    private TableColumn<Training, String> locationColumn;
    @FXML
    private TableColumn<Training, Integer> participantsColumn;

    @FXML
    private ListView<String> recentActivitiesListView;
    @FXML
    private ListView<String> quickActionsListView;

    private ObservableList<Training> upcomingSessionsData = FXCollections.observableArrayList();
    private ObservableList<String> recentActivitiesData = FXCollections.observableArrayList();
    private ObservableList<String> quickActionsData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupListViews();
        loadDashboardData();
        setupQuickActions();
    }

    private void setupTableColumns() {
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        participantsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getRegisteredCadets().size()).asObject());

        upcomingSessionsTableView.setItems(upcomingSessionsData);
    }

    private void setupListViews() {
        recentActivitiesListView.setItems(recentActivitiesData);
        quickActionsListView.setItems(quickActionsData);


        quickActionsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedAction = quickActionsListView.getSelectionModel().getSelectedItem();
                handleQuickAction(selectedAction);
            }
        });
    }

    private void loadDashboardData() {
        loadTrainingSessionsData();
        loadStatistics();
        loadRecentActivities();
    }

    private void loadTrainingSessionsData() {
        List<Training> allSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        String currentInstructorId = "INST-001";


        Date now = new Date();
        List<Training> upcomingSessions = allSessions.stream()
                .filter(session -> session.getInstructorId().equals(currentInstructorId))
                .filter(session -> session.getDateTime().after(now))
                .filter(session -> "Scheduled".equals(session.getStatus()))
                .sorted((s1, s2) -> s1.getDateTime().compareTo(s2.getDateTime()))
                .limit(5) // Show only next 5 sessions
                .collect(Collectors.toList());

        upcomingSessionsData.clear();
        upcomingSessionsData.addAll(upcomingSessions);
    }

    private void loadStatistics() {
        String currentInstructorId = "INST-001";

        List<Training> allSessions = DataPersistenceManager.loadObjects("training_sessions.bin");
        List<Training> instructorSessions = allSessions.stream()
                .filter(session -> session.getInstructorId().equals(currentInstructorId))
                .toList();


        totalSessionsLabel.setText(String.valueOf(instructorSessions.size()));

        Date now = new Date();
        long upcomingCount = instructorSessions.stream()
                .filter(session -> session.getDateTime().after(now))
                .filter(session -> "Scheduled".equals(session.getStatus()))
                .count();
        upcomingSessionsLabel.setText(String.valueOf(upcomingCount));

        // Average rating from feedback
        List<Feedback> allFeedback = DataPersistenceManager.loadObjects("feedback.bin");
        List<String> instructorSessionIds = instructorSessions.stream()
                .map(Training::getSessionId)
                .collect(Collectors.toList());

        List<Feedback> instructorFeedback = allFeedback.stream()
                .filter(feedback -> instructorSessionIds.contains(feedback.getSessionId()))
                .collect(Collectors.toList());

        if (!instructorFeedback.isEmpty()) {
            double avgRating = instructorFeedback.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);
            averageRatingLabel.setText(String.format("%.2f/5", avgRating));

            // Color code the rating
            if (avgRating >= 4.0) {
                averageRatingLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else if (avgRating >= 3.0) {
                averageRatingLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            } else {
                averageRatingLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        } else {
            averageRatingLabel.setText("N/A");
        }

        List<Evaluation> allEvaluations = DataPersistenceManager.loadObjects("evaluations.bin");
        long pendingCount = allEvaluations.stream()
                .filter(eval -> eval.getEvaluatorId().equals(currentInstructorId))
                .filter(eval -> "Pending".equals(eval.getStatus()))
                .count();
        pendingEvaluationsLabel.setText(String.valueOf(pendingCount));
    }

    private void loadRecentActivities() {
        recentActivitiesData.clear();

        recentActivitiesData.addAll(
                "Scheduled new training session: Advanced Tactics",
                "Submitted evaluation for Cadet CDT-001",
                "Updated training materials for Weapons Training",
                "Marked attendance for Physical Training session",
                "Posted announcement about upcoming drill",
                "Reviewed feedback from Leadership Workshop",
                "Completed monthly training report"
        );
    }

    private void setupQuickActions() {
        quickActionsData.clear();
        quickActionsData.addAll(
                "Take Attendance",
                "Schedule New Training",
                "Submit Evaluation",
                "View Feedback",
                "Update Materials",
                "Post Announcement",
                "View Training History",
                "Mark Absence Reason"
        );
    }

    private void handleQuickAction(String action) {
        if (action == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quick Action");
        alert.setHeaderText("Action: " + action);

        switch (action) {
            case "Take Attendance":
                alert.setContentText("This would open the Take Attendance view.");
                break;
            case "Schedule New Training":
                alert.setContentText("This would open the Schedule Training view.");
                break;
            case "Submit Evaluation":
                alert.setContentText("This would open the Submit Evaluation view.");
                break;
            case "View Feedback":
                alert.setContentText("This would open the View Feedback view.");
                break;
            case "Update Materials":
                alert.setContentText("This would open the Update Materials view.");
                break;
            case "Post Announcement":
                alert.setContentText("This would open the Post Announcement view.");
                break;
            case "View Training History":
                alert.setContentText("This would open the Training History view.");
                break;
            case "Mark Absence Reason":
                alert.setContentText("This would open the Mark Absence Reason view.");
                break;
            default:
                alert.setContentText("Action not implemented yet.");
        }

        alert.showAndWait();
    }

    @FXML
    public void refreshDashboardButtonOnAction() {
        loadDashboardData();
        showAlert("Refresh", "Dashboard data has been refreshed.");
    }

    @FXML
    public void viewAllSessionsButtonOnAction() {
        showAlert("View All Sessions", "This would open a detailed view of all training sessions.");
    }

    @FXML
    public void generateReportButtonOnAction() {
        showAlert("Generate Report", "This would open the report generation interface.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
