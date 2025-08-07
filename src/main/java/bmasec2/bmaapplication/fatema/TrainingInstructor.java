package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.User;

import java.util.Date;

public class TrainingInstructor extends User {
    private String instructorId;
    private String specialization;

    public TrainingInstructor(String userId, String name, String email, String password, String instructorId, String specialization) {
        super(userId, name, email, password, "Training Instructor");
        this.instructorId = instructorId;
        this.specialization = specialization;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "TrainingInstructor{" +
                "instructorId='" + instructorId + '\'' +
                ", specialization='" + specialization + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
