package edu.cit.soriano.pawwatch.controller;

import edu.cit.soriano.pawwatch.dto.AnimalRequest;
import edu.cit.soriano.pawwatch.model.Animal;
import edu.cit.soriano.pawwatch.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@CrossOrigin(origins = "http://localhost:3000")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // Public - Adopters browse available animals
    @GetMapping("/browse")
    public ResponseEntity<List<Animal>> browse(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String species) {

        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(animalService.searchAnimals(keyword));
        }
        if (species != null && !species.isEmpty()) {
            return ResponseEntity.ok(animalService.filterBySpecies(species));
        }
        return ResponseEntity.ok(animalService.getAvailableAnimals());
    }

    // Public - View single animal
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    // Admin only
    @GetMapping("/admin/all")
    public ResponseEntity<List<Animal>> getAll() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Animal> add(@RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.addAnimal(request));
    }

    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<Animal> edit(@PathVariable Long id,
                                        @RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.editAnimal(id, request));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        animalService.removeAnimal(id);
        return ResponseEntity.ok("Animal removed successfully");
    }
}