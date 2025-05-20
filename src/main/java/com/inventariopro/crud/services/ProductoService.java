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

    public ArrayList<ProductoModel> getProductosByUsuario(String email) {
        Optional<User> usuarioOptional = userRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            User usuario = usuarioOptional.get();
            return (ArrayList<ProductoModel>) productoRepository.findByUsuario(usuario);
        } else {
            throw new RuntimeException("❌ Usuario no encontrado con email: " + email);
        }
    }

    public ProductoModel saveProducto(ProductoModel producto) {
        return productoRepository.save(producto);
    }

    public Optional<ProductoModel> getById(Long id) {
        return productoRepository.findById(id);
    }

    public ProductoModel updateById(ProductoModel request, Long id) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setNombreProducto(request.getNombreProducto());
                producto.setPrecioProducto(request.getPrecioProducto());
                producto.setCantidadProducto(request.getCantidadProducto());
                producto.setCategoria(request.getCategoria());
                producto.setProveedor(null);
                return productoRepository.save(producto);
            })
            .orElseThrow(() -> new RuntimeException("❌ Producto no encontrado con ID: " + id));
    }

    public boolean deleteProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Este es el método que te falta para evitar el error
    public ProductoModel getProductoByIdAndUsuario(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return productoRepository.findByIdAndUsuario(id, usuario)
            .orElse(null);
    }
}
