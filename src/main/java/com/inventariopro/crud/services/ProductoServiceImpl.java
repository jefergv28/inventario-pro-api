package com.inventariopro.crud.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.inventariopro.crud.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistorialMovimientoService historialMovimientoService;

    @Override
    public List<ProductoModel> getProductosByUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return productoRepository.findByUsuarioAndActivoTrue(usuario);
    }

    // ... (el resto de tus métodos implementados aquí)
    @Override
    @Transactional
    public ProductoModel saveProducto(ProductoModel producto, Long usuarioId) throws Exception {
        ProductoModel productoGuardado = productoRepository.save(producto);
        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (productoGuardado.getCantidadProducto() > 0) {
            historialMovimientoService.registrarMovimiento(
                    productoGuardado,
                    usuario.getEmail(),
                    "ENTRADA",
                    productoGuardado.getCantidadProducto()
            );
        }
        return productoGuardado;
    }

    @Override
    public ProductoModel guardarProducto(ProductoModel producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<ProductoModel> getById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public ProductoModel getProductoByIdAndUsuario(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productoRepository.findByIdAndUsuario(id, usuario).orElse(null);
    }

    @Override
    public Optional<ProductoModel> obtenerPorIdYUsuario(Long id, User usuario) {
        return productoRepository.findByIdAndUsuario(id, usuario);
    }

    @Override
    @Transactional
    @SuppressWarnings("CallToPrintStackTrace")
    public ProductoModel updateById(ProductoModel datos, Long id, Long usuarioId) throws Exception {
        return productoRepository.findById(id).map(producto -> {
            int cantidadAnterior = producto.getCantidadProducto();
            int cantidadNueva = datos.getCantidadProducto();
            producto.setNombreProducto(datos.getNombreProducto());
            producto.setDescripcionProducto(datos.getDescripcionProducto());
            producto.setCategoria(datos.getCategoria());
            producto.setProveedor(datos.getProveedor());
            producto.setPrecioProducto(datos.getPrecioProducto());
            producto.setImageUrl(datos.getImageUrl());
            if (cantidadNueva != cantidadAnterior) {
                producto.setCantidadProducto(cantidadNueva);
            }
            ProductoModel actualizado = productoRepository.save(producto);
            try {
                User usuario = userRepository.findById(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                if (cantidadNueva > cantidadAnterior) {
                    historialMovimientoService.registrarMovimiento(
                            actualizado,
                            usuario.getEmail(),
                            "ENTRADA",
                            cantidadNueva - cantidadAnterior
                    );
                } else if (cantidadNueva < cantidadAnterior) {
                    historialMovimientoService.registrarMovimiento(
                            actualizado,
                            usuario.getEmail(),
                            "SALIDA",
                            cantidadAnterior - cantidadNueva
                    );
                }
            } catch (Exception e) {
                System.err.println("Error al registrar movimiento: " + e.getMessage());
                e.printStackTrace();
            }
            return actualizado;
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long productoId, Long usuarioId) {
        try {
            ProductoModel producto = productoRepository.findByIdAndUsuarioId(productoId, usuarioId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado o no autorizado"));
            if (producto.getCantidadProducto() > 0) {
                historialMovimientoService.registrarMovimiento(
                        producto,
                        producto.getUsuario().getEmail(),
                        "SALIDA",
                        producto.getCantidadProducto()
                );
            }
            producto.setActivo(false);
            productoRepository.save(producto);
        } catch (Exception e) {
            log.error("❌ Error al desactivar producto con ID {}: {}", productoId, e.getMessage(), e);
            throw new RuntimeException("Error al desactivar el producto", e);
        }
    }

    @Override
    public List<ProductoModel> obtenerProductosActivosPorUsuario(User usuario) {
        return productoRepository.findByUsuarioAndActivoTrue(usuario);
    }

    @Override
    public List<Map<String, Object>> getMostMovedProductsByUsuario(String username) {
        // Implementación básica para que compile
        return List.of(
            Map.of("productName", "Ejemplo Producto 1", "movements", 50),
            Map.of("productName", "Ejemplo Producto 2", "movements", 45),
            Map.of("productName", "Ejemplo Producto 3", "movements", 30)
        );
    }

    // Implementación del método para productos recientes que te mostré antes
    @Override
    public List<ProductoModel> getProductosRecientesByUsuario(String username) {
        User usuario = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productoRepository.findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc(usuario.getId());
    }
}