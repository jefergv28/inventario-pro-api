package com.inventariopro.crud.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.HistorialMovimientoModel;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.HistorialMovimientoRepository;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class HistorialMovimientoService {

    @Autowired
    private HistorialMovimientoRepository historialRepository;

    @Autowired
    private ProductoRepository productoRepository; // Aunque no lo usaremos para actualizar cantidad aquí, lo mantenemos por si acaso

    @Autowired
    private UserRepository userRepository;

    // ✅ Registrar movimiento con email (usuario autenticado)
    public HistorialMovimientoModel registrarMovimiento(Long productoId, String email, String tipoMovimiento, Integer cantidad) throws Exception {
        // Obtenemos el producto solo para asociarlo al historial, no para modificar su cantidad aquí
        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // No modificamos la cantidad del producto aquí.
        // Esa lógica ya se maneja en ProductoService.saveProducto y ProductoService.updateById.

        // Validaciones (si quieres mantenerlas, aunque la principal ya está en ProductoService)
        if ("SALIDA".equalsIgnoreCase(tipoMovimiento)) {
            // Opcional: Podrías hacer una validación de stock aquí si el HistorialMovimientoService
            // fuera llamado directamente para una salida sin pasar por ProductoService.
            // Pero como ProductoService ya valida, esta es redundante en este caso de uso.
        } else if (!"ENTRADA".equalsIgnoreCase(tipoMovimiento)) {
            throw new Exception("Tipo de movimiento inválido");
        }

        HistorialMovimientoModel movimiento = new HistorialMovimientoModel();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(tipoMovimiento.toUpperCase());
        movimiento.setCantidad(cantidad); // La cantidad del movimiento (la diferencia)
        movimiento.setFechaMovimiento(new Date());

        return historialRepository.save(movimiento);
    }

    // ✅ Obtener movimientos solo del usuario autenticado
    public List<HistorialMovimientoModel> obtenerMovimientosPorUsuario(String email) {
        return historialRepository.findByUsuario_Email(email);
    }

    // ✅ Guardar directamente un movimiento (opcional, si no lo usas, puedes eliminarlo)
    public HistorialMovimientoModel guardarMovimiento(HistorialMovimientoModel movimiento) {
        return historialRepository.save(movimiento);
    }

    // ✅ Obtener movimiento por ID
    public Optional<HistorialMovimientoModel> obtenerPorId(Long id) {
        return historialRepository.findById(id);
    }

    // ✅ Eliminar movimiento
    public boolean eliminarMovimiento(Long id) {
        try {
            historialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}