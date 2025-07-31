package bmasec2.bmaapplication.model;

import java.util.Date;
import java.util.Map;


public class Report {
    private String reportId;
    private String type;
    private String generatedBy;
    private Date date;
    private Map<String, Object> content;


    public Report(String reportId, String type, String generatedBy, Map<String, Object> content) {
        this.reportId = reportId;
        this.type = type;
        this.generatedBy = generatedBy;
        this.date = new Date();
        this.content = content;
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

    public Date getDate() {
        return date;
    }

    public Map<String, Object> getContent() {
        return content;
    }
}
