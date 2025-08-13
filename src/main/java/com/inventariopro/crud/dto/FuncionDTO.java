package com.inventariopro.crud.dto;

public class FuncionDTO {
  private final String nombre;
    private final String path;

    public FuncionDTO(String nombre, String path) {
        this.nombre = nombre;
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }
}
