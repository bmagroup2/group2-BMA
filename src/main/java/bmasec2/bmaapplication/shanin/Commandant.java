package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.model.Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Commandant extends User {


    private String commandantId;
    private Date tenureStart;
    private String contactNumber;

    public Commandant(String userId, String name, String email, String password, String commandantId, String contactNumber) {
        super(userId, name, email, "Commandant", password);
        this.commandantId = commandantId;
        this.contactNumber = contactNumber;
        this.tenureStart = new Date();
    }



    public List<Report> viewReports() {
        System.out.println(this.name + " is viewing reports.");
        return new ArrayList<>();
    }

    public boolean approveEvaluations(String evalId, String decision) {
        System.out.println(this.name + " has made a decision: " + decision + " for evaluation ID: " + evalId);
        return true;
    }


    public boolean assignInstructors(String instructorId, String batch) {
        System.out.println(this.name + " is assigning instructor " + instructorId + " to batch " + batch);

        return true;
    }


    public boolean publishResults(String batch, String semester) {
        System.out.println(this.name + " is publishing results for batch " + batch + ", semester " + semester);

        return true;
    }


    public boolean createAnnouncement(String title, String message) {
        System.out.println(this.name + " is creating an announcement: '" + title + "'");
        return true;
    }
}
