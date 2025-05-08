package com.inventariopro.crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProveedorModel;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorModel, Long> {

    Optional<ProveedorModel> findByNombre(String providerName);

    List<ProveedorModel> findByNombreContainingIgnoreCase(String query);
}
