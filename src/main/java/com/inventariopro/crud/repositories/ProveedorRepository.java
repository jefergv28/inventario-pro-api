package com.inventariopro.crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProveedorModel;
import com.inventariopro.crud.models.User;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorModel, Long> {

    List<ProveedorModel> findByUsuario(User usuario);

    Optional<ProveedorModel> findByNombreAndUsuario(String nombre, User usuario);

    List<ProveedorModel> findByNombreContainingIgnoreCaseAndUsuario(String query, User usuario);

    Optional<ProveedorModel> findByIdAndUsuario(Long id, User usuario);

}
