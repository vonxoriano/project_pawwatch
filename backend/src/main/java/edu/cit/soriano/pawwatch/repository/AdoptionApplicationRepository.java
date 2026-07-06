package edu.cit.soriano.pawwatch.repository;

import edu.cit.soriano.pawwatch.model.AdoptionApplication;
import edu.cit.soriano.pawwatch.model.User;
import edu.cit.soriano.pawwatch.model.Animal;
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