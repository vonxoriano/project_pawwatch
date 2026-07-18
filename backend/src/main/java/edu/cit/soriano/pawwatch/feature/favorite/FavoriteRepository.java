package edu.cit.soriano.pawwatch.feature.favorite;

import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.animal.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndAnimal(User user, Animal animal);
    boolean existsByUserAndAnimal(User user, Animal animal);
    void deleteByUserAndAnimal(User user, Animal animal);
    void deleteByAnimal(Animal animal);
}