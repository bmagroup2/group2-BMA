package bmasec2.bmaapplication.shanin;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CommandantDashboardView
{
    @javafx.fxml.FXML
    private TableView pendingAnnouncementsForApprovalListTableView;
    @javafx.fxml.FXML
    private Label activeCadetsInTrainingCountLabel;
    @javafx.fxml.FXML
    private Label pendingApprovalCountLabel;
    @javafx.fxml.FXML
    private TableColumn pendingAnnouncementTitleColumn;
    @javafx.fxml.FXML
    private TableColumn pendingAnnouncementSubmittedByColumn;
    @javafx.fxml.FXML
    private PieChart performanceDistributionPieChart;

    @javafx.fxml.FXML
    public void initialize() {
    }}