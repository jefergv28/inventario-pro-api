package com.inventariopro.crud.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CreateUserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role; // "admin" o "employee"

    // Aquí mapeamos directamente los permisos que vienen del frontend
    private Map<String, Boolean> permissions;
}