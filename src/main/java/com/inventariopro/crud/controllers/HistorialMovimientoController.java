package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.HistorialMovimientoDTO;
import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.services.HistorialMovimientoService;
import com.inventariopro.crud.services.ProductoService;

@RestController
@RequestMapping("/historial")
public class HistorialMovimientoController {

    @Autowired
    private HistorialMovimientoService historialService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<HistorialMovimientoDTO> obtenerMovimientos(@AuthenticationPrincipal UserDetails userDetails) {
        List<HistorialMovimientoModel> movimientos = historialService.obtenerMovimientosPorUsuario(userDetails.getUsername());

        return movimientos.stream()
            .map(this::convertirAHistorialMovimientoDTO)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<HistorialMovimientoDTO> crearMovimiento(@RequestBody HistorialMovimientoDTO request,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Optional<ProductoModel> productoOpt = productoService.getById(request.getProductoId());
            if (productoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            ProductoModel producto = productoOpt.get();

            HistorialMovimientoModel movimiento = historialService.registrarMovimiento(
                producto,
                userDetails.getUsername(),
                request.getTipoMovimiento(),
                request.getCantidad()
            );

            HistorialMovimientoDTO movimientoDTO = convertirAHistorialMovimientoDTO(movimiento);

            return ResponseEntity.status(HttpStatus.CREATED).body(movimientoDTO);

        } catch (Exception e) {
            System.err.println("Error al crear movimiento: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMovimientoDTO> obtenerMovimientoPorId(@PathVariable Long id) {
        Optional<HistorialMovimientoModel> movimientoOpt = historialService.obtenerPorId(id);

        if (movimientoOpt.isPresent()) {
            HistorialMovimientoDTO movimientoDTO = convertirAHistorialMovimientoDTO(movimientoOpt.get());
            return ResponseEntity.ok(movimientoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public String eliminarMovimiento(@PathVariable Long id) {
        boolean eliminado = historialService.eliminarMovimiento(id);
        return eliminado ? "Movimiento eliminado" : "Error al eliminar el movimiento";
    }

    /**
     * Método auxiliar para convertir una entidad HistorialMovimientoModel a un DTO.
     * @param movimiento La entidad a convertir.
     * @return El DTO con la información simplificada.
     */
  private HistorialMovimientoDTO convertirAHistorialMovimientoDTO(HistorialMovimientoModel movimiento) {
    HistorialMovimientoDTO dto = new HistorialMovimientoDTO();
    dto.setId(movimiento.getId());
    dto.setTipoMovimiento(movimiento.getTipoMovimiento());
    dto.setCantidad(movimiento.getCantidad());

    // CORRECCIÓN FINAL: Usar el método setFecha() correcto
    if (movimiento.getFechaMovimiento() != null) {
        dto.setFecha(movimiento.getFechaMovimiento());
    }

    // Mapea la información del producto
    if (movimiento.getProducto() != null) {
        dto.setProductoId(movimiento.getProducto().getId());
        dto.setNombreProducto(movimiento.getProducto().getNombreProducto());
    }
    // Mapea la información del usuario
    if (movimiento.getUsuario() != null) {
        dto.setUsuarioId(movimiento.getUsuario().getId());
        dto.setNombreUsuario(movimiento.getUsuario().getName());
    }
    return dto;
}
}