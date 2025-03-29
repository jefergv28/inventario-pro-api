package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.services.ProveedorService;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<ProveedorModel> obtenerProveedores() {
        return proveedorService.obtenerProveedores();
    }

    @PostMapping
    public ProveedorModel guardarProveedor(@RequestBody ProveedorModel proveedor) {
        return proveedorService.guardarProveedor(proveedor);
    }

    @GetMapping("/{id}")
    public Optional<ProveedorModel> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        boolean eliminado = proveedorService.eliminarProveedor(id);
        return eliminado ? "Proveedor eliminado" : "Error al eliminar el proveedor";
    }
}
