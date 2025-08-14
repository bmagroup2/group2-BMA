package bmasec2.bmaapplication.model;

import java.io.Serializable;

public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;
    private String instructorId;
    private String name;
    private String specialization;

    public Instructor(String instructorId, String name, String specialization) {
        this.instructorId = instructorId;
        this.name = name;
        this.specialization = specialization;
    }

    public String getInstructorId() { return instructorId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return name + " (" + instructorId + ")";
    }
}

