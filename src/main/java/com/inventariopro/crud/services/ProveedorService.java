package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.repositories.ProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<ProveedorModel> obtenerProveedores() {
        return proveedorRepository.findAll();
    }

    public ProveedorModel guardarProveedor(ProveedorModel proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Optional<ProveedorModel> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public boolean eliminarProveedor(Long id) {
        try {
            proveedorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
