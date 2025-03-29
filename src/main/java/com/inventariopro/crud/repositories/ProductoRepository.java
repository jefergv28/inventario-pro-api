package com.inventariopro.crud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.UsuarioModel;

// Indica que esta interfaz es un componente de repositorio de Spring
@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {
    List<ProductoModel> findByUsuario(UsuarioModel usuario);
}
