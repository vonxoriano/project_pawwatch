package edu.cit.soriano.pawwatch.feature.application.dto;

public class ApplicationRequest {

    private Long animalId;
    private String remarks;

    // Adoption questionnaire fields
    private String housingType; // "OWN" or "RENT"
    private Boolean hasLandlordPermission; // only relevant if renting
    private Boolean hasYard;
    private Integer householdMembers;
    private Boolean hasYoungChildren;
    private Boolean hasOtherPets;
    private String petExperience;
    private Integer hoursAwayDaily;
    private String reasonForAdopting;
    private Boolean agreesToReturnPolicy;

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

    public String getHousingType() {
        return housingType;
    }

    public void setHousingType(String housingType) {
        this.housingType = housingType;
    }

    public Boolean getHasLandlordPermission() {
        return hasLandlordPermission;
    }

    public void setHasLandlordPermission(Boolean hasLandlordPermission) {
        this.hasLandlordPermission = hasLandlordPermission;
    }

    public Boolean getHasYard() {
        return hasYard;
    }

    public void setHasYard(Boolean hasYard) {
        this.hasYard = hasYard;
    }

    public Integer getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(Integer householdMembers) {
        this.householdMembers = householdMembers;
    }

    public Boolean getHasYoungChildren() {
        return hasYoungChildren;
    }

    public void setHasYoungChildren(Boolean hasYoungChildren) {
        this.hasYoungChildren = hasYoungChildren;
    }

    public Boolean getHasOtherPets() {
        return hasOtherPets;
    }

    public void setHasOtherPets(Boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }

    public String getPetExperience() {
        return petExperience;
    }

    public void setPetExperience(String petExperience) {
        this.petExperience = petExperience;
    }

    public Integer getHoursAwayDaily() {
        return hoursAwayDaily;
    }

    public void setHoursAwayDaily(Integer hoursAwayDaily) {
        this.hoursAwayDaily = hoursAwayDaily;
    }

    public String getReasonForAdopting() {
        return reasonForAdopting;
    }

    public void setReasonForAdopting(String reasonForAdopting) {
        this.reasonForAdopting = reasonForAdopting;
    }

    public Boolean getAgreesToReturnPolicy() {
        return agreesToReturnPolicy;
    }

    public void setAgreesToReturnPolicy(Boolean agreesToReturnPolicy) {
        this.agreesToReturnPolicy = agreesToReturnPolicy;
    }
}
