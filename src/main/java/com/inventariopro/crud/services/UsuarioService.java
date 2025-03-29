package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.UsuarioModel;
import com.inventariopro.crud.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioModel registrarUsuario(UsuarioModel usuario) {
      if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
        throw new IllegalArgumentException("El email ya esta registrado");
      }


        // Encripta la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean validarCredenciales(String email, String password) {
      Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(email);

      if (usuarioOpt.isPresent()) {
          UsuarioModel usuario = usuarioOpt.get();

          System.out.println("📌 Usuario encontrado: " + usuario.getEmail());
          System.out.println("📌 Contraseña ingresada: " + password);
          System.out.println("📌 Contraseña en BD: " + usuario.getPassword());

          boolean match = passwordEncoder.matches(password, usuario.getPassword());
          System.out.println("📌 ¿Coincide la contraseña?: " + match);

          return match;
      }

      System.out.println("❌ Usuario no encontrado.");
      return false;
  }


    public List<UsuarioModel> obtenerUsuarios() {

      throw new UnsupportedOperationException("Unimplemented method 'obtenerUsuarios'");
    }

    public Optional<UsuarioModel> obtenerPorId(Long id) {

      throw new UnsupportedOperationException("Unimplemented method 'obtenerPorId'");
    }

    public boolean eliminarUsuario(Long id) {

      throw new UnsupportedOperationException("Unimplemented method 'eliminarUsuario'");
    }

}