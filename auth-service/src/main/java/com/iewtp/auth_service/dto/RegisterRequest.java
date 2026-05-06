package com.iewtp.auth_service.dto;

import com.iewtp.auth_service.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;
}
