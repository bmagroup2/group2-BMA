package bmasec2.bmaapplication.fatema;

import bmasec2.bmaapplication.User;

import java.util.Date;

public class LogisticOfficer extends User {
    private String officerId;
    private String contactNumber;

    public LogisticOfficer(String userId, String name, String email, String password, String officerId, String contactNumber) {
        super(userId, name, email, password, "Logistic Officer");
        this.officerId = officerId;
        this.contactNumber = contactNumber;
    }


    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "LogisticOfficer{" +
                "officerId=\'" + officerId + '\'' +
                ", contactNumber=\'" + contactNumber + '\'' +
                ", userId=\'" + getUserId() + '\'' +
                ", name=\'" + getName() + '\'' +
                ", role=\'" + getRole() + '\'' +
                '}';
    }
}
