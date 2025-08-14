package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.system.DataPersistenceManager;
import bmasec2.bmaapplication.model.Menu;
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

        dailyMenuDatePicker.setValue(LocalDate.now());
    }

    @javafx.fxml.FXML
    public void saveDailyMenuButtonOnAction(ActionEvent actionEvent) {
        try {
            
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
            

            LocalDate selectedDate = dailyMenuDatePicker.getValue();
            Date menuDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            

            String menuId = "MENU_" + selectedDate.toString().replace("-", "");
            
            Menu newMenu = new Menu(
                menuId,
                menuDate,
                breakfastMenuTextArea.getText().trim(),
                lunchMenuTextArea.getText().trim(),
                DinnerMenuTextArea.getText().trim(),
                "Daily menu for " + selectedDate
            );
            
            DataPersistenceManager.addMenuAndSave(newMenu);
            

            showAlert("Success", "Daily meal menu saved successfully!");
            

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