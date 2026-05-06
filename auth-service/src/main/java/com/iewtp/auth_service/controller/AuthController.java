package com.iewtp.auth_service.controller;


import com.iewtp.auth_service.dto.AuthResponse;
import com.iewtp.auth_service.dto.LoginRequest;
import com.iewtp.auth_service.dto.RefreshTokenRequest;
import com.iewtp.auth_service.dto.RegisterRequest;
import com.iewtp.auth_service.exception.UserAlreadyExistsException;
import com.iewtp.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;



    @PostMapping("/register")
    public ResponseEntity<String> register( @Valid @RequestBody RegisterRequest request) throws UserAlreadyExistsException {

        System.out.println("Controller hit");
        System.out.println("Request: " + request);

        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("TEST HIT");
        return "OK";
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request){
        AuthResponse response = authService.refreshToken(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request){
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged Out Successfully");
    }
}
