package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    public User crearUsuario(User usuario) {
        return userRepository.save(usuario);
    }

    public List<User> obtenerTodosUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id);  // ← antes estaba devolviendo Optional vacío
    }

    public User actualizarUsuario(User usuario) {
        return userRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);  // ← antes eliminaba todos los usuarios
    }
}
