package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventariopro.crud.dto.MovimientoRequest;
import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.services.
HistorialMovimientoService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@RequestMapping("/historial")
public class HistorialMovimientoController {

    @Autowired
    private HistorialMovimientoService historialService;

  @GetMapping
public List<HistorialMovimientoModel> obtenerMovimientos(@AuthenticationPrincipal UserDetails userDetails) {
    return historialService.obtenerMovimientosPorUsuario(userDetails.getUsername());
}


   @PostMapping
public ResponseEntity<?> crearMovimiento(@RequestBody MovimientoRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {
    try {
        HistorialMovimientoModel movimiento = historialService.registrarMovimiento(
            request.getProductoId(),
            userDetails.getUsername(), // <- ahora usamos el email del token
            request.getTipoMovimiento(),
            request.getCantidad()
        );
        return ResponseEntity.ok(movimiento);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
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
