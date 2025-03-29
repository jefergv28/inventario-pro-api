package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.CategoriaModel;
import com.inventariopro.crud.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaModel> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    public CategoriaModel guardarCategoria(CategoriaModel categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<CategoriaModel> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public boolean eliminarCategoria(Long id) {
        try {
            categoriaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
