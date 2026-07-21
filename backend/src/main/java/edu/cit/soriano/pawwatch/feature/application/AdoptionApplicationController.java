package edu.cit.soriano.pawwatch.feature.application;

import edu.cit.soriano.pawwatch.feature.application.dto.ReportSummary;
import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationRequest;
import edu.cit.soriano.pawwatch.feature.application.dto.ApplicationStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class AdoptionApplicationController {

    private final AdoptionApplicationService applicationService;

    public AdoptionApplicationController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Adopter - submit application
    @PostMapping("/submit")
    public ResponseEntity<?> submit(
            @RequestBody ApplicationRequest request,
            Principal principal) {
        try {
            AdoptionApplication application = applicationService
                    .submitApplication(principal.getName(), request);
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    // Admin - view all applications (with optional status/keyword/date filter)
@GetMapping("/admin/all")
public ResponseEntity<List<AdoptionApplication>> getAllApplications(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
    if ((status != null && !status.isEmpty()) || (keyword != null && !keyword.isEmpty())
            || dateFrom != null || dateTo != null) {
        return ResponseEntity.ok(applicationService.filterApplications(status, keyword, dateFrom, dateTo));
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
    // Admin - aggregated report for a date range
@GetMapping("/admin/report")
public ResponseEntity<?> getReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    try {
        ReportSummary report = applicationService.getApplicationReport(startDate, endDate);
        return ResponseEntity.ok(report);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}