package com.inventariopro.crud.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistorialMovimientoService historialMovimientoService;

    // Obtener productos por usuario email
    public ArrayList<ProductoModel> getProductosByUsuario(String email) {
        Optional<User> usuarioOptional = userRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            User usuario = usuarioOptional.get();
            return (ArrayList<ProductoModel>) productoRepository.findByUsuario(usuario);
        } else {
            throw new RuntimeException("❌ Usuario no encontrado con email: " + email);
        }
    }

    // Guardar producto nuevo y registrar movimiento ENTRADA con cantidad inicial
    public ProductoModel saveProducto(ProductoModel producto, Long usuarioId) throws Exception {
        ProductoModel nuevoProducto = productoRepository.save(producto);

        if (nuevoProducto.getCantidadProducto() > 0) {
            historialMovimientoService.registrarMovimiento(nuevoProducto.getId(), usuarioId, "ENTRADA", nuevoProducto.getCantidadProducto());
        }

        return nuevoProducto;
    }

    // Obtener producto por ID
    public Optional<ProductoModel> getById(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Actualizar producto, registrar movimientos ENTRADA o SALIDA si cambia la cantidad
     */
    public ProductoModel updateById(ProductoModel request, Long id, Long usuarioId) throws Exception {
        return productoRepository.findById(id)
            .map(producto -> {
                int cantidadOriginal = producto.getCantidadProducto();
                int nuevaCantidad = request.getCantidadProducto();

                producto.setNombreProducto(request.getNombreProducto());
                producto.setPrecioProducto(request.getPrecioProducto());
                producto.setCantidadProducto(nuevaCantidad);
                producto.setCategoria(request.getCategoria());
                producto.setProveedor(request.getProveedor());
                producto.setDescripcionProducto(request.getDescripcionProducto());
                producto.setImageUrl(request.getImageUrl());

                ProductoModel productoActualizado = productoRepository.save(producto);

                // Registrar movimientos solo si cambia la cantidad
                try {
                    if (nuevaCantidad > cantidadOriginal) {
                        int cantidadEntrada = nuevaCantidad - cantidadOriginal;
                        historialMovimientoService.registrarMovimiento(id, usuarioId, "ENTRADA", cantidadEntrada);
                    } else if (nuevaCantidad < cantidadOriginal) {
                        int cantidadSalida = cantidadOriginal - nuevaCantidad;
                        historialMovimientoService.registrarMovimiento(id, usuarioId, "SALIDA", cantidadSalida);
                    }
                } catch (Exception e) {
                    System.err.println("Error registrando movimiento: " + e.getMessage());
                }

                return productoActualizado;
            })
            .orElseThrow(() -> new RuntimeException("❌ Producto no encontrado con ID: " + id));
    }

    // Eliminar producto y registrar salida de la cantidad existente
 public boolean eliminarProducto(Long id, Long usuarioId) {
    Optional<ProductoModel> productoOpt = productoRepository.findById(id);
    Optional<User> usuarioOpt = userRepository.findById(usuarioId);

    if (productoOpt.isPresent() && usuarioOpt.isPresent()) {
        ProductoModel producto = productoOpt.get();
        User usuario = usuarioOpt.get();

        // Registrar el movimiento de salida con la cantidad actual
        if (producto.getCantidadProducto() > 0) {
            try {
             historialMovimientoService.registrarMovimiento(
    producto.getId(),
    usuario.getId(),
    "SALIDA",
    producto.getCantidadProducto()
);

            } catch (Exception e) {
                System.err.println("❌ Error registrando movimiento: " + e.getMessage());
                return false;
            }
        }

        // Ahora sí, eliminar el producto
        productoRepository.delete(producto);
        return true;
    }

    return false;
}



    // Obtener producto por ID y usuario (email)
    public ProductoModel getProductoByIdAndUsuario(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return productoRepository.findByIdAndUsuario(id, usuario)
            .orElse(null);
    }

    // Actualizar producto por ID y usuario (email)
    public ProductoModel updateProductoByIdAndUsuario(Long id, String email, ProductoModel datosActualizados, Long usuarioId) throws Exception {
        // Obtener usuario
        User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar producto
        ProductoModel producto = productoRepository.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado o no pertenece al usuario"));

        int cantidadOriginal = producto.getCantidadProducto();
        int nuevaCantidad = datosActualizados.getCantidadProducto();

        // Actualizar campos
        producto.setNombreProducto(datosActualizados.getNombreProducto());
        producto.setCantidadProducto(nuevaCantidad);
        producto.setDescripcionProducto(datosActualizados.getDescripcionProducto());
        producto.setCategoria(datosActualizados.getCategoria());
        producto.setProveedor(datosActualizados.getProveedor());
        producto.setPrecioProducto(datosActualizados.getPrecioProducto());
        if (datosActualizados.getImageUrl() != null) {
            producto.setImageUrl(datosActualizados.getImageUrl());
        }

        ProductoModel productoActualizado = productoRepository.save(producto);

        // Registrar movimientos si cambia cantidad
        try {
            if (nuevaCantidad > cantidadOriginal) {
                int cantidadEntrada = nuevaCantidad - cantidadOriginal;
                historialMovimientoService.registrarMovimiento(id, usuarioId, "ENTRADA", cantidadEntrada);
            } else if (nuevaCantidad < cantidadOriginal) {
                int cantidadSalida = cantidadOriginal - nuevaCantidad;
                historialMovimientoService.registrarMovimiento(id, usuarioId, "SALIDA", cantidadSalida);
            }
        } catch (Exception e) {
            System.err.println("Error registrando movimiento: " + e.getMessage());
        }

        return productoActualizado;
    }
}
