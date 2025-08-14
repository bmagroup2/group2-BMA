package bmasec2.bmaapplication.afifa;

import bmasec2.bmaapplication.User;

import java.util.Date;

public class CadetSupervisor extends User {
    private String supervisorId;
    private String department;

    public CadetSupervisor(String userId, String name, String email, String password, String supervisorId, String department) {
        super(userId, name, email,"Cadet Supervisor", password );
        this.supervisorId = supervisorId;
        this.department = department;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "CadetSupervisor{" +
                "supervisorId=\'" + supervisorId + '\'' +
                ", department=\'" + department + '\'' +
                ", userId=\'" + getUserId() + '\'' +
                ", name=\'" + getName() + '\'' +
                ", role=\'" + getRole() + '\'' +
                '}'+
                '\n';
    }
}

