package com.inventariopro.crud.controllers;

import com.inventariopro.crud.models.UsuarioModel;
import com.inventariopro.crud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioModel> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @PostMapping
    public UsuarioModel guardarUsuario(@RequestBody UsuarioModel usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @GetMapping("/{id}")
    public Optional<UsuarioModel> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        return eliminado ? "Usuario eliminado" : "Error al eliminar el usuario";
    }
}
