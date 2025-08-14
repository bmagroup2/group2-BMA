package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class MealAttendance implements Serializable {
    private String attendanceId;
    private String cadetId;
    private Date mealDate;
    private String mealType;
    private boolean attended;
    private String notes;

    public MealAttendance(String attendanceId, String cadetId, Date mealDate, 
                         String mealType, boolean attended, String notes) {
        this.attendanceId = attendanceId;
        this.cadetId = cadetId;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.attended = attended;
        this.notes = notes;
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

    public Date getMealDate() {
        return mealDate;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MealAttendance{" +
                "attendanceId='" + attendanceId + '\'' +
                ", cadetId='" + cadetId + '\'' +
                ", mealDate=" + mealDate +
                ", mealType='" + mealType + '\'' +
                ", attended=" + attended +
                ", notes='" + notes + '\'' +
                '}';
    }
}

