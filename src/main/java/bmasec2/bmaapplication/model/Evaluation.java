package bmasec2.bmaapplication.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Evaluation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String evalId;
    private String cadetId;
    private String cadetName;
    private String batch;
    private String instructorId;
    private Date evaluationDate;
    private String scoreBreakdown;
    private String notes;
    private String status;

    public Evaluation(String evalId, String cadetId, String cadetName, String batch, String instructorId, String scoreBreakdown, String notes) {
        this.evalId = evalId;
        this.cadetId = cadetId;
        this.cadetName = cadetName;
        this.batch = batch;
        this.instructorId = instructorId;
        this.evaluationDate = new Date();
        this.scoreBreakdown = scoreBreakdown;
        this.notes = notes;
        this.status = "Pending";
    }

    public Evaluation(String evaluationId, String cadetId, String evaluatorId, String evaluationType, double score, double maxScore, String comments) {
    }


    public String getEvalId() { return evalId; }
    public String getCadetId() { return cadetId; }
    public String getCadetName() { return cadetName; }
    public String getBatch() { return batch; }
    public String getInstructorId() { return instructorId; }
    public Date getEvaluationDate() { return evaluationDate; }
    public String getScoreBreakdown() { return scoreBreakdown; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }


    public void setEvalId(String evalId) { this.evalId = evalId; }
    public void setCadetId(String cadetId) { this.cadetId = cadetId; }
    public void setCadetName(String cadetName) { this.cadetName = cadetName; }
    public void setBatch(String batch) { this.batch = batch; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    public void setEvaluationDate(Date evaluationDate) { this.evaluationDate = evaluationDate; }
    public void setScoreBreakdown(String scoreBreakdown) { this.scoreBreakdown = scoreBreakdown; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Evaluation{" +
                "evalId='" + evalId + '\'' +
                ", cadetId='" + cadetId + '\'' +
                ", cadetName='" + cadetName + '\'' +
                ", batch='" + batch + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", evaluationDate=" + evaluationDate +
                ", scoreBreakdown='" + scoreBreakdown + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public double getScore() {
        return 0;
    }

    public Object getSessionId() {
        return null;
    }

    public Object getEvaluatorId() {
        return null;
    }

    public Object getEvaluationType() {
        return null;
    }
}