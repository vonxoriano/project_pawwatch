package edu.cit.soriano.pawwatch.controller;

import edu.cit.soriano.pawwatch.dto.AuthResponse;
import edu.cit.soriano.pawwatch.dto.LoginRequest;
import edu.cit.soriano.pawwatch.dto.RegisterRequest;
import edu.cit.soriano.pawwatch.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
// ./mvnw spring-boot:run