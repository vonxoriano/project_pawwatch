package edu.cit.soriano.pawwatch.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String contactNumber;
    private String password;
}