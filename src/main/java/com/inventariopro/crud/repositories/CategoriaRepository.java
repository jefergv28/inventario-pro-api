package com.inventariopro.crud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.CategoriaModel;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

// Cuenta las categorías distintas para un usuario
@Query("SELECT COUNT(DISTINCT c.id) FROM CategoriaModel c WHERE c.usuario.id = :usuarioId")
long countDistinctByUsuarioId(@Param("usuarioId") Long usuarioId);

List<CategoriaModel> findByNombreContainingIgnoreCase(String nombre);
}