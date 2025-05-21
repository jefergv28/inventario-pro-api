package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.ProveedorDTO;
import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.UserRepository;
import com.inventariopro.crud.services.ProveedorService;

@RestController
@RequestMapping("/proveedores")
@CrossOrigin(origins = "http://localhost:3000")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private UserRepository usuarioRepository;

    private User getUserFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> obtenerProveedores(@AuthenticationPrincipal UserDetails userDetails) {
        User usuario = getUserFromUserDetails(userDetails);
        List<ProveedorModel> proveedores = proveedorService.obtenerProveedoresPorUsuario(usuario);

        List<ProveedorDTO> proveedoresDTO = proveedores.stream()
                .map(ProveedorDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(proveedoresDTO);
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> guardarProveedor(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody ProveedorModel proveedor) {
        User usuario = getUserFromUserDetails(userDetails);
        proveedor.setUsuario(usuario);
        ProveedorModel saved = proveedorService.guardarProveedor(proveedor);
        return ResponseEntity.ok(new ProveedorDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtenerProveedorPorId(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User usuario = getUserFromUserDetails(userDetails);
        Optional<ProveedorModel> proveedorOpt = proveedorService.obtenerPorIdYUsuario(id, usuario);

        return proveedorOpt
                .map(prov -> ResponseEntity.ok(new ProveedorDTO(prov)))
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o no autorizado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(@PathVariable Long id,
                                                 @RequestBody ProveedorModel proveedorActualizado,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        User usuario = getUserFromUserDetails(userDetails);
        Optional<ProveedorModel> proveedorExistente = proveedorService.obtenerPorIdYUsuario(id, usuario);

        if (proveedorExistente.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        ProveedorModel proveedor = proveedorExistente.get();
        proveedor.setNombre(proveedorActualizado.getNombre());
        proveedor.setContacto(proveedorActualizado.getContacto());
        proveedor.setDireccion(proveedorActualizado.getDireccion());
        // Agrega más campos si los hay

        ProveedorModel actualizado = proveedorService.guardarProveedor(proveedor);
        return ResponseEntity.ok(new ProveedorDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProveedor(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User usuario = getUserFromUserDetails(userDetails);
        boolean eliminado = proveedorService.eliminarProveedor(id, usuario);
        return eliminado
                ? ResponseEntity.ok("Proveedor eliminado")
                : ResponseEntity.status(404).body("Proveedor no encontrado o no autorizado");
    }
}
