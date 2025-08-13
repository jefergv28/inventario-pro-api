package com.inventariopro.crud.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResultsDTO {
    private final List<ProductoDTO> productos;
    private final List<CategoriaDTO> categorias;
    private final List<ProveedorDTO> proveedores;  // nuevo
    private final List<UsuarioDTO> usuarios;      // nuevo
    private final List<FuncionDTO> funciones;

    @JsonCreator
    public SearchResultsDTO(
        @JsonProperty("productos") List<ProductoDTO> productos,
        @JsonProperty("categorias") List<CategoriaDTO> categorias,
        @JsonProperty("proveedores") List<ProveedorDTO> proveedores,
        @JsonProperty("usuarios") List<UsuarioDTO> usuarios,
        @JsonProperty("funciones") List<FuncionDTO> funciones) {
        this.productos = productos;
        this.categorias = categorias;
        this.proveedores = proveedores;
        this.usuarios = usuarios;
        this.funciones = funciones;
    }

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public List<ProveedorDTO> getProveedores() {
        return proveedores;
    }

    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    public List<FuncionDTO> getFunciones() {
        return funciones;
    }
}
