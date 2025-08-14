package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.system.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class messOfficerDashboardViewController
{
    @javafx.fxml.FXML
    private Label lowFoodStockItemsLabel;
    @javafx.fxml.FXML
    private Label lunchAttendanceLabel;
    @javafx.fxml.FXML
    private ListView<String> todaysMenuListView;

    @javafx.fxml.FXML
    public void initialize() {
        loadDashboardData();
    }
    
    private void loadDashboardData() {

        List<Menu> menus = DataPersistenceManager.loadObjects("meal_menus.dat");
        

        lunchAttendanceLabel.setText("145 / 150");
        lowFoodStockItemsLabel.setText("3 Items");
        

        ObservableList<String> menuItems = FXCollections.observableArrayList();
        

        menuItems.add("Breakfast: Rice, Dal, Vegetables");
        menuItems.add("Lunch: Biriyani, Chicken Curry, Salad");
        menuItems.add("Dinner: Rice, Fish Curry, Mixed Vegetables");
        
        if (menuItems.isEmpty()) {
            menuItems.add("No menu items for today");
        }
        
        todaysMenuListView.setItems(menuItems);
    }
}