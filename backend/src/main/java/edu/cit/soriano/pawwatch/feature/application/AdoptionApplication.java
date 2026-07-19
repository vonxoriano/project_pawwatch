package edu.cit.soriano.pawwatch.feature.application;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.animal.Animal;

@Entity
@Table(name = "adoption_applications")
public class AdoptionApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "createdAt"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonIgnoreProperties({"description", "healthStatus"})
    private Animal animal;

    @Column(nullable = false)
    private LocalDateTime applicationDate = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "PENDING";

    private String remarks;

    // Adoption questionnaire fields
    private String housingType; // "OWN" or "RENT"

    private Boolean hasLandlordPermission; // only relevant if renting, nullable

    private Boolean hasYard;

    private Integer householdMembers;

    private Boolean hasYoungChildren;

    private Boolean hasOtherPets;

    @Column(columnDefinition = "TEXT")
    private String petExperience;

    private Integer hoursAwayDaily;

    @Column(columnDefinition = "TEXT")
    private String reasonForAdopting;

    private Boolean agreesToReturnPolicy;

    public AdoptionApplication() {
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
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