package edu.cit.soriano.pawwatch.feature.application.dto;

public class ApplicationStatusRequest {

    private String status;
    private String remarks;

    public ApplicationStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}