package com.inventariopro.crud.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.JwtUtil.JwtUtil;
import com.inventariopro.crud.dto.LoginRequestDTO;
import com.inventariopro.crud.models.UsuarioModel;
import com.inventariopro.crud.services.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody UsuarioModel usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al registrar el usuario.");
            return ResponseEntity.status(500).body(errorResponse); // Código 500 para error interno
        }
    }


 @PostMapping("/login")
public ResponseEntity<Map<String, String>> loginUsuario(@RequestBody LoginRequestDTO loginRequest) {
    System.out.println("📌 Email recibido: " + loginRequest.getEmail());
    System.out.println("📌 Contraseña recibida: " + loginRequest.getPassword());

    if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error: Faltan campos email o password.");
        return ResponseEntity.badRequest().body(response);
    }

    boolean isAuthenticated = usuarioService.validarCredenciales(loginRequest.getEmail(), loginRequest.getPassword());

    Map<String, String> response = new HashMap<>();
    if (isAuthenticated) {
        // 🔥 Generar el token JWT
        String token = JwtUtil.generateToken(loginRequest.getEmail());
        response.put("token", token);
        response.put("message", "Inicio de sesión exitoso.");
        return ResponseEntity.ok(response);
    } else {
        response.put("message", "Error: Usuario o contraseña incorrectos.");
        return ResponseEntity.status(401).body(response);
    }
}




}
