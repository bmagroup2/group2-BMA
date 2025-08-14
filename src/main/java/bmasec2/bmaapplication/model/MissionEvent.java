package bmasec2.bmaapplication.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

public class MissionEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String eventId;
    private String eventName;
    private LocalDate eventDate;
    private String description;

    public MissionEvent(String eventId, String eventName, LocalDate eventDate, String description) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.description = description;
    }


    public String getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public LocalDate getEventDate() { return eventDate; }
    public String getDescription() { return description; }


    public void setEventId(String eventId) { this.eventId = eventId; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MissionEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", description='" + description + '\'' +
                '}';
    }

    public Collection<Object> getParticipants() {
        return java.util.List.of();
    }
}