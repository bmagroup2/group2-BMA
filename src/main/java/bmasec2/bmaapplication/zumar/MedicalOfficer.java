package bmasec2.bmaapplication.zumar;

import bmasec2.bmaapplication.User;

import java.util.Date;

public class MedicalOfficer extends User {
    private String officerId;
    private String medicalLicense;

    public MedicalOfficer(String userId, String name, String email, String password, String officerId, String medicalLicense) {
        super(userId, name, email,"Medical Officer", password );
        this.officerId = officerId;
        this.medicalLicense = medicalLicense;
    }


    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getMedicalLicense() {
        return medicalLicense;
    }

    public void setMedicalLicense(String medicalLicense) {
        this.medicalLicense = medicalLicense;
    }

    @Override
    public String toString() {
        return "MedicalOfficer{" +
                "officerId=\'" + officerId + '\'' +
                ", medicalLicense=\'" + medicalLicense + '\'' +
                ", userId=\'" + getUserId() + '\'' +
                ", name=\'" + getName() + '\'' +
                ", role=\'" + getRole() + '\'' +
                '}' +
                '\n';
    }
}


