package com.inventariopro.crud.services;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

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
}
