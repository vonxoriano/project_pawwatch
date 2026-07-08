package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.animal.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    List<AdoptionApplication> findByUser(User user);
    List<AdoptionApplication> findByAnimal(Animal animal);
    Optional<AdoptionApplication> findByUserAndAnimalAndStatus(User user, Animal animal, String status);
    boolean existsByUserAndAnimalAndStatus(User user, Animal animal, String status);
}