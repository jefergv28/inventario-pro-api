package com.inventariopro.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.ConfiguracionModel;
import com.inventariopro.crud.repositories.ConfiguracionRepository;

@Service
public class ConfiguracionService {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    public Optional<ConfiguracionModel> obtenerConfiguracion(Long id) {
        return configuracionRepository.findById(id);
    }

    public ConfiguracionModel guardarConfiguracion(ConfiguracionModel configuracion) {
        return configuracionRepository.save(configuracion);
    }
}
