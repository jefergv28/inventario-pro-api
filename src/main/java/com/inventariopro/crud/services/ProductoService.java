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
            System.out.println("Usuario encontrado: " + usuario.getEmail());
            ArrayList<ProductoModel> productos = (ArrayList<ProductoModel>) productoRepository.findByUsuario(usuario);
            System.out.println("Productos encontrados: " + productos.size());
            return productos;
        } else {
            throw new RuntimeException("❌ Usuario no encontrado con email: " + email);
        }
    }

    public ProductoModel saveProducto(ProductoModel producto) {
        System.out.println("Intentando guardar producto: " + producto);
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
                producto.setProveedor(request.getProveedor());

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
}
