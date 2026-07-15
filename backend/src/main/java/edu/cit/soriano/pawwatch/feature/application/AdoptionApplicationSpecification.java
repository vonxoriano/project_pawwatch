package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.animal.Animal;
import edu.cit.soriano.pawwatch.feature.auth.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

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
}