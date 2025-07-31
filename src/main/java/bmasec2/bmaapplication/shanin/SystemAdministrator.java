package bmasec2.bmaapplication.shanin;

import bmasec2.bmaapplication.User;
import bmasec2.bmaapplication.afifa.Cadet;
import bmasec2.bmaapplication.model.AuditLog;
import bmasec2.bmaapplication.model.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SystemAdministrator extends User {
    private int adminLevel;
    private List<String> accessRights;

    public SystemAdministrator(String userId, String name, String email, String password, int adminLevel) {
        super(userId, name, email, "System Admin", password);
        this.adminLevel = adminLevel;
        this.accessRights = new ArrayList<>(List.of("full_control"));
    }


    public void manageUsers() {
        System.out.println(this.name + " is managing user accounts.");

    }


    public List<AuditLog> viewAuditLogs() {
        System.out.println(this.name + " is viewing audit logs.");
        return new ArrayList<>();
    }

    public void updateSystemConfig(Map<String, String> config) {
        System.out.println(this.name + " is updating system configuration.");

    }

    public List<Cadet> manageCadetList() {
        System.out.println(this.name + " is managing the cadet list.");

        return new ArrayList<>();
    }

    public List<Report> generateUserReports(String role) {
        System.out.println(this.name + " is generating reports for role: " + role);
        return new ArrayList<>();
    }
}
