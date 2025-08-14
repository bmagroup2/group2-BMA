package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.Announcement;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandantDashboardView {

    @FXML private TableView<Announcement> pendingAnnouncementsForApprovalListTableView;
    @FXML private Label activeCadetsInTrainingCountLabel;
    @FXML private Label pendingApprovalCountLabel;
    @FXML private TableColumn<Announcement, String> pendingAnnouncementTitleColumn;
    @FXML private TableColumn<Announcement, String> pendingAnnouncementSubmittedByColumn;
    @FXML private PieChart performanceDistributionPieChart;

    private static final String USERS_FILE = "users.dat";
    private static final String ANNOUNCEMENTS_FILE = "announcements.dat";

    @FXML
    public void initialize() {

        pendingAnnouncementTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        pendingAnnouncementSubmittedByColumn.setCellValueFactory(new PropertyValueFactory<>("authorId"));

        loadDashboardData();
    }

    private void loadDashboardData() {

        List<User> allUsers = DataPersistenceManager.loadObjects(USERS_FILE);
        long activeCadets = allUsers.stream()
                .filter(user -> user instanceof Cadet && user.getStatus().equals("Active"))
                .count();
        activeCadetsInTrainingCountLabel.setText(String.valueOf(activeCadets));


        List<Announcement> allAnnouncements = DataPersistenceManager.loadObjects(ANNOUNCEMENTS_FILE);
        ObservableList<Announcement> pendingAnnouncements = FXCollections.observableArrayList(
                allAnnouncements.stream()
                        .filter(ann -> ann.getStatus().equals("Pending"))
                        .collect(Collectors.toList())
        );
        pendingAnnouncementsForApprovalListTableView.setItems(pendingAnnouncements);
        pendingApprovalCountLabel.setText(String.valueOf(pendingAnnouncements.size()));

        Map<String, Long> statusCounts = allUsers.stream()
                .filter(user -> user instanceof Cadet)
                .map(user -> (Cadet) user)
                .collect(Collectors.groupingBy(Cadet::getStatus, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        statusCounts.forEach((status, count) -> {
            pieChartData.add(new PieChart.Data(status + " (" + count + ")", count));
        });

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No Cadet Data", 1));
        }
        performanceDistributionPieChart.setData(pieChartData);
        performanceDistributionPieChart.setTitle("Cadet Status Distribution");
    }
}