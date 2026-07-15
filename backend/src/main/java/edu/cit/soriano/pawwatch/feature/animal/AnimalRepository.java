package edu.cit.soriano.pawwatch.feature.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {
    List<Animal> findByAdoptionStatus(String adoptionStatus);
    List<Animal> findBySpeciesAndAdoptionStatus(String species, String adoptionStatus);
    List<Animal> findByNameContainingIgnoreCaseOrBreedContainingIgnoreCase(String name, String breed);
}