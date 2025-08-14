package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;
    private String announcementId;
    private String title;
    private String content;
    private String messOfficer;
    private String allCadets;
    private String authorId;
    private Date creationDate;
    private String status;

    public Announcement(String announcementId, String title, String content, String authorId) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.creationDate = new Date();
        this.status = "Pending";
    }

    public Announcement(String announcementId, String title, String content, String createdBy, Date date, String targetAudience, String pendingApproval) {
    }

    public Announcement(String announcementId, String title, String content, String messOfficer, String allCadets) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.messOfficer = messOfficer;
        this.allCadets = allCadets;
    }


    public String getAnnouncementId() { return announcementId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public Date getCreationDate() { return creationDate; }
    public String getStatus() { return status = "Pending"; }


    public void setAnnouncementId(String announcementId) { this.announcementId = announcementId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Announcement{" +
                "announcementId='" + announcementId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", messOfficer='" + messOfficer + '\'' +
                ", allCadets='" + allCadets + '\'' +
                ", authorId='" + authorId + '\'' +
                ", creationDate=" + creationDate +
                ", status='" + status + '\'' +
                '}';
    }
}

