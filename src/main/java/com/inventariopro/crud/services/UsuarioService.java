package com.inventariopro.crud.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path; // Importa esta clase para manejar rutas con NIO
import java.nio.file.Paths;  // Importa esta clase
import java.util.List; // Importa esta clase
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Para inyectar propiedades
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inventariopro.crud.dto.ActualizarUsuarioDTO;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inyecta la ruta de subida desde application.properties
    @Value("${file.upload-dir}") // Define esta propiedad en tu application.properties
    private String uploadDir;

    public User crearUsuario(User usuario) {
        return userRepository.save(usuario);
    }

    public List<User> obtenerTodosUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }

    public User actualizarUsuario(User usuario) {
        return userRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void actualizarConfiguracion(String username, ActualizarUsuarioDTO dto) {
       User usuario = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        usuario.setName(dto.getName());
       if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
    usuario.setEmail(dto.getEmail());
}

        usuario.setLanguage(dto.getLanguage());
        usuario.setNotifications(dto.isNotifications());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(usuario);
    }

    public User obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    @SuppressWarnings("CallToPrintStackTrace")
    public String actualizarFotoPerfil(String email, MultipartFile imagen) throws IOException {
        User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (imagen.isEmpty()) {
            throw new IOException("No se seleccionó ninguna imagen.");
        }

        try {
            // Crea la ruta absoluta para guardar los archivos
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            // Crea el directorio si no existe
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Genera un nombre único para la imagen
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path filePath = uploadPath.resolve(nombreArchivo);

            // Guarda el archivo
            Files.copy(imagen.getInputStream(), filePath);

            // Guarda la URL relativa o el nombre de archivo en la base de datos
            // Esta es la ruta que tu frontend usará para acceder a la imagen
            usuario.setProfilePicture("/uploads/" + nombreArchivo); // Asegúrate que tu frontend apunte a un recurso estático /uploads

            userRepository.save(usuario);

            System.out.println("Imagen guardada correctamente en: " + filePath.toString());

            return usuario.getProfilePicture();

        } catch (IOException e) {
            // Loguea la excepción completa para depuración
            e.printStackTrace();
            throw new IOException("Error al guardar la imagen: " + e.getMessage(), e);
        }
    }
}