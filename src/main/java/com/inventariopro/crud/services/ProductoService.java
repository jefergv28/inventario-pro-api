package com.inventariopro.crud.services;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.inventariopro.crud.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistorialMovimientoService historialMovimientoService;

    public List<ProductoModel> getProductosByUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return productoRepository.findByUsuario(usuario);
    }

 public ProductoModel saveProducto(ProductoModel producto, Long usuarioId) throws Exception {
    ProductoModel nuevo = productoRepository.save(producto);

    if (producto.getCantidadProducto() > 0) {
        User usuario = userRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        historialMovimientoService.registrarMovimiento(
            nuevo.getId(),
            usuario.getEmail(), // ← ahora sí existe y es válido
            "ENTRADA",
            producto.getCantidadProducto()
        );
    }

    return nuevo;
}


    public ProductoModel guardarProducto(ProductoModel producto) {
        return productoRepository.save(producto);
    }

    public Optional<ProductoModel> getById(Long id) {
        return productoRepository.findById(id);
    }

    public ProductoModel getProductoByIdAndUsuario(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productoRepository.findByIdAndUsuario(id, usuario).orElse(null);
    }

    public Optional<ProductoModel> obtenerPorIdYUsuario(Long id, User usuario) {
        return productoRepository.findByIdAndUsuario(id, usuario);
    }

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
            producto.setCantidadProducto(cantidadNueva);

            ProductoModel actualizado = productoRepository.save(producto);

            try {
    User usuario = userRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (cantidadNueva > cantidadAnterior) {
        historialMovimientoService.registrarMovimiento(id, usuario.getEmail(), "ENTRADA", cantidadNueva - cantidadAnterior);
    } else if (cantidadNueva < cantidadAnterior) {
        historialMovimientoService.registrarMovimiento(id, usuario.getEmail(), "SALIDA", cantidadAnterior - cantidadNueva);
    }
} catch (Exception e) {
    System.err.println("Error al registrar movimiento: " + e.getMessage());
}

            return actualizado;
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public boolean eliminarProducto(Long id, Long usuarioId) {
        Optional<ProductoModel> productoOpt = productoRepository.findById(id);
        Optional<User> usuarioOpt = userRepository.findById(usuarioId);

        if (productoOpt.isPresent() && usuarioOpt.isPresent()) {
            ProductoModel producto = productoOpt.get();
            User usuario = usuarioOpt.get();

            try {
                if (producto.getCantidadProducto() > 0) {
                    historialMovimientoService.registrarMovimiento(
                        producto.getId(),
                        usuario.getEmail(),
                        "SALIDA",
                        producto.getCantidadProducto()
                    );
                }

                productoRepository.delete(producto);
                return true;

            } catch (Exception e) {
                System.err.println("❌ Error al eliminar producto: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    public List<ProductoModel> obtenerProductosActivosPorUsuario(User usuario) {
        return productoRepository.findByUsuarioAndActivoTrue(usuario);
    }
}
