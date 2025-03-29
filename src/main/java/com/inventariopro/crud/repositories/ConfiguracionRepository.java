package com.inventariopro.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ConfiguracionModel;

@Repository
public interface ConfiguracionRepository extends JpaRepository<ConfiguracionModel, Long> {
}
