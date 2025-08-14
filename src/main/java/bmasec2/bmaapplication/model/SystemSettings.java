package bmasec2.bmaapplication.model;

import java.io.Serializable;

public class SystemSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    private String applicationName;
    private String applicationLogoPath;

    public SystemSettings() {
        this.applicationName = "BMA Application";
        this.applicationLogoPath = null;
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
                "applicationName=\'" + applicationName + '\'' +
                ", applicationLogoPath=\'" + applicationLogoPath + '\'' +
                '}' +
                ';';
    }
}

