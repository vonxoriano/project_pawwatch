package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationRequest;
import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:3000")
public class AdoptionApplicationController {

    private final AdoptionApplicationService applicationService;

    public AdoptionApplicationController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Adopter - submit application
    @PostMapping("/submit")
    public ResponseEntity<AdoptionApplication> submit(
            @RequestBody ApplicationRequest request,
            Principal principal) {
        AdoptionApplication application = applicationService
                .submitApplication(principal.getName(), request);
        return ResponseEntity.ok(application);
    }

    // Adopter - view my applications
    @GetMapping("/my")
    public ResponseEntity<List<AdoptionApplication>> getMyApplications(Principal principal) {
        return ResponseEntity.ok(applicationService.getMyApplications(principal.getName()));
    }

    // Adopter - cancel application
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable Long id, Principal principal) {
        applicationService.cancelApplication(id, principal.getName());
        return ResponseEntity.ok("Application cancelled successfully");
    }

    // Admin - view all applications (with optional status/keyword filter)
    @GetMapping("/admin/all")
    public ResponseEntity<List<AdoptionApplication>> getAllApplications(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword) {
    if ((status != null && !status.isEmpty()) || (keyword != null && !keyword.isEmpty())) {
        return ResponseEntity.ok(applicationService.filterApplications(status, keyword));
    }
    return ResponseEntity.ok(applicationService.getAllApplications());
}

    // Admin - approve or reject
    @PatchMapping("/admin/process/{id}")
    public ResponseEntity<AdoptionApplication> processApplication(
            @PathVariable Long id,
            @RequestBody ApplicationStatusRequest request) {
        return ResponseEntity.ok(applicationService.processApplication(id, request));
    }
}