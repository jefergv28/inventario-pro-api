package com.inventariopro.crud.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.services.ProductoService;

@CrossOrigin(origins = "http://127.0.0.1:5500") // Permite peticiones desde el frontend en localhost
@RestController
@RequestMapping("/productos") // Define la ruta base para este controlador
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Método para obtener la lista de productos
    @GetMapping
    public ArrayList<ProductoModel> getProductos() {
        return this.productoService.getProducto();
    }

    // Método para guardar un nuevo producto en la base de datos
    @PostMapping
    public ProductoModel saveProducto(@RequestBody ProductoModel producto) {
        return this.productoService.saveProducto(producto);
    }

    // Método para obtener un producto por su ID
    @GetMapping("/{id}")
    public Optional<ProductoModel> getProductoById(@PathVariable Long id) {
        return this.productoService.getById(id);
    }

    // Método para actualizar un producto por su ID
    @PutMapping("/{id}")
    public ProductoModel updateProductoById(@RequestBody ProductoModel request, @PathVariable Long id) {
        return this.productoService.updateById(request, id);
    }

    // Método para eliminar un producto por su ID
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        boolean ok = this.productoService.deleteProducto(id);

        if (ok) {
            return "✅ Producto con ID " + id + " eliminado correctamente.";
        } else {
            return "❌ Error: Producto con ID " + id + " no encontrado.";
        }
    }
}
