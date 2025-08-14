package bmasec2.bmaapplication.fatema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TrainingMaterial implements Serializable {
    private String materialId;
    private String trainingSessionId;
    private String trainingTopic;
    private String targetBatch;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadDate;
    private String uploadedByUserId;

    public TrainingMaterial(String materialId, String trainingSessionId, String trainingTopic, String targetBatch, String fileName, String filePath, String uploadedByUserId) {
        this.materialId = materialId;
        this.trainingSessionId = trainingSessionId;
        this.trainingTopic = trainingTopic;
        this.targetBatch = targetBatch;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadDate = LocalDateTime.now();
        this.uploadedByUserId = uploadedByUserId;
    }

    // Getters
    public String getMaterialId() {
        return materialId;
    }

    public String getTrainingSessionId() {
        return trainingSessionId;
    }

    public String getTrainingTopic() {
        return trainingTopic;
    }

    public String getTargetBatch() {
        return targetBatch;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public String getUploadedByUserId() {
        return uploadedByUserId;
    }

    // Setters
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public void setTrainingSessionId(String trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }

    public void setTrainingTopic(String trainingTopic) {
        this.trainingTopic = trainingTopic;
    }

    public void setTargetBatch(String targetBatch) {
        this.targetBatch = targetBatch;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setUploadedByUserId(String uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }
}


