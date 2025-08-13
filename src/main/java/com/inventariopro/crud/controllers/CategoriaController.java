package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importar ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping; // Importar CategoriaDTO
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.CategoriaDTO;
import com.inventariopro.crud.models.CategoriaModel;
import com.inventariopro.crud.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() { // Cambiado a ResponseEntity<List<CategoriaDTO>>
        List<CategoriaDTO> categorias = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    public CategoriaModel guardarCategoria(@RequestBody CategoriaModel categoria) {
        return categoriaService.guardarCategoria(categoria);
    }

    @GetMapping("/{id}")
    public Optional<CategoriaModel> obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        boolean eliminado = categoriaService.eliminarCategoria(id);
        return eliminado ? "Categoría eliminada" : "Error al eliminar la categoría";
    }
}