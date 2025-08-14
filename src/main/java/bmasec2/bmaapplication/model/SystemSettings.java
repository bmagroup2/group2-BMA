package bmasec2.bmaapplication.model;

import java.io.Serializable;

public class SystemSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    private int systemId;
    private String applicationName, s, dutyRosterEntry, rosterEntry, messOfficer;
    private String applicationLogoPath;


    public SystemSettings() {

    }

    public SystemSettings(String s, String dutyRosterEntry, String rosterEntry, String messOfficer) {
        this.s = s;
        this.dutyRosterEntry = dutyRosterEntry;
        this.rosterEntry = rosterEntry;
        this.messOfficer = messOfficer;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationLogoPath() {
        return applicationLogoPath;
    }

    public void setApplicationLogoPath(String applicationLogoPath) {
        this.applicationLogoPath = applicationLogoPath;
    }



    @Override
    public String toString() {
        return "SystemSettings{" +
                "applicationName='" + applicationName + '\'' +
                ", applicationLogoPath='" + applicationLogoPath + '\'' +
                '}' +
                ';';
    }

    public String getSettingName() {
        return "";
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSettingValue() {
        return "";
    }
}

