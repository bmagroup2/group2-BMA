package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Cadet extends User implements Serializable {

    private String batch;
    private String rank;
    private Date joinDate;
    private float performanceScore;
    private String medicalStatus;

    public Cadet(String userId, String name, String email, String password, String batch, String rank) {
        super(userId, name, email, "Cadet", password);
        this.batch = batch;
        this.rank = rank;
        this.joinDate = new Date();
        this.performanceScore = 0.0f;
        this.medicalStatus = "Fit";
    }

    public List<String> viewSchedule() {
        System.out.println(this.name + " is viewing their training schedule.");
        return Arrays.asList("09:00 - Drill", "14:00 - Marksmanship");
    }

    public boolean markAttendance(String sessionId, String status) {
        System.out.println(this.name + " is marking attendance for session " + sessionId + " as " + status);
        return true;
    }

    public boolean requestLeave(Date start, Date end, String reason) {
        System.out.println(this.name + " is requesting leave for: " + reason);
        return true;
    }

    public boolean submitReport(String content) {
        System.out.println(this.name + " is submitting a progress report.");
        return true;
    }

    public boolean registerTraining(String sessionId) {
        System.out.println(this.name + " is registering for training session " + sessionId);
        return true;
    }

    public Map<String, Float> viewPerformance() {
        System.out.println(this.name + " is viewing their performance scores.");
        return Map.of("Drill", 85.5f, "Academics", 92.0f);
    }

    public String getBatch() { return batch; }

    public void setBatch(String batch) { this.batch = batch; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    @Override
    public String toString() {
        return "Cadet{" +
                "batch='" + batch + '\'' +
                ", rank='" + rank + '\'' +
                ", joinDate=" + joinDate +
                ", performanceScore=" + performanceScore +
                ", medicalStatus='" + medicalStatus + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", lastLogin=" + lastLogin +
                ", status='" + status + '\'' +
                '}';
    }
}