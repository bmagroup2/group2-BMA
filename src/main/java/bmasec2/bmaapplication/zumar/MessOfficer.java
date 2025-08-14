package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.User;

import java.util.Date;

public class MessOfficer extends User {
    private String officerId;
    private String shift;

    public MessOfficer(String userId, String name, String email, String password, String officerId, String shift) {
        super(userId, name, email,"Mess Officer", password );
        this.officerId = officerId;
        this.shift = shift;
    }


    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "MessOfficer{" +
                "officerId=\'" + officerId + '\'' +
                ", shift=\'" + shift + '\'' +
                ", userId=\'" + getUserId() + '\'' +
                ", name=\'" + getName() + '\'' +
                ", role=\'" + getRole() + '\'' +
                '}' +
                '\n';
    }
}


