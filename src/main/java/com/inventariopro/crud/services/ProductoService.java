package com.inventariopro.crud.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.repositories.IProductoRepository;

// Indica que esta clase es un servicio de la aplicación
@Service
public class ProductoService {

    // Inyección de dependencias para acceder al repositorio de productos
    @Autowired
    private IProductoRepository productoRepository;

    // Método para obtener todos los productos de la base de datos
    public ArrayList<ProductoModel> getProducto() {
        return (ArrayList<ProductoModel>) productoRepository.findAll();
    }

    // Método para guardar un nuevo producto en la base de datos
    public ProductoModel saveProducto(ProductoModel producto) {
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
                // Actualiza los atributos del producto encontrado
                producto.setNombreProducto(request.getNombreProducto());
                producto.setPrecioProducto(request.getPrecioProducto());
                producto.setCantidadProducto(request.getCantidadProducto());

                // Guarda y retorna el producto actualizado
                return productoRepository.save(producto);
            })
            .orElseThrow(() -> new RuntimeException("❌ Producto no encontrado con ID: " + id));
    }

    // Método para eliminar un producto por su ID
    public boolean deleteProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true; // Producto eliminado correctamente
        }
        return false; // Producto no encontrado
    }
}
