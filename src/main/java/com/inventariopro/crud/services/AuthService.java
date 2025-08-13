package com.inventariopro.crud.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.dto.AuthResponse;
import com.inventariopro.crud.dto.LoginRequest;
import com.inventariopro.crud.dto.RegisterRequest;
import com.inventariopro.crud.models.Role;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.UserRepository;


@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;

  public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

  public AuthResponse login(LoginRequest request) {
    // Autenticar al usuario
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    // Buscar usuario y generar token
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String token = jwtService.getToken(user);

    return AuthResponse.builder()
        .token(token)
        .build();
  }

  public AuthResponse register(RegisterRequest request) {
    User user = User.builder()
        .name(request.getFullName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword())) // Importante: encriptar
        .role(Role.USER)
        .build();

    userRepository.save(user);

    return AuthResponse.builder()
        .token(jwtService.getToken(user))
        .build();
  }

  public void forgotPassword(String email) {
    userRepository.findByEmail(email).ifPresent(user -> {
        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = "http://localhost:3000/auth/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Recuperación de Contraseña", "Haz clic en el siguiente enlace para restablecer tu contraseña: " + resetLink);
    });
}

public void resetPassword(String token, String newPassword) {
    User user = userRepository.findByPasswordResetToken(token)
        .orElseThrow(() -> new RuntimeException("Token inválido o expirado."));

    if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("El token ha expirado.");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setPasswordResetToken(null);
    user.setPasswordResetTokenExpiry(null);
    userRepository.save(user);
}

}
