package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.model.MissionEvent;
import bmasec2.bmaapplication.system.DataPersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MissionEventsViewController {

    @FXML private TableColumn<MissionEvent, String> eventNameColumn;
    @FXML private TextField eventNameTextField;
    @FXML private DatePicker eventDateDatePicker;
    @FXML private TableView<MissionEvent> upcomingAndPastEventsTableView;
    @FXML private TableColumn<MissionEvent, LocalDate> eventDateColumn;
    @FXML private TextField descriptionTextField;

    private ObservableList<MissionEvent> masterEventList;
    private static final String EVENTS_FILE = "mission_events.dat";

    @FXML
    public void initialize() {

        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));


        loadEvents();

        upcomingAndPastEventsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                eventNameTextField.setText(newSelection.getEventName());
                eventDateDatePicker.setValue(newSelection.getEventDate());
                descriptionTextField.setText(newSelection.getDescription());
            } else {
                clearFormFields();
            }
        });
    }

    private void loadEvents() {
        List<MissionEvent> loadedEvents = DataPersistenceManager.loadObjects(EVENTS_FILE);
        masterEventList = FXCollections.observableArrayList(loadedEvents);
        upcomingAndPastEventsTableView.setItems(masterEventList);

        // Add dummy data if the file is empty for testing
        if (masterEventList.isEmpty()) {
            masterEventList.add(new MissionEvent(UUID.randomUUID().toString(), "Annual Drill Competition", LocalDate.of(2025, 9, 15), "Inter-batch drill competition."));
            masterEventList.add(new MissionEvent(UUID.randomUUID().toString(), "Cadet Induction Ceremony", LocalDate.of(2025, 8, 1), "Official welcome for new cadets."));
            masterEventList.add(new MissionEvent(UUID.randomUUID().toString(), "Leadership Workshop", LocalDate.of(2025, 10, 5), "Workshop for senior cadets."));
            DataPersistenceManager.saveObjects(masterEventList.stream().collect(Collectors.toList()), EVENTS_FILE);
            upcomingAndPastEventsTableView.setItems(masterEventList); // Refresh table after adding dummy data
        }
    }

    private void saveEvents() {
        DataPersistenceManager.saveObjects(masterEventList.stream().collect(Collectors.toList()), EVENTS_FILE);
    }

    @FXML
    public void deleteBtnOnAction(ActionEvent actionEvent) {
        MissionEvent selectedEvent = upcomingAndPastEventsTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + selectedEvent.getEventName() + "?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                masterEventList.remove(selectedEvent);
                saveEvents();
                clearFormFields();
                showAlert(AlertType.INFORMATION, "Event Deleted", "Event " + selectedEvent.getEventName() + " deleted successfully.");
            }
        } else {
            showAlert(AlertType.WARNING, "No Event Selected", "Please select an event to delete.");
        }
    }

    @FXML
    public void saveEventBtnOnAction(ActionEvent actionEvent) {
        String eventName = eventNameTextField.getText();
        LocalDate eventDate = eventDateDatePicker.getValue();
        String description = descriptionTextField.getText();

        if (eventName.isEmpty() || eventDate == null || description.isEmpty()) {
            showAlert(AlertType.ERROR, "Missing Information", "Please fill in all event details.");
            return;
        }

        MissionEvent selectedEvent = upcomingAndPastEventsTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            // Add new event
            String newEventId = UUID.randomUUID().toString();
            MissionEvent newEvent = new MissionEvent(newEventId, eventName, eventDate, description);
            masterEventList.add(newEvent);
            showAlert(AlertType.INFORMATION, "Event Added", "New event " + eventName + " added successfully.");
        } else {

            selectedEvent.setEventName(eventName);
            selectedEvent.setEventDate(eventDate);
            selectedEvent.setDescription(description);
            upcomingAndPastEventsTableView.refresh();
            showAlert(AlertType.INFORMATION, "Event Updated", "Event " + eventName + " updated successfully.");
        }
        saveEvents();
        clearFormFields();
    }

    private void clearFormFields() {
        eventNameTextField.setText("");
        eventDateDatePicker.setValue(null);
        descriptionTextField.setText("");
        upcomingAndPastEventsTableView.getSelectionModel().clearSelection();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}