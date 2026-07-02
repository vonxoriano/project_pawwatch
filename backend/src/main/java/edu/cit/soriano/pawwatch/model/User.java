package edu.cit.soriano.pawwatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ADOPTER";

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}