package com.inventariopro.crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;

// Indica que esta interfaz es un componente de repositorio de Spring
@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {
    List<ProductoModel> findByUsuario(User usuario);

    Optional<ProductoModel> findByIdAndUsuario(Long id, User usuario);
    // ProductoRepository.java
List<ProductoModel> findByUsuarioAndActivoTrue(User usuario);

}
