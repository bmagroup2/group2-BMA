package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Attendance implements Serializable {
    private String attendanceId;
    private String cadetId;
    private String sessionId;
    private Date date;
    private String status; // Present, Absent, Late
    private String reason;

    public Attendance(String attendanceId, String cadetId, String sessionId, Date date, String status) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.sessionId = sessionId;
        this.date = date;
        this.status = status;
        this.reason = "";
    }

    public Attendance(String attendanceId, String cadetId, String sessionId, Date date, String status, String reason) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.sessionId = sessionId;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
                ", sessionId='" + sessionId + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
