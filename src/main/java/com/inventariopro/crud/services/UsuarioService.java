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
    private UserRepository UserRepository;

    public User crearUsuario(User usuario) {
        return UserRepository.save(usuario);
    }

    public List<User> obtenerTodosUsuarios() {
        return UserRepository.findAll();
    }

    public Optional<User> obtenerUsuarioPorId(Long id) {
        return UserRepository.findById(id);
    }

    public User actualizarUsuario(User usuario) {
        return UserRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        UserRepository.deleteById(id);
    }
}
