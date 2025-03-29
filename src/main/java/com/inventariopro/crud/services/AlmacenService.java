package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.AlmacenModel;
import com.inventariopro.crud.repositories.AlmacenRepository;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    public List<AlmacenModel> obtenerAlmacenes() {
        return almacenRepository.findAll();
    }

    public AlmacenModel guardarAlmacen(AlmacenModel almacen) {
        return almacenRepository.save(almacen);
    }

    public Optional<AlmacenModel> obtenerPorId(Long id) {
        return almacenRepository.findById(id);
    }

    public boolean eliminarAlmacen(Long id) {
        try {
            almacenRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
