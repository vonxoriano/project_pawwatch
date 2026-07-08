package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationRequest;
import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationStatusRequest;
import edu.cit.soriano.pawwatch.feature.animal.Animal;
import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.animal.AnimalRepository;
import edu.cit.soriano.pawwatch.feature.auth.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionApplicationService {

    private final AdoptionApplicationRepository applicationRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AdoptionApplicationService(
            AdoptionApplicationRepository applicationRepository,
            AnimalRepository animalRepository,
            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    public AdoptionApplication submitApplication(String email, ApplicationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        if (!animal.getAdoptionStatus().equals("AVAILABLE")) {
            throw new RuntimeException("Animal is not available for adoption");
        }

        if (applicationRepository.existsByUserAndAnimalAndStatus(user, animal, "PENDING")) {
            throw new RuntimeException("You already have a pending application for this animal");
        }

        AdoptionApplication application = new AdoptionApplication();
        application.setUser(user);
        application.setAnimal(animal);
        application.setStatus("PENDING");
        application.setRemarks(request.getRemarks());

        animal.setAdoptionStatus("PENDING");
        animalRepository.save(animal);

        return applicationRepository.save(application);
    }

    public List<AdoptionApplication> getMyApplications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByUser(user);
    }

    public List<AdoptionApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public AdoptionApplication processApplication(Long applicationId, ApplicationStatusRequest request) {
        AdoptionApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(request.getStatus());
        application.setRemarks(request.getRemarks());

        if (request.getStatus().equals("APPROVED")) {
            Animal animal = application.getAnimal();
            animal.setAdoptionStatus("ADOPTED");
            animalRepository.save(animal);

            List<AdoptionApplication> otherApplications = applicationRepository
                    .findByAnimal(animal);
            for (AdoptionApplication other : otherApplications) {
                if (!other.getApplicationId().equals(applicationId)
                        && other.getStatus().equals("PENDING")) {
                    other.setStatus("REJECTED");
                    applicationRepository.save(other);
                }
            }
        }

        if (request.getStatus().equals("REJECTED")) {
            Animal animal = application.getAnimal();
            List<AdoptionApplication> pendingApplications = applicationRepository
                    .findByAnimal(animal);
            boolean hasOtherPending = pendingApplications.stream()
                    .anyMatch(a -> !a.getApplicationId().equals(applicationId)
                            && a.getStatus().equals("PENDING"));
            if (!hasOtherPending) {
                animal.setAdoptionStatus("AVAILABLE");
                animalRepository.save(animal);
            }
        }

        return applicationRepository.save(application);
    }

    public void cancelApplication(Long applicationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdoptionApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (!application.getStatus().equals("PENDING")) {
            throw new RuntimeException("Only pending applications can be cancelled");
        }

        Animal animal = application.getAnimal();
        animal.setAdoptionStatus("AVAILABLE");
        animalRepository.save(animal);

        applicationRepository.delete(application);
    }
}