package edu.cit.soriano.pawwatch.feature.favorite;

import edu.cit.soriano.pawwatch.feature.animal.Animal;
import edu.cit.soriano.pawwatch.feature.animal.AnimalRepository;
import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            AnimalRepository animalRepository,
            UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Favorite addFavorite(String email, Long animalId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        if (favoriteRepository.existsByUserAndAnimal(user, animal)) {
            throw new RuntimeException("Animal is already in your favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAnimal(animal);

        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(String email, Long animalId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        if (!favoriteRepository.existsByUserAndAnimal(user, animal)) {
            throw new RuntimeException("Animal is not in your favorites");
        }

        favoriteRepository.deleteByUserAndAnimal(user, animal);
    }

    public List<Favorite> getMyFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return favoriteRepository.findByUser(user);
    }

    public boolean isFavorite(String email, Long animalId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
        return favoriteRepository.existsByUserAndAnimal(user, animal);
    }
}