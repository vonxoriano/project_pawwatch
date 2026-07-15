package edu.cit.soriano.pawwatch.feature.animal;

import org.springframework.data.jpa.domain.Specification;

public class AnimalSpecification {

    public static Specification<Animal> hasAdoptionStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("adoptionStatus"), status);
    }

    public static Specification<Animal> hasSpecies(String species) {
        return (root, query, cb) ->
                species == null || species.isEmpty() ? null : cb.equal(root.get("species"), species);
    }

    public static Specification<Animal> hasGender(String gender) {
        return (root, query, cb) ->
                gender == null || gender.isEmpty() ? null : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Animal> minAge(Integer minAge) {
        return (root, query, cb) ->
                minAge == null ? null : cb.greaterThanOrEqualTo(root.get("age"), minAge);
    }

    public static Specification<Animal> maxAge(Integer maxAge) {
        return (root, query, cb) ->
                maxAge == null ? null : cb.lessThanOrEqualTo(root.get("age"), maxAge);
    }
}