package com.inventariopro.crud.controllers;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.inventariopro.crud.dto.ActualizarUsuarioDTO;
import com.inventariopro.crud.dto.UsuarioDTO;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<User> crearUsuario(@RequestBody User usuario) {
        User nuevoUsuario = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> obtenerTodosUsuarios() {
        List<User> usuarios = usuarioService.obtenerTodosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Obtener un usuario por su id
    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<User> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<User> actualizarUsuario(@PathVariable Long id, @RequestBody User usuario) {
        Optional<User> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            User usuarioActualizado = usuarioService.actualizarUsuario(usuario);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<User> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioExistente.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

        // Ahora simplemente creamos una instancia de UsuarioDTO usando el constructor
        // y este se encargará de mapear todos los campos correctamente, incluyendo los booleanos.
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

            // Aquí llamamos al servicio y esperamos que nos devuelva la URL de la imagen.
            String profilePictureUrl = usuarioService.actualizarFotoPerfil(userDetails.getUsername(), imagen);

            // Devolvemos la URL de la foto en un objeto JSON,
            // que el frontend podrá leer fácilmente.
            return ResponseEntity.ok(Collections.singletonMap("profilePicture", profilePictureUrl));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }
}

