package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.AuditLog;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemAdminDashBoardViewController {

    @FXML private TableColumn<AuditLog, String> actionColumn;
    @FXML private Label totalOfficersLabel;
    @FXML private Label totalUsersLabel;
    @FXML private BarChart<String, Number> userDistributionChart;
    @FXML private Label totalCadetsLabel;
    @FXML private TableColumn<AuditLog, String> userColumn;
    @FXML private Label totalInstructorsLabel;
    @FXML private TableView<AuditLog> recentActivityTable;
    @FXML private TableColumn<AuditLog, Date> timestampColumn;

    private static final String USERS_FILE = "users.bin";
    private static final String AUDIT_LOGS_FILE = "auditlogs.bin";

    @FXML
    public void initialize() {

        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        List<AuditLog> allLogs = DataPersistenceManager.loadObjects(AUDIT_LOGS_FILE);


        totalUsersLabel.setText(String.valueOf(allUsers.size()));

        long cadetCount = allUsers.stream().filter(u -> "Cadet".equals(u.getRole())).count();
        totalCadetsLabel.setText(String.valueOf(cadetCount));

        long instructorCount = allUsers.stream().filter(u -> "Training Instructor".equals(u.getRole())).count();
        totalInstructorsLabel.setText(String.valueOf(instructorCount));

        long officerCount = allUsers.stream().filter(u -> u.getRole().endsWith("Officer")).count();
        totalOfficersLabel.setText(String.valueOf(officerCount));


        populateUserDistributionChart(allUsers);


        populateRecentActivityTable(allLogs);
    }

    private void populateUserDistributionChart(List<User> users) {
        // Group users by role and count them
        Map<String, Long> userCountsByRole = users.stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Roles");

        userCountsByRole.forEach((role, count) -> {
            series.getData().add(new XYChart.Data<>(role, count));
        });

        userDistributionChart.getData().add(series);
        userDistributionChart.setLegendVisible(false);
    }

    private void populateRecentActivityTable(List<AuditLog> logs) {

        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));


        int logSize = logs.size();
        List<AuditLog> recentLogs = logs.subList(Math.max(0, logSize - 10), logSize);

        ObservableList<AuditLog> observableLogs = FXCollections.observableArrayList(recentLogs);
        recentActivityTable.setItems(observableLogs);


        timestampColumn.setSortType(TableColumn.SortType.DESCENDING);
        recentActivityTable.getSortOrder().add(timestampColumn);
        recentActivityTable.sort();
    }
}