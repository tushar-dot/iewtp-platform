package com.iewtp.auth_service.service;

import com.iewtp.auth_service.dto.AuthResponse;
import com.iewtp.auth_service.dto.LoginRequest;
import com.iewtp.auth_service.dto.RefreshTokenRequest;
import com.iewtp.auth_service.dto.RegisterRequest;
import com.iewtp.auth_service.entity.RefreshToken;
import com.iewtp.auth_service.entity.Role;
import com.iewtp.auth_service.entity.User;
import com.iewtp.auth_service.exception.UserAlreadyExistsException;
import com.iewtp.auth_service.repository.RefreshTokenRepository;
import com.iewtp.auth_service.repository.UserRepository;
import com.iewtp.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) throws UserAlreadyExistsException {

        System.out.println("Service hit");
        System.out.println("Email: " + request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException ("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        userRepository.save(user);

        System.out.println("User saved");
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }
    public  void logout(String refreshToken){
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Invalid Refresh Token"));
        refreshTokenRepository.deleteById(token.getId());
    }
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtUtil.generateToken(token.getUser().getEmail());

        return new AuthResponse(newAccessToken, token.getToken());
    }

    // method to generate refresh token
    private String generateRefreshToken(User user){

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshToken);

        return token;
    }
}