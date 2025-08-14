package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class VaccinationRecord implements Serializable {
    private String recordId;
    private String cadetId;
    private String vaccineName;
    private Date vaccinationDate;
    private Date nextDueDate;
    private String administeredBy;
    private String batchNumber;
    private String status;
    private String notes;

    public VaccinationRecord(String recordId, String cadetId, String vaccineName, 
                           Date vaccinationDate, Date nextDueDate, String administeredBy, 
                           String batchNumber, String status, String notes) {
        this.recordId = recordId;
        this.cadetId = cadetId;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.nextDueDate = nextDueDate;
        this.administeredBy = administeredBy;
        this.batchNumber = batchNumber;
        this.status = status;
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

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Date getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(Date vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public Date getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "VaccinationRecord{" +
                "recordId='" + recordId + '\'' +
                ", cadetId='" + cadetId + '\'' +
                ", vaccineName='" + vaccineName + '\'' +
                ", vaccinationDate=" + vaccinationDate +
                ", nextDueDate=" + nextDueDate +
                ", administeredBy='" + administeredBy + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

