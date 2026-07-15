package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.animal.Animal;
import edu.cit.soriano.pawwatch.feature.auth.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AdoptionApplicationSpecification {

    public static Specification<AdoptionApplication> hasStatus(String status) {
        return (root, query, cb) ->
                status == null || status.isEmpty() ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<AdoptionApplication> keywordMatches(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;

            Join<AdoptionApplication, User> userJoin = root.join("user");
            Join<AdoptionApplication, Animal> animalJoin = root.join("animal");

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(userJoin.get("fullName")), likePattern),
                    cb.like(cb.lower(animalJoin.get("name")), likePattern)
            );
        };
    }

    public static Specification<AdoptionApplication> dateFrom(LocalDate dateFrom) {
        return (root, query, cb) -> {
            if (dateFrom == null) return null;
            LocalDateTime start = LocalDateTime.of(dateFrom, LocalTime.MIN);
            return cb.greaterThanOrEqualTo(root.get("applicationDate"), start);
        };
    }

    public static Specification<AdoptionApplication> dateTo(LocalDate dateTo) {
        return (root, query, cb) -> {
            if (dateTo == null) return null;
            LocalDateTime end = LocalDateTime.of(dateTo, LocalTime.MAX);
            return cb.lessThanOrEqualTo(root.get("applicationDate"), end);
        };
    }
}