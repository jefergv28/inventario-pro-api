package com.inventariopro.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.HistorialMovimientoModel;

@Repository
public interface HistorialMovimientoRepository extends JpaRepository<HistorialMovimientoModel, Long> {
}
