package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importar Collectors

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar @Transactional

import com.inventariopro.crud.dto.CategoriaDTO; // Importar CategoriaDTO
import com.inventariopro.crud.models.CategoriaModel;
import com.inventariopro.crud.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true) // Es buena práctica para métodos de solo lectura
    public List<CategoriaDTO> obtenerCategorias() {
        // Obtenemos todas las categorías
        List<CategoriaModel> categorias = categoriaRepository.findAll();
        // Las convertimos a DTOs para evitar LazyInitializationException
        return categorias.stream()
                         .map(CategoriaDTO::new) // Usa el constructor del DTO
                         .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaModel guardarCategoria(CategoriaModel categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaModel> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public boolean eliminarCategoria(Long id) {
        try {
            categoriaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // Considera loggear la excepción completa: log.error("Error al eliminar categoría", e);
            return false;
        }
    }
}