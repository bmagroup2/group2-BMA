package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
    private String feedbackId;
    private String sessionId;
    private String cadetId;
    private int rating;
    private String comments;
    private Date submissionDate;
    private String feedbackType;

    public Feedback(String feedbackId, String sessionId, String cadetId, int rating, String comments) {
        this.feedbackId = feedbackId;
        this.sessionId = sessionId;
        this.cadetId = cadetId;
        this.rating = rating;
        this.comments = comments;
        this.submissionDate = new Date();
        this.feedbackType = "Training";
    }

    public Feedback(String feedbackId, String sessionId, String cadetId, int rating, String comments, String feedbackType) {
        this.feedbackId = feedbackId;
        this.sessionId = sessionId;
        this.cadetId = cadetId;
        this.rating = rating;
        this.comments = comments;
        this.submissionDate = new Date();
        this.feedbackType = feedbackType;
    }


    public boolean submit() {
        this.submissionDate = new Date();
        return true;
    }

    public boolean create() {
        this.submissionDate = new Date();
        return true;
    }


    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCadetId() {
        return cadetId;
    }

    public void setCadetId(String cadetId) {
        this.cadetId = cadetId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId='" + feedbackId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", cadetId='" + cadetId + '\'' +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", submissionDate=" + submissionDate +
                ", feedbackType='" + feedbackType + '\'' +
                '}';
    }

    public Object getCadetName() {
        return null;
    }

    public Date getDate() {
        return null;
    }
}
