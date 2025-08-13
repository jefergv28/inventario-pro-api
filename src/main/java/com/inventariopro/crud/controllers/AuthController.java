package com.inventariopro.crud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.AuthResponse;
import com.inventariopro.crud.dto.LoginRequest;
import com.inventariopro.crud.dto.RegisterRequest;
import com.inventariopro.crud.dto.ResetPasswordRequest;
import com.inventariopro.crud.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:8080"})

@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
@PostMapping("/forgot-password")
 public ResponseEntity<String> forgotPassword(@RequestBody String email) {
    authService.forgotPassword(email);
 return ResponseEntity.ok("Si la dirección de correo electrónico está asociada con una cuenta, recibirás un enlace para restablecer tu contraseña."); }
 @PostMapping("/reset-password") public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
 authService.resetPassword(request.getToken(), request.getNewPassword());
 return ResponseEntity.ok("Contraseña restablecida con éxito.");
 }

}
