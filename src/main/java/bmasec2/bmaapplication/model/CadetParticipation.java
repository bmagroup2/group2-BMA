package bmasec2.bmaapplication.model;

public class CadetParticipation {
    private final String cadetName;
    private final String cadetId;
    private final String status;

    public CadetParticipation(String cadetName, String cadetId, String status) {
        this.cadetName = cadetName;
        this.cadetId = cadetId;
        this.status = status;
    }

    public String getCadetName() {
        return cadetName;
    }

    public String getCadetId() {
        return cadetId;
    }

    public String getStatus() {
        return status;
    }
}
