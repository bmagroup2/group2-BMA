package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Attendance implements Serializable {
    private String attendanceId;
    private String cadetId;
    private String cadetName;
    private String sessionId;
    private String sessionName;
    private LocalDate date;
    private String status; 
    private String reason;

    public Attendance(String attendanceId, String cadetId, String cadetName, String sessionId, String sessionName, LocalDate date, String status) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.cadetName = cadetName;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.date = date;
        this.status = status;
        this.reason = "";
    }

    public Attendance(String attendanceId, String cadetId, String cadetName, String sessionId, String sessionName, LocalDate date, String status, String reason) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.cadetName = cadetName;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }

    public Attendance(String attendanceId, String cadetId, String cadetName, String sessionName, String status) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.cadetName = cadetName;
        this.sessionName = sessionName;
        this.status = status;
        this.date = LocalDate.now();
    }

    public Attendance(String attendanceId, String userId, Date attendanceDate, String absent, String mealType) {
        this.date = null;
    }


    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getCadetId() {
        return cadetId;
    }

    public void setCadetId(String cadetId) {
        this.cadetId = cadetId;
    }

    public String getCadetName() {
        return cadetName;
    }

    public void setCadetName(String cadetName) {
        this.cadetName = cadetName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId='" + attendanceId + '\'' +
                ", cadetId='" + cadetId + '\'' +
                ", cadetName='" + cadetName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}


