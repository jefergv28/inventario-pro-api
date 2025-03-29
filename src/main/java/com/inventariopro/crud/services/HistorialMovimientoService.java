package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.repositories.HistorialMovimientoRepository;

@Service
public class HistorialMovimientoService {

    @Autowired
    private HistorialMovimientoRepository historialRepository;

    public List<HistorialMovimientoModel> obtenerMovimientos() {
        return historialRepository.findAll();
    }

    public HistorialMovimientoModel guardarMovimiento(HistorialMovimientoModel movimiento) {
        return historialRepository.save(movimiento);
    }

    public Optional<HistorialMovimientoModel> obtenerPorId(Long id) {
        return historialRepository.findById(id);
    }

    public boolean eliminarMovimiento(Long id) {
        try {
            historialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
