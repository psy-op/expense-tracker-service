package com.example.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    private String currency = "USD";
}

@Data
class LoginRequest {
    @Email
    private String email;
    @NotBlank
    private String password;
}

@Data
class AuthResponse {
    private final String token;
}
