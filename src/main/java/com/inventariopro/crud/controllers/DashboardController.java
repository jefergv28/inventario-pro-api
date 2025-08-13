package com.inventariopro.crud.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.services.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService; // Inyectamos el servicio

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = dashboardService.getDashboardData(); // Obtenemos los datos del servicio
        return ResponseEntity.ok(data);
    }
}