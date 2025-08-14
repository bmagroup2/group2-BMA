package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private String notificationId;
    private String senderId;
    private String senderName;
    private String recipientRole; // e.g., Commandant
    private String subject;
    private String message;
    private LocalDateTime sentDate;
    private boolean isRead;
    private String relatedItemId; // Optional: for inventory shortages

    public Notification(String notificationId, String senderId, String senderName, String recipientRole, String subject, String message, String relatedItemId) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientRole = recipientRole;
        this.subject = subject;
        this.message = message;
        this.sentDate = LocalDateTime.now();
        this.isRead = false;
        this.relatedItemId = relatedItemId;
    }

    // Getters
    public String getNotificationId() {
        return notificationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getRelatedItemId() {
        return relatedItemId;
    }

    // Setters
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setRelatedItemId(String relatedItemId) {
        this.relatedItemId = relatedItemId;
    }
}


