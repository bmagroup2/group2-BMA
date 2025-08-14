package bmasec2.bmaapplication.fatema;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TrainingSession implements Serializable {
    private String sessionId;
    private String topic;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String instructorId;
    private String cadetBatch;
    private int maxParticipants;
    private String status; // e.g., Scheduled, Completed, Cancelled

    public TrainingSession(String sessionId, String topic, LocalDate date, LocalTime time, String location, String instructorId, String cadetBatch, int maxParticipants) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.location = location;
        this.instructorId = instructorId;
        this.cadetBatch = cadetBatch;
        this.maxParticipants = maxParticipants;
        this.status = "Scheduled";
    }

    // Getters
    public String getSessionId() {
        return sessionId;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public String getCadetBatch() {
        return cadetBatch;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public void setCadetBatch(String cadetBatch) {
        this.cadetBatch = cadetBatch;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


