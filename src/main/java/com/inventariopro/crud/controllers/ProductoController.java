package com.inventariopro.crud.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.UserRepository;
import com.inventariopro.crud.services.ProductoService;

@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde el frontend en localhost
@RestController
@RequestMapping("/productos") // Define la ruta base para este controlador
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserRepository usuarioRepository;

    // Método para obtener la lista de productos del usuario autenticado
    @GetMapping
    public ArrayList<ProductoModel> getProductos(@AuthenticationPrincipal UserDetails usuario) {
        String emailUsuario = usuario.getUsername();
        System.out.println("Email autenticado: " + emailUsuario);
        ArrayList<ProductoModel> productos = this.productoService.getProductosByUsuario(emailUsuario);
        System.out.println("Productos en controlador: " + productos.size());
        return productos;
    }
    // Método para guardar un nuevo producto en la base de datos

    @PostMapping
    public ProductoModel saveProducto(@RequestBody ProductoModel producto, @AuthenticationPrincipal UserDetails usuario) {
        if (usuario == null) {
            System.out.println("❌ Usuario no autenticado. El token no es válido.");
            throw new RuntimeException("Usuario no autenticado.");
        }
        System.out.println("✅ Usuario autenticado: " + usuario.getUsername());

        System.out.println("Buscando usuario con email: " + usuario.getUsername());
        User usuarioEntidad = usuarioRepository.findByEmail(usuario.getUsername())
            .orElseThrow(() -> {
                System.out.println("❌ Usuario no encontrado en la base de datos.");
                return new RuntimeException("Usuario no encontrado.");
            });
        System.out.println("✅ Usuario encontrado: " + usuarioEntidad.getEmail());

        System.out.println("Datos del producto recibido: " + producto);
        producto.setUser(usuarioEntidad);

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
