package bmasec2.bmaapplication.model;

import java.io.Serial;
import java.io.Serializable;

public class CadetRanking implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String cadetId;
    private String cadetName;
    private double overallScore;
    private String rank;
    private String recommendedFor;

    public CadetRanking(String cadetId, String cadetName, double overallScore, String rank, String recommendedFor) {
        this.cadetId = cadetId;
        this.cadetName = cadetName;
        this.overallScore = overallScore;
        this.rank = rank;
        this.recommendedFor = recommendedFor;
    }


    public String getCadetId() { return cadetId; }
    public String getCadetName() { return cadetName; }
    public double getOverallScore() { return overallScore; }
    public String getRank() { return rank; }
    public String getRecommendedFor() { return recommendedFor; }


    public void setCadetId(String cadetId) { this.cadetId = cadetId; }
    public void setCadetName(String cadetName) { this.cadetName = cadetName; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
    public void setRank(String rank) { this.rank = rank; }
    public void setRecommendedFor(String recommendedFor) { this.recommendedFor = recommendedFor; }

    @Override
    public String toString() {
        return "CadetRanking{" +
                "cadetId='" + cadetId + '\'' +
                ", cadetName='" + cadetName + '\'' +
                ", overallScore=" + overallScore +
                ", rank='" + rank + '\'' +
                ", recommendedFor='" + recommendedFor + '\'' +
                '}';
    }
}