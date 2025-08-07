package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Report implements Serializable {
    private String reportId;
    private String type;
    private String generatedBy;
    private Date date;
    private Map<String, Object> content;
    private String unit;
    private String title;

    public Report(String reportId, String type, String generatedBy, Map<String, Object> content) {
        this.reportId = reportId;
        this.type = type;
        this.generatedBy = generatedBy;
        this.date = new Date();
        this.content = content;
    }

    public Report(String reportId, String unit, Date date, String title, String contentString) {
        this.reportId = reportId;
        this.unit = unit;
        this.date = date;
        this.title = title;
        this.content = new HashMap<>();
        this.content.put("mainContent", contentString);
        this.type = "Unit Report";
        this.generatedBy = "System";
    }


    public String getReportId() {
        return reportId;
    }

    public String getType() {
        return type;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public Date getReportDate() {
        return date;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public String getUnit() {
        return unit;
    }

    public String getTitle() {
        return title;
    }

    public void setReportId(String reportId) { this.reportId = reportId; }
    public void setType(String type) { this.type = type; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public void setDate(Date date) { this.date = date; }
    public void setContent(Map<String, Object> content) { this.content = content; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return "Report{" +
                "reportId='" + reportId + '\'' +
                ", type='" + type + '\'' +
                ", generatedBy='" + generatedBy + '\'' +
                ", date=" + date +
                ", content=" + content +
                ", unit='" + unit + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getContentString() {
        if (content != null && content.containsKey("mainContent")) {
            return content.get("mainContent").toString();
        }
        return "";
    }
}