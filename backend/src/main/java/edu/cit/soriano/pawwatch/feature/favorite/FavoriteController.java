package edu.cit.soriano.pawwatch.feature.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // Add an animal to favorites
    @PostMapping("/add/{animalId}")
    public ResponseEntity<?> add(@PathVariable Long animalId, Principal principal) {
        try {
            Favorite favorite = favoriteService.addFavorite(principal.getName(), animalId);
            return ResponseEntity.ok(favorite);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Remove an animal from favorites
    @DeleteMapping("/remove/{animalId}")
    public ResponseEntity<?> remove(@PathVariable Long animalId, Principal principal) {
        try {
            favoriteService.removeFavorite(principal.getName(), animalId);
            return ResponseEntity.ok("Removed from favorites");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // View my favorites list
    @GetMapping("/my")
    public ResponseEntity<List<Favorite>> getMyFavorites(Principal principal) {
        return ResponseEntity.ok(favoriteService.getMyFavorites(principal.getName()));
    }

    // Check if a specific animal is favorited (for toggle button state)
    @GetMapping("/check/{animalId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long animalId, Principal principal) {
        return ResponseEntity.ok(favoriteService.isFavorite(principal.getName(), animalId));
    }
}