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

import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.services.HistorialMovimientoService;

@RestController
@RequestMapping("/historial")
public class HistorialMovimientoController {

    @Autowired
    private HistorialMovimientoService historialService;

    @GetMapping
    public List<HistorialMovimientoModel> obtenerMovimientos() {
        return historialService.obtenerMovimientos();
    }

    @PostMapping
    public HistorialMovimientoModel guardarMovimiento(@RequestBody HistorialMovimientoModel movimiento) {
        return historialService.guardarMovimiento(movimiento);
    }

    @GetMapping("/{id}")
    public Optional<HistorialMovimientoModel> obtenerMovimientoPorId(@PathVariable Long id) {
        return historialService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarMovimiento(@PathVariable Long id) {
        boolean eliminado = historialService.eliminarMovimiento(id);
        return eliminado ? "Movimiento eliminado" : "Error al eliminar el movimiento";
    }
}
