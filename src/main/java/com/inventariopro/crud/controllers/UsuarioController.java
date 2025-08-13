package com.inventariopro.crud.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventariopro.crud.dto.ActualizarUsuarioDTO;
import com.inventariopro.crud.dto.CreateUserRequestDTO;
import com.inventariopro.crud.dto.MapStringBooleanType;
import com.inventariopro.crud.dto.UsuarioDTO;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.services.EmailService;
import com.inventariopro.crud.services.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    // Método auxiliar para obtener permisos del usuario autenticado
    @SuppressWarnings("UseSpecificCatch")
    private Map<String, Boolean> getUserPermissions(String username) {
        try {
            Optional<User> userOptional = usuarioService.getByEmail(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPermissionsJson() != null) {
                    return objectMapper.readValue(user.getPermissionsJson(), new MapStringBooleanType());
                }
            }
        } catch (Exception e) {
            log.error("Error al leer permisos del usuario", e);
        }
        return Collections.emptyMap();
    }

    // Endpoint para obtener todos los empleados (solo para administradores)
    @GetMapping("/empleados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        List<User> users = usuarioService.getAllUsers();
        // Filtra al usuario actual para que no se pueda eliminar a sí mismo
        users.removeIf(user -> user.getEmail().equals(userDetails.getUsername()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint para obtener un usuario por ID (con permiso)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Boolean> userPermissions = getUserPermissions(userDetails.getUsername());
        if (!userPermissions.getOrDefault("editUser", false)) {
            return new ResponseEntity<>("No tienes permiso para ver este usuario.", HttpStatus.FORBIDDEN);
        }

        Optional<User> userOptional = usuarioService.getById(id);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para editar un usuario, incluyendo sus permisos (con permiso)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable String id,
        @RequestBody User user,
        @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Boolean> userPermissions = getUserPermissions(userDetails.getUsername());
        if (!userPermissions.getOrDefault("editUser", false)) {
            return new ResponseEntity<>("No tienes permiso para editar usuarios.", HttpStatus.FORBIDDEN);
        }

        try {
            Optional<User> updatedUser = usuarioService.updateUser(id, user);
            if (updatedUser.isPresent()) {
                return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error al actualizar usuario: ", e);
            return new ResponseEntity<>("Error al actualizar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para eliminar un usuario (con permiso)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
        @PathVariable String id,
        @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Boolean> userPermissions = getUserPermissions(userDetails.getUsername());
        if (!userPermissions.getOrDefault("deleteUser", false)) {
            return new ResponseEntity<>("No tienes permiso para eliminar usuarios.", HttpStatus.FORBIDDEN);
        }

        try {
            boolean deleted = usuarioService.deleteUser(id);
            if (deleted) {
                return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error al eliminar usuario: ", e);
            return new ResponseEntity<>("Error al eliminar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/configuracion")
    public ResponseEntity<?> actualizarConfiguracionUsuario(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ActualizarUsuarioDTO dto
    ) {
        try {
            usuarioService.actualizarConfiguracion(userDetails.getUsername(), dto);
            return ResponseEntity.ok("Configuración actualizada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioActual(@AuthenticationPrincipal UserDetails userDetails) {
        User usuario = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername());
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/me/foto")
    public ResponseEntity<?> actualizarFotoPerfil(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam("imagen") MultipartFile imagen) {
        try {
            if (userDetails == null || userDetails.getUsername() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
            }
            String profilePictureUrl = usuarioService.actualizarFotoPerfil(userDetails.getUsername(), imagen);
            return ResponseEntity.ok(Collections.singletonMap("profilePicture", profilePictureUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // Endpoint para crear empleado
    @PostMapping("/crear-empleado")
    public ResponseEntity<User> crearEmpleado(@RequestBody CreateUserRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Boolean> userPermissions = getUserPermissions(userDetails.getUsername());
        if (!userPermissions.getOrDefault("createUser", false)) { // Usar un permiso más genérico como "createUser"
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        try {
            User loggedInUser = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername());
            Long creatorId = loggedInUser.getId();
            String initialPassword = requestDTO.getPassword();

            User nuevoEmpleado = usuarioService.crearEmpleado(requestDTO, creatorId);

            if (nuevoEmpleado != null) {
                emailService.sendInitialPasswordEmail(
                    nuevoEmpleado.getEmail(),
                    nuevoEmpleado.getName(),
                    initialPassword
                );
            }
            return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear empleado: Email duplicado", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error interno al crear empleado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/pending")
@PreAuthorize("hasRole('ADMIN')") // Opcional: solo admins acceden
public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPendientes(@AuthenticationPrincipal UserDetails userDetails) {
    List<User> usuariosPendientes = usuarioService.obtenerUsuariosPendientes();
    List<UsuarioDTO> usuariosDTO = usuariosPendientes.stream()
        .map(UsuarioDTO::new)
        .toList(); // Asegúrate de que tu UsuarioDTO contenga permisos y estado
    return ResponseEntity.ok(usuariosDTO);
}

}