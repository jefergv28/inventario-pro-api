package com.inventariopro.crud.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.UsuarioModel;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.inventariopro.crud.repositories.UsuarioRepository;

// Indica que esta clase es un servicio de la aplicación
@Service
public class ProductoService {

    // Inyección de dependencias para acceder al repositorio de productos
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    // Método para obtener los productos de un usuario por su email
    public ArrayList<ProductoModel> getProductosByUsuario(String email) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            System.out.println("Usuario encontrado: " + usuario.getEmail());
            ArrayList<ProductoModel> productos = (ArrayList<ProductoModel>) productoRepository.findByUsuario(usuario);
            System.out.println("Productos encontrados: " + productos.size());
            return productos;
        } else {
            throw new RuntimeException("❌ Usuario no encontrado con email: " + email);
        }
    }

    // Método para guardar un nuevo producto
    public ProductoModel saveProducto(ProductoModel producto) {
        System.out.println("Intentando guardar producto: " + producto);
        return productoRepository.save(producto);
    }

    // Método para obtener un producto por su ID
    public Optional<ProductoModel> getById(Long id) {
        return productoRepository.findById(id);
    }

    // Método para actualizar un producto por su ID
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

    // Método para eliminar un producto por su ID
    public boolean deleteProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
