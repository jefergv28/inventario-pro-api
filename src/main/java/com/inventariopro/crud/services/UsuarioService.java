package com.inventariopro.crud.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional; // Importar HashMap
import java.util.UUID;     // Importar Map

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventariopro.crud.dto.ActualizarUsuarioDTO;
import com.inventariopro.crud.dto.CreateUserRequestDTO;
import com.inventariopro.crud.models.Role;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.models.UserStatus;
import com.inventariopro.crud.repositories.UserRepository;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${file.upload-dir}")
    private String uploadDir;

    // --- MÉTODO MODIFICADO: crearUsuario ---
    public User crearUsuario(User usuario) {
        // Validación para evitar duplicados por email
        if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo electrónico.");
        }

        // 1. Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 2. Asignar el rol de ADMINISTRADOR por defecto para nuevos registros
        usuario.setRole(Role.ADMIN);

        // 3. Asignar el estado APROBADO por defecto
        usuario.setStatus(UserStatus.APPROVED);

        // 4. Asignar todos los permisos por defecto para un ADMINISTRADOR
        Map<String, Boolean> adminPermissions = new HashMap<>();
        adminPermissions.put("add", true);
        adminPermissions.put("editUser", true);
        adminPermissions.put("deleteUser", true);
        adminPermissions.put("createUser", true); // Permiso para crear otros usuarios/empleados
        adminPermissions.put("viewProduct", true);
        adminPermissions.put("addProduct", true);
        adminPermissions.put("editProduct", true);
        adminPermissions.put("deleteProduct", true);
        adminPermissions.put("viewCategory", true);
        adminPermissions.put("addCategory", true);
        adminPermissions.put("editCategory", true);
        adminPermissions.put("deleteCategory", true);
        adminPermissions.put("viewProvider", true);
        adminPermissions.put("addProvider", true);
        adminPermissions.put("editProvider", true);
        adminPermissions.put("deleteProvider", true);
        // Puedes añadir más permisos si los tienes definidos en tu sistema

        try {
            String permissionsJson = objectMapper.writeValueAsString(adminPermissions);
            usuario.setPermissionsJson(permissionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar permisos del administrador: " + e.getMessage(), e);
        }

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

    public List<User> findUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> obtenerEmpleadosPorCreador(Long creatorId) {
        return userRepository.findByCreatorId(creatorId);
    }

    public User crearEmpleado(CreateUserRequestDTO requestDTO, Long creatorId) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo electrónico.");
        }

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creador no encontrado"));

        User nuevoEmpleado = new User();
        nuevoEmpleado.setName(requestDTO.getName());
        nuevoEmpleado.setEmail(requestDTO.getEmail());
        nuevoEmpleado.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        nuevoEmpleado.setRole(Role.fromString(requestDTO.getRole()));
        nuevoEmpleado.setCreator(creator);
        nuevoEmpleado.setStatus(UserStatus.APPROVED);

        try {
            String permissionsJson = objectMapper.writeValueAsString(requestDTO.getPermissions());
            nuevoEmpleado.setPermissionsJson(permissionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar permisos: " + e.getMessage(), e);
        }

        return userRepository.save(nuevoEmpleado);
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
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path filePath = uploadPath.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), filePath);

            usuario.setProfilePicture("/uploads/" + nombreArchivo);
            userRepository.save(usuario);

            System.out.println("Imagen guardada correctamente en: " + filePath.toString());

            return usuario.getProfilePicture();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error al guardar la imagen: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getById(String id) {
        try {
            Long userId = Long.valueOf(id);
            return userRepository.findById(userId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<User> updateUser(String id, User updatedUser) {
        try {
            Long userId = Long.valueOf(id);
            return userRepository.findById(userId).map(user -> {
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                user.setRole(updatedUser.getRole());
                user.setPermissionsJson(updatedUser.getPermissionsJson());
                return userRepository.save(user);
            });
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public boolean deleteUser(String id) {
        try {
            Long userId = Long.valueOf(id);
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

   public List<User> obtenerUsuariosPendientesCreadosPorAdmins() {
    List<User> pendingUsers = userRepository.findByStatus(UserStatus.PENDING);
    return pendingUsers.stream()
        .filter(user -> {
            User creator = user.getCreator();
            return creator != null && creator.getRole() == Role.ADMIN;

        })
        .toList();
}

public List<User> obtenerUsuariosPendientes() {
    return userRepository.findByStatus(UserStatus.PENDING);
}

}