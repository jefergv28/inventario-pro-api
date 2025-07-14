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
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Registrar movimiento con email (usuario autenticado)
    public HistorialMovimientoModel registrarMovimiento(Long productoId, String email, String tipoMovimiento, Integer cantidad) throws Exception {
        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if ("SALIDA".equalsIgnoreCase(tipoMovimiento)) {
            if (producto.getCantidadProducto() < cantidad) {
                throw new Exception("Cantidad insuficiente para realizar la salida");
            }
            producto.setCantidadProducto(producto.getCantidadProducto() - cantidad);
        } else if ("ENTRADA".equalsIgnoreCase(tipoMovimiento)) {
            producto.setCantidadProducto(producto.getCantidadProducto() + cantidad);
        } else {
            throw new Exception("Tipo de movimiento inválido");
        }

        productoRepository.save(producto);

        HistorialMovimientoModel movimiento = new HistorialMovimientoModel();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(tipoMovimiento.toUpperCase());
        movimiento.setCantidad(cantidad);
        movimiento.setFechaMovimiento(new Date());

        return historialRepository.save(movimiento);
    }

    // ✅ Obtener movimientos solo del usuario autenticado
    public List<HistorialMovimientoModel> obtenerMovimientosPorUsuario(String email) {
        return (List<HistorialMovimientoModel>) historialRepository.findByUsuario_Email(email);
    }

    // ✅ Guardar directamente un movimiento (opcional)
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
