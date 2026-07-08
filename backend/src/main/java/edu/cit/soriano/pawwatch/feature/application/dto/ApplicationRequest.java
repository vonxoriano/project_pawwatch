package edu.cit.soriano.pawwatch.feature.application.dto;

public class ApplicationRequest {

    private Long animalId;
    private String remarks;

    public ApplicationRequest() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
