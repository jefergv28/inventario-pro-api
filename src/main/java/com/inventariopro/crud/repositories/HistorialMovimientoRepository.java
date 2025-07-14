package com.inventariopro.crud.repositories;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.HistorialMovimientoModel;

@Repository
public interface HistorialMovimientoRepository extends JpaRepository<HistorialMovimientoModel, Long> {

    @Transactional
    void deleteByProductoId(Long productoId);

    List<HistorialMovimientoModel> findByUsuario_Email(String email);

}
