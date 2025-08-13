package com.inventariopro.crud.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventariopro.crud.dto.MapStringBooleanType;
import com.inventariopro.crud.dto.ProductoDTO;
import com.inventariopro.crud.models.CategoriaModel;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.CategoriaRepository;
import com.inventariopro.crud.repositories.UserRepository;
import com.inventariopro.crud.services.HistorialMovimientoService;
import com.inventariopro.crud.services.ProductoService;
import com.inventariopro.crud.services.ProveedorService;
import com.inventariopro.crud.services.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("productos")
public class ProductoController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private HistorialMovimientoService historialMovimientoService;



    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Boolean> getUserPermissions(String username) {
        try {
            Optional<User> userOptional = usuarioService.getByEmail(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPermissionsJson() != null) {
                    return objectMapper.readValue(user.getPermissionsJson(), new MapStringBooleanType());
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Error al leer permisos del usuario", e);
        }
        return null;
    }

    private User getUserFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equalsIgnoreCase("image/jpeg") ||
                        contentType.equalsIgnoreCase("image/png") ||
                        contentType.equalsIgnoreCase("image/jpg"))) {
            throw new RuntimeException("Solo se permiten imágenes JPEG o PNG");
        }
    }

    private String storeImage(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalName;
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(UPLOAD_DIR);
        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }
        java.nio.file.Path filePath = uploadPath.resolve(fileName);
        System.out.println("Guardando imagen en: " + filePath.toAbsolutePath());
        file.transferTo(filePath.toFile());
        return fileName;
    }

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

        Map<String, Boolean> userPermissions = getUserPermissions(usuario.getUsername());
        if (userPermissions == null || !userPermissions.getOrDefault("addProduct", false)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del producto es requerido");
            }

            User userEntity = usuarioRepository.findByEmail(usuario.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ProveedorModel proveedor = proveedorService.obtenerPorIdYUsuario(providerId, userEntity)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o no pertenece al usuario"));

            CategoriaModel categoria = categoriaRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            ProductoModel producto = new ProductoModel();
            producto.setNombreProducto(name);
            producto.setCantidadProducto(quantity);
            producto.setDescripcionProducto(description);
            producto.setCategoria(categoria);
            producto.setProveedor(proveedor);
            producto.setPrecioProducto(price);
            producto.setUsuario(userEntity);

            if (image != null && !image.isEmpty()) {
                validateImageFile(image);
                String fileName = storeImage(image);
                producto.setImageUrl("/uploads/" + fileName);
            }

            ProductoModel productoGuardado = productoService.saveProducto(producto, userEntity.getId());

            if (quantity > 0) {
                historialMovimientoService.registrarMovimiento(
                        productoGuardado,
                        userEntity.getEmail(),
                        "ENTRADA",
                        quantity
                );
            }

            ProductoModel productoActualizado = productoService.getProductoByIdAndUsuario(productoGuardado.getId(), usuario.getUsername());
            ProductoDTO productoDTO = new ProductoDTO(productoActualizado);

            return ResponseEntity.ok(productoDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al guardar el producto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

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

        Map<String, Boolean> userPermissions = getUserPermissions(usuario.getUsername());
        if (userPermissions == null || !userPermissions.getOrDefault("editProduct", false)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            User userEntity = usuarioRepository.findByEmail(usuario.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ProveedorModel proveedor = proveedorService.obtenerPorIdYUsuario(providerId, userEntity)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o no pertenece al usuario"));

            CategoriaModel categoria = categoriaRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            ProductoModel productoParaActualizar = new ProductoModel();
            productoParaActualizar.setId(id);
            productoParaActualizar.setNombreProducto(name);
            productoParaActualizar.setCantidadProducto(quantity);
            productoParaActualizar.setDescripcionProducto(description);
            productoParaActualizar.setCategoria(categoria);
            productoParaActualizar.setProveedor(proveedor);
            productoParaActualizar.setPrecioProducto(price);
            productoParaActualizar.setUsuario(userEntity);

            if (image != null && !image.isEmpty()) {
                validateImageFile(image);
                String fileName = storeImage(image);
                productoParaActualizar.setImageUrl("/uploads/" + fileName);
            } else {
                ProductoModel productoActualEnDB = productoService.getProductoByIdAndUsuario(id, userEntity.getUsername());
                if (productoActualEnDB != null) {
                    productoParaActualizar.setImageUrl(productoActualEnDB.getImageUrl());
                }
            }

            ProductoModel actualizado = productoService.updateById(productoParaActualizar, id, userEntity.getId());
            ProductoDTO actualizadoDTO = new ProductoDTO(actualizado);

            return ResponseEntity.ok(actualizadoDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar el producto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarProductos(@AuthenticationPrincipal UserDetails usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        try {
            List<ProductoModel> productos = productoService.getProductosByUsuario(usuario.getUsername());
            List<ProductoDTO> productosDTO = productos.stream()
                    .map(ProductoDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener productos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id, @AuthenticationPrincipal UserDetails usuario) {
        Map<String, Boolean> userPermissions = getUserPermissions(usuario.getUsername());
        if (userPermissions == null || !userPermissions.getOrDefault("viewProduct", false)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            ProductoModel producto = productoService.getProductoByIdAndUsuario(id, usuario.getUsername());
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
            }
            ProductoDTO productoDTO = new ProductoDTO(producto);
            return ResponseEntity.ok(productoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener producto: " + e.getMessage());
        }
    }

@DeleteMapping("/{id}")
public ResponseEntity<?> eliminarProducto(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    // 1. Verificamos los permisos y obtenemos el usuario
    User user = getUserFromUserDetails(userDetails);

    try {
        // --- AQUÍ ESTÁ EL CAMBIO CRUCIAL ---
        // Ya no verificamos el historial de movimientos aquí.
        // Simplemente llamamos al servicio para que desactive el producto.
        productoService.eliminarProducto(id, user.getId());

        return ResponseEntity.ok("Producto desactivado correctamente.");
    } catch (Exception e) {
        // El servicio lanzará una excepción si el producto no se encuentra o hay otro error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error al desactivar producto: " + e.getMessage());
    }
}

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Boolean> userPermissions = getUserPermissions(userDetails.getUsername());
        if (userPermissions == null || !userPermissions.getOrDefault("editProduct", false)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            User usuario = getUserFromUserDetails(userDetails);
            Optional<ProductoModel> productoOpt = productoService.obtenerPorIdYUsuario(id, usuario);

            if (productoOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Producto no encontrado o no autorizado");
            }

            ProductoModel producto = productoOpt.get();
            producto.setActivo(false);
            productoService.guardarProducto(producto);

            return ResponseEntity.ok("Producto desactivado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al desactivar producto");
        }
    }

    @GetMapping("/recientes")
public ResponseEntity<?> obtenerProductosRecientes(@AuthenticationPrincipal UserDetails usuario) {
    try {
        // Debes implementar este método en tu ProductoService
        // para que traiga los productos más recientes del usuario.
        List<ProductoModel> productosRecientes = productoService.getProductosRecientesByUsuario(usuario.getUsername());

        List<ProductoDTO> productosDTO = productosRecientes.stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productosDTO);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener productos recientes: " + e.getMessage());
    }
}
}