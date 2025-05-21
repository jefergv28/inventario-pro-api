package com.inventariopro.crud.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inventariopro.crud.dto.ProductoDTO;
import com.inventariopro.crud.models.CategoriaModel;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.CategoriaRepository;
import com.inventariopro.crud.repositories.UserRepository;
import com.inventariopro.crud.services.ProductoService;
import com.inventariopro.crud.services.ProveedorService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private final String UPLOAD_DIR = "uploads";

    // Endpoint para obtener todos los productos del usuario autenticado
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getProductos(@AuthenticationPrincipal UserDetails usuario) {
        try {
            ArrayList<ProductoModel> productos = productoService.getProductosByUsuario(usuario.getUsername());

            // Convertir cada ProductoModel a ProductoDTO
            List<ProductoDTO> productosDTO = productos.stream()
                    .map(ProductoDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para guardar un nuevo producto
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveProducto(
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("providerId") Long providerId,
            @RequestParam("price") double price,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails usuario) {

        try {
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
            }

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del producto es requerido");
            }

            // Buscar usuario autenticado
            User userEntity = usuarioRepository.findByEmail(usuario.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Buscar proveedor por ID
            ProveedorModel proveedor = proveedorService.obtenerPorIdYUsuario(providerId, userEntity)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o no pertenece al usuario"));

            // Buscar categoría por ID
            CategoriaModel categoria = categoriaRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            // Crear el producto
            ProductoModel producto = new ProductoModel();
            producto.setNombreProducto(name);
            producto.setCantidadProducto(quantity);
            producto.setDescripcionProducto(description);
            producto.setCategoria(categoria);
            producto.setProveedor(proveedor);
            producto.setPrecioProducto(price);
            producto.setUsuario(userEntity);

            // Manejar imagen
            if (image != null && !image.isEmpty()) {
                validateImageFile(image);
                String fileName = storeImage(image);
                producto.setImageUrl(fileName);
            }

            ProductoModel savedProducto = productoService.saveProducto(producto);
            return ResponseEntity.ok(savedProducto);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al guardar el producto: " + e.getMessage());
        }
    }

    // Nuevo endpoint para obtener un producto por id (para edición)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails usuario) {
        try {
            ProductoModel producto = productoService.getProductoByIdAndUsuario(id, usuario.getUsername());
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
            }
            return ResponseEntity.ok(new ProductoDTO(producto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    // Nuevo endpoint para actualizar (editar) un producto existente
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("providerId") Long providerId,
            @RequestParam("price") double price,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails usuario) {

        try {
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
            }

            ProductoModel productoExistente = productoService.getProductoByIdAndUsuario(id, usuario.getUsername());
            if (productoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
            }

            User userEntity = usuarioRepository.findByEmail(usuario.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ProveedorModel proveedor = proveedorService.obtenerPorIdYUsuario(providerId, userEntity)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o no pertenece al usuario"));

            CategoriaModel categoria = categoriaRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            // Actualizar campos
            productoExistente.setNombreProducto(name);
            productoExistente.setCantidadProducto(quantity);
            productoExistente.setDescripcionProducto(description);
            productoExistente.setCategoria(categoria);
            productoExistente.setProveedor(proveedor);
            productoExistente.setPrecioProducto(price);

            if (image != null && !image.isEmpty()) {
                validateImageFile(image);
                String fileName = storeImage(image);
                productoExistente.setImageUrl("/" + UPLOAD_DIR + "/" + fileName);
            }

            ProductoModel actualizado = productoService.saveProducto(productoExistente);
            return ResponseEntity.ok(actualizado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar el producto: " + e.getMessage());
        }
    }

    // Endpoint para eliminar un producto por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            boolean deleted = productoService.deleteProducto(id);
            return deleted ? ResponseEntity.ok("✅ Producto eliminado")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Producto no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto");
        }
    }

    // Endpoint para obtener todos los proveedores (en este caso se obtienen todos los usuarios)
    @GetMapping("/proveedores")
    public ResponseEntity<List<User>> getAllProveedores() {
        try {
            List<User> proveedores = usuarioRepository.findAll(); // Suponiendo que los proveedores son usuarios
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para validar que el archivo sea una imagen válida
    private void validateImageFile(MultipartFile file) {
        if (file == null) {
            throw new RuntimeException("El archivo no puede ser nulo");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("El archivo debe ser una imagen (JPEG, PNG, etc.)");
        }

        if (file.getSize() > 5_000_000) {
            throw new RuntimeException("La imagen es demasiado grande (máximo 5MB)");
        }
    }

    // Método para almacenar la imagen en el directorio UPLOAD_DIR
    private String storeImage(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        String fileName = StringUtils.cleanPath(System.currentTimeMillis() + "_" + file.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new RuntimeException("Nombre de archivo inválido: " + fileName);
        }

        Path targetLocation = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
