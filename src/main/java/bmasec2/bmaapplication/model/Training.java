package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Training implements Serializable {
    private String sessionId;
    private String topic;
    private Date dateTime;
    private String location;
    private String instructorId;
    private int maxParticipants;
    private List<String> registeredCadets;
    private String status;

    public Training(String sessionId, String topic, Date dateTime, String location, String instructorId, int maxParticipants) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.dateTime = dateTime;
        this.location = location;
        this.instructorId = instructorId;
        this.maxParticipants = maxParticipants;
        this.registeredCadets = new ArrayList<>();
        this.status = "Scheduled";
    }

    // Business methods
    public boolean register(String cadetId) {
        if (registeredCadets.size() < maxParticipants && !registeredCadets.contains(cadetId)) {
            registeredCadets.add(cadetId);
            return true;
        }
        return false;
    }

    public boolean cancel() {
        this.status = "Cancelled";
        return true;
    }

    public boolean schedule() {
        this.status = "Scheduled";
        return true;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<String> getRegisteredCadets() {
        return registeredCadets;
    }

    public void setRegisteredCadets(List<String> registeredCadets) {
        this.registeredCadets = registeredCadets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Training{" +
                "sessionId='" + sessionId + '\'' +
                ", topic='" + topic + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", registeredCadets=" + registeredCadets.size() +
                ", status='" + status + '\'' +
                '}';
    }
}
