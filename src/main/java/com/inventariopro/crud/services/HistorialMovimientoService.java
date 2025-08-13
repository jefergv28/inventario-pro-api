package com.inventariopro.crud.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.HistorialMovimientoRepository;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class HistorialMovimientoService {

    @Autowired
    private HistorialMovimientoRepository historialRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Registra un movimiento en el historial.
     * @param producto La instancia de ProductoModel (debe ser una entidad gestionada).
     * @param email El email del usuario que realiza el movimiento.
     * @param tipoMovimiento El tipo de movimiento ("ENTRADA" o "SALIDA").
     * @param cantidad La cantidad del movimiento.
     * @return El HistorialMovimientoModel guardado.
     * @throws Exception Si el usuario no es encontrado o el tipo de movimiento es inválido.
     */
    @Transactional
    public HistorialMovimientoModel registrarMovimiento(ProductoModel producto, String email, String tipoMovimiento, Integer cantidad) throws Exception {

        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (!"ENTRADA".equalsIgnoreCase(tipoMovimiento) && !"SALIDA".equalsIgnoreCase(tipoMovimiento)) {
            throw new Exception("Tipo de movimiento inválido");
        }

        HistorialMovimientoModel movimiento = new HistorialMovimientoModel();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(tipoMovimiento.toUpperCase());
        movimiento.setCantidad(cantidad);
        movimiento.setFechaMovimiento(new Date());

        return historialRepository.save(movimiento);
    }

    public List<HistorialMovimientoModel> obtenerMovimientosPorUsuario(String email) {
        return historialRepository.findByUsuarioEmailWithDetails(email);
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

    /**
     * Obtiene el historial de movimientos de entrada y salida a lo largo del tiempo.
     * @param username El email del usuario autenticado.
     * @return Una lista de mapas con datos de movimientos por fecha.
     */
    public List<Map<String, Object>> getMovimientosPorTiempo(String username) {
        // Implementación básica para que el código compile y el frontend reciba datos de ejemplo.
        // La lógica real debería consultar tu base de datos para obtener los movimientos
        // por fecha (ej. agrupados por día o mes) y contar las entradas y salidas.
        return List.of(
            Map.of("date", "2024-07-01", "entradas", 10, "salidas", 5),
            Map.of("date", "2024-07-02", "entradas", 12, "salidas", 8),
            Map.of("date", "2024-07-03", "entradas", 8, "salidas", 10)
        );
    }
}