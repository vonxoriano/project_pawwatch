package edu.cit.soriano.pawwatch.feature.animal;

import edu.cit.soriano.pawwatch.feature.animal.dto.AnimalRequest;
import edu.cit.soriano.pawwatch.feature.favorite.FavoriteRepository;
import edu.cit.soriano.pawwatch.feature.application.AdoptionApplicationRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final FavoriteRepository favoriteRepository;
    private final AdoptionApplicationRepository applicationRepository;

    public AnimalService(AnimalRepository animalRepository,
                          FavoriteRepository favoriteRepository,
                          AdoptionApplicationRepository applicationRepository) {
        this.animalRepository = animalRepository;
        this.favoriteRepository = favoriteRepository;
        this.applicationRepository = applicationRepository;
    }

    public Animal addAnimal(AnimalRequest request) {
        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setSpecies(request.getSpecies());
        animal.setBreed(request.getBreed());
        animal.setAge(request.getAge());
        animal.setGender(request.getGender());
        animal.setDescription(request.getDescription());
        animal.setHealthStatus(request.getHealthStatus());
        animal.setPhoto(request.getPhoto());
        animal.setAdoptionStatus("AVAILABLE");
        return animalRepository.save(animal);
    }

    public Animal editAnimal(Long id, AnimalRequest request) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
        animal.setName(request.getName());
        animal.setSpecies(request.getSpecies());
        animal.setBreed(request.getBreed());
        animal.setAge(request.getAge());
        animal.setGender(request.getGender());
        animal.setDescription(request.getDescription());
        animal.setHealthStatus(request.getHealthStatus());
        animal.setPhoto(request.getPhoto());
        return animalRepository.save(animal);
    }

    public void removeAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        if (!applicationRepository.findByAnimal(animal).isEmpty()) {
            throw new RuntimeException(
                    "Cannot remove this animal: it has adoption application history. " +
                    "Remove is only allowed for animals with no submitted applications.");
        }

        favoriteRepository.deleteByAnimal(animal);
        animalRepository.delete(animal);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Animal> getAvailableAnimals() {
        return animalRepository.findByAdoptionStatus("AVAILABLE");
    }

    public List<Animal> searchAnimals(String keyword) {
        return animalRepository
                .findByNameContainingIgnoreCaseOrBreedContainingIgnoreCase(keyword, keyword);
    }

    public List<Animal> filterBySpecies(String species) {
        return animalRepository.findBySpeciesAndAdoptionStatus(species, "AVAILABLE");
    }

    public List<Animal> filterAvailableAnimals(String species, Integer minAge, Integer maxAge) {
        Specification<Animal> spec = Specification.allOf(
                AnimalSpecification.hasAdoptionStatus("AVAILABLE"),
                AnimalSpecification.hasSpecies(species),
                AnimalSpecification.minAge(minAge),
                AnimalSpecification.maxAge(maxAge));

        return animalRepository.findAll(spec);
    }

    public List<Animal> filterAvailableAnimals(String species, String gender, Integer minAge, Integer maxAge) {
        Specification<Animal> spec = Specification.allOf(
                AnimalSpecification.hasAdoptionStatus("AVAILABLE"),
                AnimalSpecification.hasSpecies(species),
                AnimalSpecification.hasGender(gender),
                AnimalSpecification.minAge(minAge),
                AnimalSpecification.maxAge(maxAge));

        return animalRepository.findAll(spec);
    }

    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
    }

    public Animal updateStatus(Long id, String status) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
        animal.setAdoptionStatus(status);
        return animalRepository.save(animal);
    }
}