package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.Training;
import bmasec2.bmaapplication.model.Attendance;
import bmasec2.bmaapplication.model.Feedback;
import bmasec2.bmaapplication.model.Evaluation;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TrainingInstructor extends User {
    private String instructorId;
    private String specialization;
    private String department;
    private int yearsOfExperience;

    public TrainingInstructor(String userId, String name, String email, String password, String instructorId, String specialization) {
        super(userId, name, email, "Training Instructor", password);
        this.instructorId = instructorId;
        this.specialization = specialization;
        this.department = "Training";
        this.yearsOfExperience = 0;
    }


    public boolean takeAttendance(String sessionId, Map<String, String> attendanceData) {
        System.out.println("Attendance taken for session: " + sessionId);
        return true;
    }

    public boolean uploadMaterials(File file, String sessionId) {

        if (file != null && file.exists()) {
            System.out.println("Materials uploaded for session: " + sessionId);
            return true;
        }
        return false;
    }

    public boolean scheduleTraining(Map<String, Object> details) {

        System.out.println("Training session scheduled with details: " + details);
        return true;
    }

    public boolean evaluateCadets(Map<String, Object> evalData) {

        System.out.println("Cadet evaluation submitted: " + evalData);
        return true;
    }

    public List<Feedback> viewFeedback(String sessionId) {

        System.out.println("Viewing feedback for session: " + sessionId);
        return null;
    }

    public boolean postAnnouncement(String title, String message, List<String> targetCadets) {

        System.out.println("Announcement posted: " + title);
        return true;
    }

    public boolean markAbsenceReason(String cadetId, String sessionId, String reason) {

        System.out.println("Absence reason marked for cadet " + cadetId + " in session " + sessionId + ": " + reason);
        return true;
    }

    public List<Training> getTrainingHistory(String cadetId) {

        System.out.println("Retrieving training history for cadet: " + cadetId);
        return null;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toString() {
        return "TrainingInstructor{" +
                "instructorId='" + instructorId + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department='" + department + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
