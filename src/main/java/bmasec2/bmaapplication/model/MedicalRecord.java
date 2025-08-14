package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class MedicalRecord implements Serializable {
    private String recordId;
    private String cadetId;
    private Date date;
    private String diagnosis;
    private String treatment;
    private String notes;

    public MedicalRecord(String recordId, String cadetId, Date date, String diagnosis, String treatment, String notes) {
        this.recordId = recordId;
        this.cadetId = cadetId;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCadetId() {
        return cadetId;
    }

    public void setCadetId(String cadetId) {
        this.cadetId = cadetId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

