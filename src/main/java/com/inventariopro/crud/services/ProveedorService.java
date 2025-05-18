package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.repositories.ProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Obtener todos los proveedores de un usuario específico
    public List<ProveedorModel> obtenerProveedoresPorUsuario(User usuario) {
        return proveedorRepository.findByUsuario(usuario);
    }

    public Optional<ProveedorModel> obtenerPorIdYUsuario(Long id, User usuario) {
        return proveedorRepository.findById(id)
                .filter(proveedor -> proveedor.getUsuario().equals(usuario));
    }

    public ProveedorModel guardarProveedor(ProveedorModel proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public boolean eliminarProveedor(Long id, User usuario) {
        Optional<ProveedorModel> proveedorOpt = obtenerPorIdYUsuario(id, usuario);
        if (proveedorOpt.isPresent()) {
            proveedorRepository.delete(proveedorOpt.get());
            return true;
        }
        return false;
    }
}
