package bmasec2.bmaapplication.model;

import java.io.Serializable;
import java.util.Date;

public class SpecialMealRequest implements Serializable {
    private String selectCadet;
    private String requestDetails;
    private Date requestDate;
    private String status;

    public SpecialMealRequest(String selectCadet, String requestDetails, String status) {
        this.selectCadet = selectCadet;
        this.requestDetails = requestDetails;
        this.status = status;
    }

    public SpecialMealRequest(String string, String cadetId, Date requestDate, String requestDetails, String pending) {
        this.requestDate = requestDate;
    }

    public String getSelectCadet() {
        return selectCadet;
    }

    public void setSelectCadet(String selectCadet) {
        this.selectCadet = selectCadet;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    public String getStatus() {
        return status = "Pending";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SpecialMealRequest{" +
                "selectCadet='" + selectCadet + '\'' +
                ", requestDetails='" + requestDetails + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
