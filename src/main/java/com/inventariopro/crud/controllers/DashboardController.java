package com.inventariopro.crud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/dashboard") // Cambiamos el endpoint a /api/v1/dashboard
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", 120); // Número de usuarios registrados
        data.put("activeSessions", 45); // Número de sesiones activas
        data.put("sales", 3200); // Ventas del día
        return ResponseEntity.ok(data); // Retornamos la respuesta en formato JSON
    }
}