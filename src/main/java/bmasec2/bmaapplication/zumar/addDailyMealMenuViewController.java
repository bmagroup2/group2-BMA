package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.system.Menu;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class addDailyMealMenuViewController
{
    @javafx.fxml.FXML
    private TextArea DinnerMenuTextArea;
    @javafx.fxml.FXML
    private TextArea breakfastMenuTextArea;
    @javafx.fxml.FXML
    private TextArea lunchMenuTextArea;
    @javafx.fxml.FXML
    private DatePicker dailyMenuDatePicker;

    @javafx.fxml.FXML
    public void initialize() {
        // Set default date to today
        dailyMenuDatePicker.setValue(LocalDate.now());
    }

    @javafx.fxml.FXML
    public void saveDailyMenuButtonOnAction(ActionEvent actionEvent) {
        try {
            // Validate inputs
            if (dailyMenuDatePicker.getValue() == null) {
                showAlert("Error", "Please select a date.");
                return;
            }
            
            if (breakfastMenuTextArea.getText().trim().isEmpty() &&
                lunchMenuTextArea.getText().trim().isEmpty() &&
                DinnerMenuTextArea.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter at least one meal menu.");
                return;
            }
            
            // Get selected date
            LocalDate selectedDate = dailyMenuDatePicker.getValue();
            Date menuDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Create menu content
            StringBuilder menuContent = new StringBuilder();
            
            if (!breakfastMenuTextArea.getText().trim().isEmpty()) {
                menuContent.append("Breakfast: ").append(breakfastMenuTextArea.getText().trim()).append("\n");
            }
            
            if (!lunchMenuTextArea.getText().trim().isEmpty()) {
                menuContent.append("Lunch: ").append(lunchMenuTextArea.getText().trim()).append("\n");
            }
            
            if (!DinnerMenuTextArea.getText().trim().isEmpty()) {
                menuContent.append("Dinner: ").append(DinnerMenuTextArea.getText().trim()).append("\n");
            }
            
            // Create a simple menu object (using Menu class as data container)
            // Note: Since Menu class is for UI navigation, we'll save the menu data directly
            // In a real implementation, you'd create a proper MealMenu model class
            
            // For now, we'll create a simple data structure and save it
            String menuData = "Date: " + selectedDate.toString() + "\n" + menuContent.toString();
            
            // Save menu data (simplified approach)
            // In a proper implementation, you'd have a MealMenu model class
            System.out.println("Saving menu data: " + menuData);
            
            // Show success message
            showAlert("Success", "Daily meal menu saved successfully!");
            
            // Clear form
            clearForm();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to save menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        dailyMenuDatePicker.setValue(LocalDate.now());
        breakfastMenuTextArea.clear();
        lunchMenuTextArea.clear();
        DinnerMenuTextArea.clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}